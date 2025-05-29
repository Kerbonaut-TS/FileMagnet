import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileSelectorGUI extends JFrame {
    //settings field
    private JRadioButton extension_radio, similarity_radio;
    JFormattedTextField extension_field;
    private JCheckBox  name_box, size_box, date_box;
    private JLabel fileCountLabel;
    JTextField destinationPath, targetPath;

    //Panels
    private JPanel similarityPanel, extentionPanel, checkboxes;

    public FileSelectorGUI() {

        super("File Magnet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Destination panel setup
        extentionPanel = create_extensionPanel();
        similarityPanel = create_similarityPanel();
        JPanel destinationPanel = create_target_Selector();
        JPanel targetPanel = create_destination_Selector();


        similarity_radio = new JRadioButton("by similarity with...");
        similarity_radio.addActionListener(e -> clickSimilarity());
        extension_radio = new JRadioButton("by extension");
        extension_radio.addActionListener(e -> clickExtension());
        ButtonGroup group = new ButtonGroup();
        group.add(extension_radio);
        group.add(similarity_radio);
        extension_radio.setSelected(true);
        clickExtension();

        // Execute button
        JButton executeButton = new JButton("Attraction");
        executeButton.addActionListener(e -> executeButton());

        // Layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(new JLabel("Attract files:"));
        mainPanel.add(extension_radio);
        mainPanel.add(extentionPanel);
        mainPanel.add(similarity_radio);
        mainPanel.add(similarityPanel);
        mainPanel.add(targetPanel);
        mainPanel.add(destinationPanel);
        mainPanel.add(executeButton);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel create_extensionPanel(){
        JPanel criteriaPanel = new JPanel();
        criteriaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

         extension_field = new JFormattedTextField();
        extension_field.setColumns(10);
        extension_field.setText(".JPG; .JPEG");
        criteriaPanel.add(extension_field);


        return criteriaPanel;

    }



    private JPanel create_similarityPanel(){
        JPanel similarityPanel = new JPanel();
        similarityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JButton selectFilesButton = new JButton("Select similar Files");
        selectFilesButton.addActionListener(e -> selectMultipleFiles());
        fileCountLabel = new JLabel("No files selected");

        checkboxes  = new JPanel();
        checkboxes.setLayout(new BoxLayout(checkboxes, BoxLayout.Y_AXIS));
        name_box = new JCheckBox("same name");
        size_box = new JCheckBox("same size");
        date_box = new JCheckBox("same date");
        checkboxes.add(name_box);
        checkboxes.add(size_box);
        checkboxes.add(date_box);

        similarityPanel.add(selectFilesButton);
        similarityPanel.add(fileCountLabel);
        similarityPanel.add(checkboxes);

        return similarityPanel;

    }



    private JPanel create_destination_Selector() {

        JPanel destinationPanel = new JPanel();
        destinationPath = new JTextField(25);
        JButton browseButton = new JButton("Change Directory");

        String currentDir = System.getProperty("user.dir");
        destinationPath.setText(currentDir);

        destinationPanel.add(destinationPath);
        destinationPanel.add(browseButton);
        destinationPanel.setBorder(BorderFactory.createTitledBorder("Aim magnet at"));

        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                destinationPath.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        return destinationPanel;
    }

    private JPanel create_target_Selector() {

        targetPath = new JTextField(25);
        targetPath.setText("");
        JButton browseButton = new JButton("Change Directory");

        JPanel targetPanel = new JPanel();
        targetPanel.add(targetPath);
        targetPanel.add(browseButton);
        targetPanel.setBorder(BorderFactory.createTitledBorder("Attract files to"));


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


    private File[] selectMultipleFiles(){
        StringBuilder filenames = new StringBuilder("Selected files:\n");
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setMultiSelectionEnabled(true);
        int returnVal = fileChooser.showOpenDialog(this);
        File[] files = new File[0];
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            files = fileChooser.getSelectedFiles();

        }

        fileCountLabel.setText(files.length + "selected" );
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

    private void executeButton(){
        String command = "";
        String criteria = "";

        if (extension_radio.isSelected()){
            String extensions = extension_field.getText();
            command = "Attract by extenion:" + extensions;}
        else if (similarity_radio.isSelected()) {
            if (name_box.isSelected()) criteria += "Name ";
            if (size_box.isSelected()) criteria += "Size ";
            if (date_box.isSelected()) criteria += "Date ";
            command = "Similarity with criteria: " + criteria;
        }

        JOptionPane.showMessageDialog(this, command, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileSelectorGUI::new);
    }
}
