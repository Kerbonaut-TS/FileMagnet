package src;
import java.io.File;
import java.io.IOException;

public class Utilities {

    public static String read_config_file() {
        //read hidden config file called .magnet_config and return the path
        File configFile = new File(System.getProperty("user.dir"), ".magnet_config");
        if (configFile.exists()) {
            try {
                String content = new String(java.nio.file.Files.readAllBytes(configFile.toPath()));
                return content.trim(); // Return the path without leading/trailing whitespace
            } catch (IOException e) {
                System.out.println("Failed to read configuration: " + e.getMessage() + "Error");
            }
        }
        return ""; // Return empty string if file does not exist or an error occurs
    }

    public static void update_config_file(String workingDirectory, String targetpath) {
        //create hidden config file called .magnet_config and save selected path in it
        String filepath = workingDirectory + File.separator + "magnet_config.txt";
        File configFile = new File(workingDirectory, ".magnet_config");
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            // Write the selected path to the config file
            java.nio.file.Files.write(configFile.toPath(), targetpath.getBytes());
        } catch (IOException ex) {
            System.out.println("Failed to save configuration: " + ex.getMessage() + "Error");
        }
    }

    public static void write_log(String logPath, String message) throws IOException {
        File logFile = new File(logPath, "magnet.log");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        java.nio.file.Files.write(logFile.toPath(), (message + System.lineSeparator()).getBytes(), java.nio.file.StandardOpenOption.APPEND);
    }

    public static String printSettings(Magnet magnet) {
        StringBuilder settings = new StringBuilder();
        settings.append("Workdir: " + magnet.workdir.getAbsolutePath() + "\n");
        settings.append("Transfer mode: " + (magnet.move ? "Move" : "Copy") + "\n");
        settings.append("Recursive: " + magnet.recursive + "\n");
        settings.append("CHECKS ===============================\n");
        settings.append("filename check: " + magnet.check_enabled.get(magnet.FILENAME) + "\n");
        settings.append("Extension check: " + magnet.check_enabled.get(magnet.EXTENSION) + "\n");
        settings.append("Date check: " + magnet.check_enabled.get(magnet.DATE) + "\n");
        settings.append("Hour check: " + magnet.check_enabled.get(magnet.HOUR) + "\n");
        settings.append("Minute check: " + magnet.check_enabled.get(magnet.MINUTE) + "\n");
        settings.append("Second check: " + magnet.check_enabled.get(magnet.SECOND) + "\n");

        return settings.toString();
    }
}
