package src;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Magnet extends FileComparator{
    // A magnet is a file comparator that can move or copy files and has additional settings
    File workdir;
    Boolean move;
    String ext_filter;


    public Magnet(String workingDirectory){
        this.setWorkdir(workingDirectory);
    }

    public void setWorkdir(String workingDirectory){
        this.workdir = new File(workingDirectory);

    }
    public void set_extension_filter(String ext_filter){
        this.ext_filter = ext_filter;
    }

    public void change_settings(Boolean name, Boolean extension, Boolean date, Boolean size, Boolean move) {
        this.set_trasfer_mode(move);
        super.change_settings(name, extension, date, size);
    }

    public void set_trasfer_mode(Boolean move) {
        this.move = move;
    }

    public void attract_extension(String targetDirectory) throws IOException {

        File[] target_files = new File(targetDirectory).listFiles();

        for (File f : target_files) {

            System.out.println("Processing file: " + f.getName());

            if (!f.isFile()) continue; // Skip directories
            if (f.isHidden()) continue; // Skip hidden files
            if (!f.getName().contains(".")) continue; // Skip files with no extension

            FileComparator comparator = new FileComparator();
            String ext  = comparator.separate_extension(f);
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

        File[] target_files = new File(targetDirectory).listFiles();

        for (File f : target_files) {
            if (!f.isFile()) continue; // Skip directories
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
