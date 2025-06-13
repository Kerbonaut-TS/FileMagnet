import src.Magnet;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class FileSelectorGUI extends JFrame {

    Magnet magnet;
    File[] sample;
    private JPanel optionsPanel;

    //settings field
    JFormattedTextField extension_field;
    private JCheckBox   recursiveBox, sameH, sameM, sameS;
    private JPanel name_box, extension_box, similar_content_box, same_content_box, time_box;
    private JLabel fileCountLabel, namesLabel, similarContentLabel;
    JTextField workingdirPath, targetPath, samplePath;
    JRadioButton move_radio, copy_radio;

    //Panels


    public FileSelectorGUI() throws IOException {

        super("File Magnet v0.9");
        this.magnet = new Magnet(System.getProperty("user.dir"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        //setResizable(false);
        java.net.URL url = ClassLoader.getSystemResource("rsc/icon.png");
        ImageIcon icon = new ImageIcon(url);
        setIconImage(icon.getImage());

        JPanel wdirectory_panel = create_workingdir_Selector();
        JPanel targetPanel = create_target_Panel();
        JPanel optionsPanel = this.create_optionsPanel();
        JPanel transfer_Panel= this.create_transferOptionsPanel();

        JPanel execPanel = create_execPanel();


        // Main Layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); // Align panel to the left

        mainPanel.add(wdirectory_panel);
        mainPanel.add(targetPanel);
        mainPanel.add(transfer_Panel);
        mainPanel.add(optionsPanel);
        mainPanel.add(execPanel);


        add(mainPanel);
        setVisible(true);
    }

    private JPanel create_workingdir_Selector() {

        workingdirPath = new JTextField(25);
        workingdirPath.setText(System.getProperty("user.dir"));
        this.fileCountLabel = new JLabel("0 files detected");

        //layout
        JPanel panel = new JPanel(new FlowLayout());
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT); // Align panel to the left
        panel.add(workingdirPath);
        panel.add(fileCountLabel);
        panel.setBorder(BorderFactory.createTitledBorder("Working Directory:"));


        return panel;
    }



    private JPanel create_target_Panel() {

        targetPath = new JTextField(25);
        targetPath.setText("");
        this.recursiveBox = new JCheckBox("Search subdirectories");
        JButton browseButton = new JButton("Change Target");

        JPanel panel = new JPanel(new FlowLayout());
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT); // Align panel to the left
        panel.add(targetPath);
        panel.add(browseButton);
        panel.add(recursiveBox);
        panel.setBorder(BorderFactory.createTitledBorder("Search files in:"));

        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setCurrentDirectory(new File(workingdirPath.getText()));
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                targetPath.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        return panel;
    }

    public JPanel create_transferOptionsPanel() {
        move_radio = new JRadioButton("move files");
        copy_radio = new JRadioButton("copy files");

        copy_radio.setSelected(true);
        JPanel panel = new JPanel(new FlowLayout());
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT); // Align panel to the left
        panel.add(move_radio);
        panel.add(copy_radio);
        return panel;
    }

    private JPanel create_optionsPanel(){


        //init components in this section
        extension_field = new JFormattedTextField();
        extension_field.setColumns(10);
        extension_field.setText(".JPG; .JPEG");
        namesLabel = new JLabel(this.magnet.getNames());
        JPanel timeoptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sameH = new JCheckBox("same HH");
        sameM = new JCheckBox("same MM");
        sameS = new JCheckBox("same SS");
        timeoptions.add(sameH);
        timeoptions.add(sameM);
        timeoptions.add(sameS);
        similarContentLabel = new JLabel("Coming soon!");

        //create options
        extension_box = this.create_option("same extensions:", extension_field, false);
        name_box = this.create_option("same names:", namesLabel, true);
        time_box = this.create_option("taken at same time:", timeoptions, false);
        same_content_box = this.create_option("same content:", similarContentLabel,false);
        similar_content_box = this.create_option("similar content:", similarContentLabel, false);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); // Align panel to the left
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
        optionsPanel.add(extension_box);
        optionsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        optionsPanel.add(name_box);
        optionsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        optionsPanel.add(time_box);
        optionsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        optionsPanel.add(same_content_box);
        optionsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        optionsPanel.add(similar_content_box);

        return optionsPanel;

    }

    private JPanel create_option(String label , Component jcomponent, Boolean enabled) {

        JCheckBox checkbox = new JCheckBox(label);
        JPanel examples = new JPanel();
        examples.setLayout(new FlowLayout());
        examples.add(jcomponent);
        examples.setAlignmentY(Component.TOP_ALIGNMENT);

        checkbox.setSelected(false);
        checkbox.addActionListener(e -> {
            this.enableComponents(examples, checkbox.isSelected());
        });


        JPanel optionComponent = new JPanel();
        optionComponent.setLayout(new BoxLayout(optionComponent, BoxLayout.X_AXIS)); // Set horizontal layout
        optionComponent.setAlignmentX(Component.RIGHT_ALIGNMENT); // Align component to the left
        optionComponent.setAlignmentY(Component.CENTER_ALIGNMENT);
        optionComponent.add(checkbox);
        optionComponent.add(examples);

        this.enableComponents(examples, enabled);
        checkbox.setSelected(enabled);

        return optionComponent;
    }

    private void enableComponents(JPanel panel, boolean enabled) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JPanel) {
                enableComponents((JPanel) component, enabled);
            }
            component.setEnabled(enabled);
        }
    }
    private JPanel create_execPanel(){

        ButtonGroup execGroup = new ButtonGroup();

        JButton executeButton = new JButton("Go!");
        executeButton.addActionListener(e -> {try {executeButton();} catch (IOException ex) {throw new RuntimeException(ex);}});
        executeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        executeButton.setVerticalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Set vertical layout
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT); // Align panel to the left
        panel.add(executeButton);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return panel;

    }

    private File[] select_sample() throws IOException {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setCurrentDirectory(new File(workingdirPath.getText()));

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.sample = fileChooser.getSelectedFiles();
        }

        if(this.sample.length == 1){
            samplePath.setText(this.sample[0].getAbsolutePath());
            fileCountLabel.setText(this.sample.length + "selected" );

        } else if( this.sample.length > 1){
            samplePath.setText(this.sample[0].getParent());
            fileCountLabel.setText(this.sample.length + "selected" );

        } else{
            samplePath.setText("");
        }
        this.magnet.set_reference_sample(this.sample);
        return this.sample;
    }



    private void clickSimilarity() {
        extension_field.setEnabled(false);
        this.samplePath.setText(this.workingdirPath.getText());
        File sample_dir = new File(this.samplePath.getText());
        fileCountLabel.setText(sample_dir.listFiles() == null ? "0 files selected" : sample_dir.listFiles().length + " files selected");
    }


    private void executeButton() throws IOException {

        this.magnet.setWorkdir(this.workingdirPath.getText());
        this.magnet.set_trasfer_mode(move_radio.isSelected());
        this.magnet.set_recursive(recursiveBox.isSelected());


        if (this.sample  == null && !samplePath.getText().isEmpty()) {
            File sample_dir = new File(samplePath.getText());
            this.sample = sample_dir.listFiles();
        }
        this.magnet.set_reference_sample(this.sample);
        this.magnet.change_settings(name_box.isEnabled(), same_content_box.isEnabled(), time_box.isEnabled(), false);
        this.magnet.attractSimilar(this.targetPath.getText());

        JOptionPane.showMessageDialog(this, this.magnet.getOutcome(), "Completed!", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                new FileSelectorGUI();
            } catch (IOException e) {
                e.printStackTrace(); // Log the exception or handle it appropriately
                JOptionPane.showMessageDialog(null, "Failed to initialize the GUI: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });    }
}
