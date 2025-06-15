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
    String[] names;
    String[] extensions;
    int[]  sizes;
    String[] dates;
    Boolean[] bool_mask;

    // Controls if checks are active or not
    public String FILENAME = "name";
    public String EXTENSION = "extension";
    public String DATE = "date";
    public String HOUR = "hour";
    public String MINUTE = "minute";
    public String SECOND = "second";
    public String SAMECONTENT = "same_content";
    public String SIMILARCONTENT = "similar_content";

    String [] checkNames = {FILENAME, EXTENSION, DATE, HOUR, MINUTE, SECOND, SAMECONTENT, SIMILARCONTENT};
    Dictionary<String, Boolean> check_enabled= new Hashtable<>();
    Dictionary<String, Boolean> check_results = new Hashtable<>();



    public FileComparator() {

        for (String c: checkNames) {
            this.check_enabled.put(c, false);
            this.check_results.put(c, false);
        }

    }
    public void enable_check(String checkname, Boolean Enabled) {
        this.check_enabled.put(checkname, Enabled);
        this.check_results.put(checkname, false);

        Boolean timecheck = checkname.equals(DATE) || checkname.equals(HOUR) || checkname.equals(MINUTE) || checkname.equals(SECOND);

        if (timecheck & this.dates == null) {
            this.dates = new String[this.samplefiles.length];
            for (int i = 0; i < this.samplefiles.length; i++) {
                this.dates[i] = this.getExifTag(this.samplefiles[i], "Date/Time Original");
                if (dates[i] != null) {
                    TimestampParser parser = new TimestampParser(dates[i]);
                    System.out.println("Day: " + parser.getDay() + ", Month: " + parser.getMonth() + ", Year: " + parser.getYear());
                    System.out.println("Hour: " + parser.getHour() + ", Minute: " + parser.getMinute() + ", Second: " + parser.getSecond());

                }
            }
        }

    }

    public void set_reference_sample(File[] sample) throws IOException {

        this.samplefiles = FileComparator.searchFiles(sample, false);
        int filecount = this.samplefiles.length;
        this.bool_mask = new Boolean[filecount];
        this.names = new String[filecount];
        this.extensions = new String[filecount];
        this.sizes = new int[filecount];

        for (Boolean b : this.bool_mask) b = false;

        for (int i = 0; i < filecount; i++) {
            String filename = this.samplefiles[i].getName();
            System.out.println("analyzing sample: " + filename);
            int dotIndex = filename.lastIndexOf('.');
            this.names[i] = this.separate_name(this.samplefiles[i]);
            this.extensions[i] = this.separate_extension(this.samplefiles[i]);
            this.sizes[i] = (int) this.samplefiles[i].length();
        }

    }

    public String getNames( int limit){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String name : this.names) {
            sb.append(name).append(" ; ");
            count++;
            if (count >= limit) break;
        }
        int residual = this.names.length - count;
        sb.append(".. and "+residual+" more");
        return sb.toString();
    }

    public int getSampleCount() {
        return this.samplefiles.length;
    }
    public Boolean compare(File file){
        Boolean output = true;

        //file to compare
        String name = this.separate_name(file);
        String extension = this.separate_extension(file);

        if (this.check_enabled.get("name")) this.check_results.put("extension", check_filename(name));
        if (this.check_enabled.get("extension")) this.check_results.put("extension", check_extension(name));

        //combine results of checks
        for (String c : checkNames) output = output & check_results.get(c);

        return output;
    }//end compare


    public String separate_name(File file){
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
        for (String n : this.names){
            if (n.contains(name)) return true;
        }
        return false;
    }



    private Boolean check_extension(String extension){
        for (String n : this.extensions){
            if (n.contains(extension)) return true;
        }
        return false;
    }

    private Boolean check_date(String date){

        for (String d : this.dates){
            if (d.contains(date)) return true;
        }
        return false;
    }


    private String getExifTag (File file, String select_tag) {

        try{
            System.out.println("Reading file: " + file.getAbsolutePath());
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if(tag.getTagName().equals(select_tag)) return  tag.getDescription();
                }//for each tag
            }//for each dir

        } catch (Throwable e) {e.printStackTrace();}

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

        for (File f : fileList) if (f.isDirectory() |  f.isHidden() | !f.getName().contains(".")) ignore_count++;

        int file_count = fileList.length - ignore_count;
        File[] newList = new File[file_count];

        for (File f : fileList) {
            if (!f.isDirectory() &&  !f.isHidden() && f.getName().contains(".")) {
                newList[i] = f;
                i++;
            }
        }
        return newList;

    }

}
