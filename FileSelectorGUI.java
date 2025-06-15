import src.Magnet;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class FileSelectorGUI extends JFrame {

    Magnet magnet;
    JTextField workingdirPath, targetPath;

    //settings field
    JRadioButton move_radio, copy_radio;
    private JCheckBox   recursiveBox, sameH, sameM, sameS, sameD;
    private JPanel name_box, extension_box, similar_content_box, same_content_box, time_box;
    private JLabel fileCountLabel, namesLabel, similarContentLabel;
    JFormattedTextField extension_field;

    //Panels


    public FileSelectorGUI() throws IOException {

        super("File Magnet v0.9");
        this.magnet = new Magnet(System.getProperty("user.dir"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 800);
        setLocationRelativeTo(null);
        //setResizable(false);
        java.net.URL url = ClassLoader.getSystemResource("rsc/icon.png");
        ImageIcon icon = new ImageIcon(url);
        setIconImage(icon.getImage());

        JPanel wdirectory_panel = create_workingdir_Selector();
        JPanel targetPanel = create_target_Panel();
        JPanel optionsPanel = this.create_optionsPanel();
        JPanel transfer_Panel= this.create_transferPanel();

        JPanel execPanel = new JPanel(new FlowLayout());
        JButton executeButton = new JButton("Go!");
        executeButton.addActionListener(e -> {try {execute();} catch (IOException ex) {throw new RuntimeException(ex);}});
        execPanel.add(executeButton);

        // Main Layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(wdirectory_panel);
        mainPanel.add(targetPanel);
        mainPanel.add(Box.createVerticalStrut(10)); // Add some space between sections
        mainPanel.add(transfer_Panel);
        mainPanel.add(Box.createVerticalStrut(10)); // Add some space between sections
        mainPanel.add(optionsPanel);
        mainPanel.add(execPanel);


        add(mainPanel);
        setVisible(true);
    }

 
    private JPanel create_workingdir_Selector() {

        workingdirPath = new JTextField(25);
        workingdirPath.setText(System.getProperty("user.dir"));

        this.fileCountLabel = new JLabel(magnet.getSampleCount() + " files will be used as reference");

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

    public JPanel create_transferPanel() {
        ButtonGroup execGroup = new ButtonGroup();
        move_radio = new JRadioButton("move files");
        copy_radio = new JRadioButton("copy files");
        execGroup.add(move_radio);
        execGroup.add(copy_radio);

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
        //create a vertical list
        JList<String> namelist = new JList<>(this.magnet.getNames(4).split(";"));
        namelist.setLayoutOrientation(JList.VERTICAL);

        JPanel timeoptions = new JPanel();
        timeoptions.setLayout(new BoxLayout(timeoptions, BoxLayout.Y_AXIS));
        sameD = new JCheckBox("same date");
        sameD.addActionListener(e -> {this.magnet.enable_check(magnet.DATE, sameD.isSelected());});
        sameH = new JCheckBox("same HH");
        sameH.addActionListener(e -> {this.magnet.enable_check(magnet.HOUR, sameH.isSelected());});
        sameM = new JCheckBox("same MM");
        sameM.addActionListener(e -> {this.magnet.enable_check(magnet.MINUTE, sameM.isSelected());});
        sameS = new JCheckBox("same SS");
        sameS.addActionListener(e -> {this.magnet.enable_check(magnet.SECOND, sameS.isSelected());});
        timeoptions.add(sameD);
        timeoptions.add(sameH);
        timeoptions.add(sameM);
        timeoptions.add(sameS);
        JLabel similarContentLabel = new JLabel("Coming soon!");
        JLabel sameContentLabel = new JLabel("Coming soon!");

        //create options
        extension_box = this.create_option("with same extensions:", extension_field, false, magnet.EXTENSION );
        name_box = this.create_option("with same names:", namelist, true, magnet.FILENAME);
        time_box = this.create_option("taken at same time:", timeoptions, false, magnet.DATE);
        same_content_box = this.create_option("with same content:", similarContentLabel,false,  magnet.SAMECONTENT);
        similar_content_box = this.create_option("with similar content:", sameContentLabel, false,  magnet.SIMILARCONTENT);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); // Align panel to the left
        //optionsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
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

    private JPanel create_option(String label , Component jcomponent, Boolean enabled, String magnetoption) {

        JCheckBox checkbox = new JCheckBox(label);
        JPanel examples = new JPanel();
        examples.setLayout(new FlowLayout());
        examples.add(jcomponent);
        examples.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel optionComponent = new JPanel();
        optionComponent.setLayout(new BoxLayout(optionComponent, BoxLayout.X_AXIS)); // Set horizontal layout
        optionComponent.setAlignmentX(Component.RIGHT_ALIGNMENT); // Align component to the left
        optionComponent.setAlignmentY(Component.CENTER_ALIGNMENT);
        optionComponent.add(checkbox);
        optionComponent.add(examples);

        checkbox.setSelected(false);
        checkbox.addActionListener(e -> {
            this.enableComponents(examples, checkbox.isSelected());
            this.magnet.enable_check(magnetoption, checkbox.isSelected());
        });

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

    private void execute() throws IOException {

        this.magnet.setWorkdir(this.workingdirPath.getText());
        this.magnet.set_trasfer_mode(move_radio.isSelected());
        this.magnet.set_recursive(recursiveBox.isSelected());
        this.magnet.setWorkdir(this.workingdirPath.getText());
        System.out.println("Settings applied: " + this.magnet.printSettings());

        //this.magnet.attractSimilar(this.targetPath.getText());

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
