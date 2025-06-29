package src;
import java.util.*;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import src.TimestampParser;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.stream.Stream;


public class FileComparator {

    File[]  samplefiles;
    String[] filenames;
    String[] extensions;
    int[]  sizes;
    String[] dates;
    Boolean[] bool_mask;

    // Controls if checks are active or not
    public final String FILENAME = "name";
    public final String EXTENSION = "extension";
    public final String DATE = "date";
    public final String HOUR = "hour";
    public final String MINUTE = "minute";
    public final String SECOND = "second";
    public final String SAMECONTENT = "same_content";
    public final String SIMILARCONTENT = "similar_content";

    String [] checkList = {FILENAME, EXTENSION, DATE, HOUR, MINUTE, SECOND, SAMECONTENT, SIMILARCONTENT};
    Dictionary<String, Boolean> check_enabled= new Hashtable<>();
    Dictionary<String, Boolean> check_results = new Hashtable<>();



    public FileComparator() {

        for (String c: checkList) {
            this.check_enabled.put(c, false);
            this.check_results.put(c, false);
        }

    }
    public void enable_check(String checkname, Boolean enabled) {
        this.check_enabled.put(checkname, enabled);
        this.check_results.put(checkname, false);
    }

    public void set_reference_sample(File[] sample) throws IOException {

        this.samplefiles = FileComparator.searchFiles(sample, false);
        int filecount = this.samplefiles.length;
        this.bool_mask = new Boolean[filecount];
        this.filenames = new String[filecount];
        this.extensions = new String[filecount];
        this.sizes = new int[filecount];
        this.dates = new String[filecount];

        for (Boolean b : this.bool_mask) b = false;

        for (int i = 0; i < filecount; i++) {
            String filename = this.samplefiles[i].getName();
            System.out.println("analyzing sample: " + filename);
            int dotIndex = filename.lastIndexOf('.');
            this.filenames[i] = this.separate_filename(this.samplefiles[i]);
            this.extensions[i] = this.separate_extension(this.samplefiles[i]);
            this.sizes[i] = (int) this.samplefiles[i].length();
            this.dates[i] = this.getExifTag(this.samplefiles[i], "Date/Time Original");
        }

    }

    public String getExtensions() {
        StringBuilder sb = new StringBuilder();
        for (String name : this.extensions) {
            sb.append(name).append(" ; ");
        }
        return sb.toString();
    }

    public String getFileNames( int limit){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String name : this.filenames) {
            sb.append(name).append(" ; ");
            count++;
            if (count >= limit) break;
        }
        int residual = this.filenames.length - count;
        sb.append(".. and "+residual+" more");
        return sb.toString();
    }

    public int getSampleCount() {
        return this.samplefiles.length;
    }
    public Boolean compare(File file){
        //if at least one check is enabled return true else false
        Boolean output = false;
        for (String c : checkList)  output = output | this.check_enabled.get(c);

        //file to compare
        String filename = this.separate_filename(file);
        String extension = this.separate_extension(file);
        String time = this.getExifTag(file, "Date/Time Original");


        if (this.check_enabled.get(FILENAME))   this.check_results.put(FILENAME, check_filename(filename));
        if (this.check_enabled.get(EXTENSION))  this.check_results.put(EXTENSION, check_extension(extension));
        if (this.check_enabled.get(DATE))       this.check_results.put(DATE, check_time(time, DATE));
        if (this.check_enabled.get(HOUR))       this.check_results.put(HOUR, check_time(time,HOUR));
        if (this.check_enabled.get(MINUTE))     this.check_results.put(MINUTE, check_time(time,MINUTE));
        if (this.check_enabled.get(SECOND))     this.check_results.put(SECOND, check_time(time,SECOND));

        //combine results of checks
        for (String c : checkList) {
            if (this.check_enabled.get(c)) output = output & check_results.get(c);
        }
        System.out.println(" - Transfer: " + output);
        return output;
    }//end compare


    public String separate_filename(File file){
        String filename = file.getName();
        int dotIndex = filename.lastIndexOf('.');
        return file.getName().substring(0, dotIndex);
    }

    public String separate_extension(File file) {
        String filename = file.getName();
        int dotIndex = filename.lastIndexOf('.');
        return file.getName().substring(dotIndex, filename.length());
    }

    private Boolean check_filename(String name){
        for (String n : this.filenames){
            if (n.equals(name)) {
                System.out.println(n + " == " + name + " : true");
                return true;
            }
        }
        return false;
    }

    private Boolean check_extension(String extension){
        for (String n : this.extensions){
            if (n.contains(extension)) return true;
        }
        return false;
    }

    private Boolean check_time(String timestamp, String type) {

        TimestampParser fp1 = new TimestampParser(timestamp);
        if (fp1.isValid) {
            for (String t : this.dates) {
                System.out.println(" - " + t);
                if (t == null) continue; // skip null timestamps
                TimestampParser fp2 = new TimestampParser(t);
                Boolean samedate = (fp1.getDay() == fp2.getDay()) && (fp1.getMonth() == fp2.getMonth()) && (fp1.getYear() == fp2.getYear());

                switch (type) {
                    case DATE:
                        return samedate;
                    case HOUR:
                        return samedate && (fp1.getHour() == fp2.getHour());
                    case MINUTE:
                        return samedate && (fp1.getHour() == fp2.getHour()) && (fp1.getMinute() == fp2.getMinute());
                    case SECOND:
                        return samedate && (fp1.getHour() == fp2.getHour()) && (fp1.getMinute() == fp2.getMinute()) && (fp1.getSecond() == fp2.getSecond());
                    default:
                        return false;
                }//end switch
            }//end for
         }
        return false;
    }


    private String getExifTag (File file, String select_tag) {

        try{
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if(tag.getTagName().equals(select_tag)) return  tag.getDescription();
                }//for each tag
            }//for each dir

        } catch (Throwable e) {System.out.println(">No Exif data");}

        return null;

    }//end method

    public static File[] searchFiles(File[] fileList, Boolean recursive) throws IOException {

            File[] checklist = fileList.clone();
            if (recursive) {
                if (checklist != null) {
                    for (File f : checklist) {
                        if (f.isDirectory())
                            //file array with directory
                            fileList = Stream.concat(Arrays.stream(fileList), Arrays.stream(FileComparator.searchFiles(f.listFiles(), true))).toArray(File[]::new);
                    }//end for
                }//end if
            }//end if recursive

        return FileComparator.clean_filelist(fileList);

    }//end getFileList

    private static File[] clean_filelist(File[] fileList) {

        int ignore_count, i;
        ignore_count = 0;
        i = 0;

        for (File f : fileList) if (f.isDirectory() |  f.isHidden() | !f.getName().contains(".") | f.getName().substring(0,1) == "."| f.getName().contains("FileMagnet")) ignore_count++;

        int file_count = fileList.length - ignore_count;
        File[] newList = new File[file_count];

        for (File f : fileList) {
            if (!(f.isDirectory() |  f.isHidden() | !f.getName().contains(".") | f.getName().substring(0,1) == "."| f.getName().contains("FileMagnet"))) {
                newList[i] = f;
                i++;
            }
        }
        return newList;

    }

}
