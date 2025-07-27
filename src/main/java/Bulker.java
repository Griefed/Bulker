import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;
import de.griefed.CustomCellRenderer;
import de.griefed.CustomDocumentListener;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class Bulker extends JFrame {
    private JPanel applicationPanel;
    private JTextField inputSearchTextField;
    private JLabel inputHeaderLabel;
    private JLabel inputSearchLabel;
    private JPanel inputConfigPanel;
    private JPanel inputFilesPanel;
    private JPanel inputPanel;
    private JPanel outputPanel;
    private JPanel outputConfigPanel;
    private JPanel outputFilesPanel;
    private JList outputFileList;
    private JLabel outputHeaderLabel;
    private JLabel outputReplaceLabel;
    private JTextField outputReplaceTextField;
    private JButton runBulkRenameButton;
    private JButton resetFileSelectionButton;
    private JScrollPane outputListScroller;
    private JScrollPane inputScrollPane;
    private JSplitPane bulkerSplitPane;
    private JPanel controlPanel;
    private JDroppableList inputFileList;

    public Bulker() {
        setTitle("Bulker - Bulk File Renamer");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Bulker.class.getResource("bulker-icon.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(applicationPanel);

        modifyInputList();
        addListeners();
        addButtonActions();

        setSize(800, 500);

        FlatDraculaIJTheme.setup();
        FlatJetBrainsMonoFont.install();
        FlatLaf.setPreferredMonospacedFontFamily(FlatJetBrainsMonoFont.FAMILY);
        UIManager.put("defaultFont", new FontUIResource(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 12));
        FlatDraculaIJTheme.updateUI();

        pack();
        setLocationRelativeTo(null);
        bulkerSplitPane.setDividerLocation(this.getWidth() / 2);
    }

    private void modifyInputList() {
        inputFileList.setCellRenderer(new CustomCellRenderer());
        inputFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inputFileList.setToolTipText("Drag and drop files or directories here.");
    }

    private void addListeners() {
        inputSearchTextField.getDocument().addDocumentListener(new CustomDocumentListener(this));
        outputReplaceTextField.getDocument().addDocumentListener(new CustomDocumentListener(this));
        inputFileList.getModel().addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                refreshOutputFileList();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                refreshOutputFileList();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                refreshOutputFileList();
            }
        });
    }

    private void addButtonActions() {
        runBulkRenameButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "This will rename every file which contains:\n" +
                            inputSearchTextField.getText() + "\n" +
                            "Approximately " + countPotentialRenames() + " files will be renamed.\n" +
                            "Are you ABSOLUTELY sure about this?\n" +
                            "This operation CAN NOT be undone!",
                    "Run Rename Operation?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                List<File> renamed = renameFiles();
                JOptionPane.showMessageDialog(this,
                        "The renamed files have been added to the bottom of the file-input.\n" +
                                "In case you want to further rename them.",
                        "Renamed " + renamed.size() + " file(s) successfully!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        resetFileSelectionButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "This will empty the file-list to the left, removing any entry there is.",
                    "Clear input?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                ((DefaultListModel) inputFileList.getModel()).clear();
            }
        });
    }

    private List<File> renameFiles() {
        Iterator inputs = ((DefaultListModel) inputFileList.getModel()).elements().asIterator();
        List<Hashtable> inputTables = new ArrayList<>();
        List<File> renamedFiles = new ArrayList<>();
        String searchFor = inputSearchTextField.getText();
        String replaceWith = outputReplaceTextField.getText();
        while (inputs.hasNext()) {
            Hashtable hashtable = (Hashtable) inputs.next();
            String fileName = hashtable.get("name").toString();
            if (fileName.contains(searchFor)) {
                inputTables.add(hashtable);
            }
        }
        for (Hashtable table : inputTables) {
            File oldFile = new File(table.get("path").toString());
            String parentPath = oldFile.getParentFile().getAbsolutePath();
            String fileName = oldFile.getName();
            String newFilename = fileName.replace(searchFor, replaceWith);
            File destinationFile = new File(parentPath, newFilename);
            int i = 1;
            while (destinationFile.exists()) {
                destinationFile = new File(parentPath, newFilename + " (" + i + ")");
            }
            try {
                FileUtils.moveFile(oldFile, destinationFile);
                if (destinationFile.exists()) {
                    renamedFiles.add(destinationFile);
                    ((DefaultListModel) inputFileList.getModel()).removeElement(table);
                    ((DefaultListModel) inputFileList.getModel()).addAll(inputFileList.buildHashtable(destinationFile));
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return renamedFiles;
    }

    public int countPotentialRenames() {
        DefaultListModel inputModel = (DefaultListModel) inputFileList.getModel();
        Iterator inputIterator = inputModel.elements().asIterator();
        int counter = 0;
        while (inputIterator.hasNext()) {
            Hashtable hashtable = (Hashtable) inputIterator.next();
            String oldFilename = hashtable.get("name").toString();
            if (oldFilename.contains(inputSearchTextField.getText())) {
                counter++;
            }
        }
        return counter;
    }

    public void refreshOutputFileList() {
        DefaultListModel inputModel = (DefaultListModel) inputFileList.getModel();
        DefaultListModel outputModel = (DefaultListModel) outputFileList.getModel();
        Iterator inputIterator = inputModel.elements().asIterator();
        List<String> inputFileNames = new ArrayList<>();

        while (inputIterator.hasNext()) {
            Hashtable hashtable = (Hashtable) inputIterator.next();
            String oldFilename = hashtable.get("name").toString();
            String oldFilepath = hashtable.get("path").toString();
            String newFilename = oldFilename.replace(inputSearchTextField.getText(), outputReplaceTextField.getText());
            String newFilepath = oldFilepath.substring(0, oldFilepath.length() - oldFilename.length()) + newFilename;
            inputFileNames.add(newFilename + " ==> " + newFilepath);
        }

        outputModel.clear();
        outputModel.addAll(inputFileNames);
        outputFileList.setModel(outputModel);
    }
}
