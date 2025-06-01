package src;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Magnet extends FileComparator{
    // A magnet is a file comparator that can move or copy files and has additional settings
    File workdir;
    Boolean move;

    public Magnet(String workingDirectory){
        this.setWorkdir(workingDirectory);
    }

    public void setWorkdir(String workingDirectory){
        this.workdir = new File(workingDirectory);

    }

    public void change_settings(Boolean name, Boolean extension, Boolean date, Boolean size, Boolean move) {
        this.move = move;
        super.change_settings(name, extension, date, size);
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

    public void move(File file) throws IOException {

        if (!this.workdir.exists()) this.workdir.getParentFile().mkdirs();
        File destination = new File(this.workdir.toPath() + File.separator + file.getName());
        Files.move(file.getAbsoluteFile().toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

    }

    public void copy(File file) throws IOException {

        if (!this.workdir.exists()) this.workdir.getParentFile().mkdirs();
        File destination = new File(this.workdir.toPath() + File.separator + file.getName());
        Files.copy(file.getAbsoluteFile().toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

    }


}
