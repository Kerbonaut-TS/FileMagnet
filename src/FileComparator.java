package src;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;


public class FileComparator {


    File[]  files;
    String[] names;
    String[] extensions;
    int[]  sizes;
    String[] dates;
    Boolean[] bool_mask;


    // checks: name, extension, date, size
    Boolean[] checks = {false, false, false ,false};
    int NAME_CHECK = 0;
    int EXT_CHECK = 1;
    int DATE_CHECK = 2;
    int SIZE_CHECK = 3;


    public FileComparator(String filepath) {

        File file = new File(filepath);
        if (!file.exists()) {
            System.out.println("File does not exist: " + filepath);

            this.files = file.listFiles();
            this.bool_mask = new Boolean[this.files.length];

            for (int i = 0; i <= this.files.length; i++) {

                String filename = this.files[i].getName();
                int dotIndex = filename.lastIndexOf('.');
                this.names[i] = this.separate_name(this.files[i]);
                this.extensions[i] = this.separate_extension(this.files[i]);
            }

        }//end constructor
    }

    public void change_settings(Boolean name, Boolean extension, Boolean date, Boolean size){
        this.checks[NAME_CHECK] = name;
        this.checks[EXT_CHECK] = extension;
        this.checks[DATE_CHECK] = date;
        this.checks[SIZE_CHECK] = size;
    }

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

    public Boolean check_name(String name){
        for (String n : this.names){
            if (n.contains(name)) return true;
        }
        return false;
    }

    public Boolean check_extension(String extension){
        for (String n : this.extensions){
            if (n.contains(extension)) return true;
        }
        return false;
    }
    public Boolean compare( String filepath){
        Boolean output = true;

        //file to compare
        File file = new File(filepath);
        String name = this.separate_name(file);
        String extension = this.separate_extension(file);

        Boolean [] results = new Boolean[this.checks.length] ;
        if (checks[NAME_CHECK]) results[NAME_CHECK] = this.check_name(name);
        if (checks[EXT_CHECK])  results[EXT_CHECK] =  this.check_extension(name);

        for (int i = 0; i<=this.checks.length; i++) {
            if (this.checks[i]) {
                output = output & results[i];
            }
        }

        return output;
    }//end compare


    public boolean isImage(String ext){

        boolean is_image = ext.toLowerCase().contains(".jpg") || ext.toLowerCase().contains(".RAF") || ext.contains(".jpeg")|| ext.contains(".png");

        return is_image;
    }



    public String getExifTag (File file, String select_tag) {

        try{ Metadata metadata = ImageMetadataReader.readMetadata(file.getAbsoluteFile());
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {

                    if(tag.getTagName().equals(select_tag)) return  tag.getDescription();

                }//for each tag
            }//for each dir

        } catch (Throwable e) {e.printStackTrace();}

        return null;

    }//end method



}
