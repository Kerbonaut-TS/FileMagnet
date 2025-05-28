import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileSelectorGUI extends JFrame {
    private JPanel extentionPanel, similarityPanel;
    private JButton selectFilesButton;
    private JCheckBox  name_box, size_box, date_box;
    private JRadioButton extension_radio, similarity_radio;
    private JButton executeButton;
    private JLabel fileCountLabel;

    public FileSelectorGUI() {

        super("File Magnet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Destination panel setup
        extentionPanel = create_extensionPanel();
        similarityPanel = create_similarityPanel();
        JPanel targetPanel = create_DirSelector("Aim magnet at:");
        JPanel destinationPanel = create_DirSelector("Attract Files to:");

        similarity_radio = new JRadioButton("by similarity");
        similarity_radio.addActionListener(e -> clickSimilarity());
        extension_radio = new JRadioButton("by extension");
        extension_radio.addActionListener(e -> clickExtension());
        ButtonGroup group = new ButtonGroup();
        group.add(extension_radio);
        group.add(similarity_radio);
        
        // Execute button
        executeButton = new JButton("Execute");
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


        JFormattedTextField type_extension = new JFormattedTextField();
        type_extension.setColumns(10);
        type_extension.setText(".JPG; .JPEG");
        criteriaPanel.add(type_extension);


        return criteriaPanel;

    }



    private JPanel create_similarityPanel(){
        JPanel similarityPanel = new JPanel();
        similarityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);


        selectFilesButton = new JButton("Select similar Files");
        selectFilesButton.addActionListener(e -> selectMultipleFiles());
        fileCountLabel = new JLabel("No files selected");

        JPanel checkboxes = new JPanel();
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



    private JPanel create_DirSelector(String description) {

        //capture current working directory
        String currentDir = System.getProperty("user.dir");

        JPanel destinationPanel = new JPanel();
        //components
        JLabel pathTextField = new JLabel();
        JTextField pathField = new JTextField(25);
        JButton browseButton = new JButton("Browse...");

        pathTextField.setText(description);
        pathField.setText(currentDir);
        browseButton.setEnabled(false);
        pathField.setEnabled(false);

        destinationPanel.add(pathTextField);
        destinationPanel.add(pathField);
        destinationPanel.add(browseButton);

        //enablePathCheckbox.addActionListener(e -> useLocalDir());

        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                pathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        return destinationPanel;
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

        fileCountLabel.setText(files.length + "files selected" );
        return files;

    }


    private void clickExtension(){
        if(extension_radio.isSelected()){
            extentionPanel.setEnabled(true);
            similarityPanel.setEnabled(false);
        }else{
            extentionPanel.setEnabled(false);
            similarityPanel.setEnabled(true);
        }
    }

    private void clickSimilarity(){
        if(similarity_radio.isSelected()){
            extentionPanel.setEnabled(false);
            similarityPanel.setEnabled(true);
        }else{
            extentionPanel.setEnabled(true);
            similarityPanel.setEnabled(false);
        }
    }

    private void executeButton(){
        JOptionPane.showMessageDialog(this, "Hello", "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileSelectorGUI::new);
    }
}
