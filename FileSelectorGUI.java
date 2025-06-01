import src.Magnet;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class FileSelectorGUI extends JFrame {

    Magnet magnet;

    //settings field
    private JRadioButton extension_radio, similarity_radio;
    JFormattedTextField extension_field;
    private JCheckBox  name_box, size_box, date_box;
    private JLabel fileCountLabel;
    JTextField destinationPath, targetPath;
    File[] sample;
    JRadioButton move_radio, copy_radio;

    //Panels
    private JPanel similarityPanel, extentionPanel, checkboxes;

    public FileSelectorGUI() {

        super("File Magnet");
        this.magnet = new Magnet(System.getProperty("user.dir"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        java.net.URL url = ClassLoader.getSystemResource("rsc/icon.png");
        ImageIcon icon = new ImageIcon(url);
        setIconImage(icon.getImage());

        // Destination panel setup
        extension_radio = new JRadioButton("by extension");
        extension_radio.addActionListener(e -> clickExtension());
        extension_radio.setSelected(true);
        extentionPanel = create_extensionPanel();

        similarity_radio = new JRadioButton("by similarity with...");
        similarity_radio.addActionListener(e -> clickSimilarity());
        similarityPanel = create_similarityPanel();

        ButtonGroup group = new ButtonGroup();
        group.add(extension_radio);
        group.add(similarity_radio);
        clickExtension();


        JPanel destinationPanel = create_destination_Selector();
        JPanel targetPanel = create_target_Selector();

        // Execute Panel
        JPanel execPanel = create_execPanel();


        // Main Layout
        JPanel mainPanel = new JPanel();
        // Set vertical layout and center alignment
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //mainPanel.add(new JLabel("Attract files:"));
        mainPanel.add(extension_radio);
        mainPanel.add(extentionPanel);
        mainPanel.add(similarity_radio);
        mainPanel.add(similarityPanel);
        mainPanel.add(targetPanel);
        mainPanel.add(destinationPanel);
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainPanel.add(execPanel);
        mainPanel.setBorder(BorderFactory.createTitledBorder("Attraction rules"));

        add(mainPanel);
        setVisible(true);
    }

    private JPanel create_extensionPanel(){
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        extension_field = new JFormattedTextField();
        extension_field.setColumns(10);
        extension_field.setText(".JPG; .JPEG");
        panel.add(extension_field);
        return panel;

    }



    private JPanel create_similarityPanel(){

        JButton selectFilesButton = new JButton("Select similar Files");
        selectFilesButton.addActionListener(e -> select_sample());
        fileCountLabel = new JLabel("No files selected");
        checkboxes  = new JPanel();
        checkboxes.setLayout(new BoxLayout(checkboxes, BoxLayout.Y_AXIS));
        name_box = new JCheckBox("same name");
        size_box = new JCheckBox("same size");
        date_box = new JCheckBox("same date");
        checkboxes.add(name_box);
        checkboxes.add(size_box);
        checkboxes.add(date_box);
        //layout
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(selectFilesButton);
        panel.add(fileCountLabel);
        panel.add(checkboxes);
        return panel;

    }



    private JPanel create_target_Selector() {

        targetPath = new JTextField(25);
        targetPath.setText("");
        JButton browseButton = new JButton("Change Directory");

        JPanel targetPanel = new JPanel();
        targetPanel.add(targetPath);
        targetPanel.add(browseButton);
        targetPanel.setBorder(BorderFactory.createTitledBorder("Aim magnet at"));

        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                targetPath.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        return targetPanel;
    }



    private JPanel create_destination_Selector() {

        destinationPath = new JTextField(25);
        destinationPath.setText(System.getProperty("user.dir"));
        JButton browseButton = new JButton("Change Directory");
        //layout
        JPanel destinationPanel = new JPanel();
        destinationPanel.add(destinationPath);
        destinationPanel.add(browseButton);
        destinationPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        destinationPanel.setBorder(BorderFactory.createTitledBorder("Collect files in"));


        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                destinationPath.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        // Add action listener to update the magnet's workdir
        return destinationPanel;
    }


    private JPanel create_execPanel(){

        ButtonGroup execGroup = new ButtonGroup();
        move_radio = new JRadioButton("move files");
        copy_radio = new JRadioButton("copy files");
        execGroup.add(move_radio);
        execGroup.add(copy_radio);
        copy_radio.setSelected(true);
        JPanel radioPanel = new JPanel();
        radioPanel.add(move_radio);
        radioPanel.add(copy_radio);
        JButton executeButton = new JButton("Go!");
        executeButton.addActionListener(e -> {try {executeButton();} catch (IOException ex) {throw new RuntimeException(ex);}});
        executeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Set vertical layout
        panel.add(radioPanel);
        panel.add(executeButton);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return panel;

    }

    private File[] select_sample(){

        File[] files = new File[0];
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            files = fileChooser.getSelectedFiles();
        }

        fileCountLabel.setText(files.length + "selected" );
        this.magnet.set_reference_sample(files);
        return files;
    }


    private void clickExtension(){
        toggleSimilarity(false);
        extension_field.setEnabled(true);

    }

    private void clickSimilarity() {
        toggleSimilarity(true);
        extension_field.setEnabled(false);
    }

    private void toggleSimilarity(Boolean on) {
        for (Component component : similarityPanel.getComponents()) {
            component.setEnabled(on);
        }
        for (Component component : checkboxes.getComponents()) {
            component.setEnabled(on);
        }
    }

    private void executeButton() throws IOException {
        String command = "";
        String criteria = "";

        if (extension_radio.isSelected()){
            String extensions = extension_field.getText();
            command = "Attract by extension:" + extensions;}
        else if (similarity_radio.isSelected()) {
            if (name_box.isSelected()) criteria += "Name ";
            if (size_box.isSelected()) criteria += "Size ";
            if (date_box.isSelected()) criteria += "Date ";
            command = "Similarity with criteria: " + criteria;
        }
        this.magnet.setWorkdir(this.destinationPath.getText());
        this.magnet.change_settings(name_box.isSelected(), size_box.isSelected(), date_box.isSelected(), false, move_radio.isSelected());
        this.magnet.attractSimilar(this.targetPath.getText());
        JOptionPane.showMessageDialog(this, command, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileSelectorGUI::new);
    }
}
