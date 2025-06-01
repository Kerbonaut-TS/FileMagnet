package src;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Magnet extends FileComparator{
    // A magnet is a file comparator that can move or copy files and has additional settings
    File workdir;
    Boolean move = false, recursive = false;
    String ext_filter;


    public Magnet(String workingDirectory){
        this.setWorkdir(workingDirectory);
    }

    public void setWorkdir(String workingDirectory){this.workdir = new File(workingDirectory);}
    public void set_extension_filter(String ext_filter){this.ext_filter = ext_filter;}
    public void set_recursive(Boolean recursive) {this.set_recursive(recursive);}
    public void set_trasfer_mode(Boolean move) {
        this.move = move;
    }

    public void attract_extension(String targetDirectory) throws IOException {

        File[] target_dir = new File(targetDirectory).listFiles();
        File[] targetFiles = FileComparator.searchFiles(target_dir, this.recursive);

        for (File f : targetFiles) {

            System.out.println("Processing file: " + f.getName());
            String ext  = super.separate_extension(f);
            if(ext.contains(this.ext_filter)) {
                if ((this.move)) {
                    this.move(f);
                } else {
                    this.copy(f);
                }
            }
        }

    }
    public void attractSimilar(String targetDirectory) throws IOException {


        File[] target_dir = new File(targetDirectory).listFiles();
        File[] targetFiles = FileComparator.searchFiles(target_dir, this.recursive);

        for (File f : targetFiles) {
            Boolean similar = super.compare(f);
            if(similar){
                System.out.println("Attracting similar file: " + f.getName());
                if ((this.move)) {
                    this.move(f);
                } else {
                    this.copy(f);
                }
            }
        }
    }

    private void move(File file) throws IOException {

        if (!this.workdir.exists()) this.workdir.getParentFile().mkdirs();
        File destination = new File(this.workdir.toPath() + File.separator + file.getName());
        if (destination.exists()) {
            System.out.println("File already exists. Skipping: ");
        }
        else {
            Files.move(file.getAbsoluteFile().toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

    }

    private void copy(File file) throws IOException {

        if (!this.workdir.exists()) this.workdir.getParentFile().mkdirs();
        File destination = new File(this.workdir.toPath() + File.separator + file.getName());
        if (destination.exists()) {
            System.out.println("File already exists. Skipping: ");
        }
        else {
            Files.copy(file.getAbsoluteFile().toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

    }


}
