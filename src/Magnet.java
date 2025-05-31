package src;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Magnet {

    File workdir;
    FileComparator fileComparator;
    Boolean move; // true = move, false = copy

    public Magnet(String workingDirectory){
        File workdir = new File(workingDirectory);

    }

    public void setWorkdir(String workingDirectory){
        this.workdir = new File(workingDirectory);

    }
    public void set_similarSample(String filepath){
        fileComparator = new FileComparator(filepath);

    }
    public void change_settings(Boolean name, Boolean extension, Boolean date, Boolean size, Boolean move) {
        this.move = move;
        fileComparator.change_settings(name, extension, date, size);
    }
    public void attractSimilar(String targetDirectory) throws IOException {
        File[] files = new File(targetDirectory).listFiles();

        for (File f : files) {
            if (!f.isFile()) continue; // Skip directories
            Boolean similar = fileComparator.compare(f);
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
        Files.move(file.getAbsoluteFile().toPath(), this.workdir.toPath(), StandardCopyOption.REPLACE_EXISTING);

    }

    public void copy(File file) throws IOException {

        if (!this.workdir.exists()) this.workdir.getParentFile().mkdirs();
        Files.copy(file.getAbsoluteFile().toPath(), this.workdir.toPath(), StandardCopyOption.REPLACE_EXISTING);

    }







}
