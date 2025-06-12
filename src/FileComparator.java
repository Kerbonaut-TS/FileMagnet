package src;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;


public class FileComparator {

    File[]  samplefiles;
    String[] names;
    String[] extensions;
    int[]  sizes;
    String[] dates;
    Boolean[] bool_mask;

    Boolean[] checks = {false, false, false ,false};
    int NAME_CHECK = 0;
    int EXT_CHECK = 1;
    int DATE_CHECK = 2;
    int SIZE_CHECK = 3;


    public FileComparator() {


    }
    public void change_settings(Boolean name, Boolean extension, Boolean date, Boolean size) {
        this.checks[NAME_CHECK] = name;
        this.checks[EXT_CHECK] = extension;
        this.checks[DATE_CHECK] = date;
        this.checks[SIZE_CHECK] = size;

        if (date){
            for (int i = 0; i < this.samplefiles.length; i++) {
                this.dates[i] = this.getExifTag(this.samplefiles[i], "Date/Time Original");}
        }
    }

    public void set_reference_sample(File[] sample) throws IOException {

        this.samplefiles = FileComparator.searchFiles(sample, false);
        int filecount = this.samplefiles.length;
        this.bool_mask = new Boolean[filecount];
        this.names = new String[filecount];
        this.extensions = new String[filecount];
        this.dates = new String[filecount];
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
    public Boolean compare(File file){
        Boolean output = true;

        //file to compare
        String name = this.separate_name(file);
        String extension = this.separate_extension(file);

        Boolean [] results = new Boolean[this.checks.length] ;
        for (Boolean b : results) b = false;
        if (checks[NAME_CHECK]) results[NAME_CHECK] = this.check_name(name);
        if (checks[EXT_CHECK])  results[EXT_CHECK] =  this.check_extension(name);

        for (int i = 0; i<this.checks.length; i++) {
            if (this.checks[i]) {
                output = output & results[i];
            }
        }
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

    private Boolean check_name(String name){
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

        try{ Metadata metadata = ImageMetadataReader.readMetadata(file.getAbsoluteFile());
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
