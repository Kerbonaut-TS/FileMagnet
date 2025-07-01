package src;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Magnet extends FileComparator{
    // A magnet is a FileComparator that in addition can move or copy files
    File workdir;
    Boolean move = false, recursive = false;

    static int MODE_COPY = 0;
    static int MODE_MOVE = 1;
    int count_analyzed = 0;
    int count_ignored = 0;
    int count_transferred = 0;

    public Magnet(String workingDirectory) throws IOException {
        this.setWorkdir(workingDirectory);
    }

    public void setWorkdir(String workingDirectory) throws IOException {
        //The working directory is the directory where files will be copied or moved to.
        // the content of this directory is used as a reference sample for the files that will be transferred
        this.workdir = new File(workingDirectory);
        this.set_reference_sample(this.workdir.listFiles());
    }
    public void set_recursive(Boolean recursive) {this.recursive = recursive;}
    public void set_trasfer_mode(Boolean move) {
        this.move = move;
    }

    private void init_counters () {
        this.count_analyzed = 0;
        this.count_ignored = 0;
        this.count_transferred = 0;
    }


    public void attract(String targetDirectory) throws IOException {
        this.init_counters();
        File[] complete_filelist = new File(targetDirectory).listFiles();
        File[] targetFiles = FileComparator.searchFiles(complete_filelist, this.recursive);

        for (File f : targetFiles) {
            Boolean similar = super.compare(f);
            if(similar){
                Boolean trasferred = this.transfer(f, this.move ? Magnet.MODE_MOVE : Magnet.MODE_COPY);
                if(trasferred) this.count_transferred++;
            }
        }
        this.count_analyzed = targetFiles.length;

    }

    private Boolean transfer(File file, int transfer_mode) throws IOException {

        if (!this.workdir.exists()) this.workdir.getParentFile().mkdirs();

        File destination = new File(this.workdir.toPath() + File.separator + file.getName());
        if (destination.exists()) {
            System.out.println("File already exists. Skipping... ");
            return false;
        }
        else {
            if (transfer_mode == Magnet.MODE_MOVE){
                Files.move(file.getAbsoluteFile().toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else if (transfer_mode == Magnet.MODE_COPY) {
                Files.copy(file.getAbsoluteFile().toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        }
    }

     public String getOutcome(){
        String outcome = "Processed " + this.count_analyzed + "\n";
        outcome += "Transferred " + this.count_transferred + " files\n";
        return outcome;
     }

    public String printSettings() {
        StringBuilder settings = new StringBuilder();
        settings.append("Workdir: " + this.workdir.getAbsolutePath() + "\n");
        settings.append("Transfer mode: " + (this.move ? "Move" : "Copy") + "\n");
        settings.append("Recursive: " + this.recursive + "\n");
        settings.append("CHECKS ===============================\n");
        settings.append("filename check: " + this.check_enabled.get(FILENAME) + "\n");
        settings.append("Extension check: " + this.check_enabled.get(EXTENSION) + "\n");
        settings.append("Date check: " + this.check_enabled.get(DATE) + "\n");
        settings.append("Hour check: " + this.check_enabled.get(HOUR) + "\n");
        settings.append("Minute check: " + this.check_enabled.get(MINUTE) + "\n");
        settings.append("Second check: " + this.check_enabled.get(SECOND) + "\n");

        return settings.toString();
    }


}
