import javax.swing.*;

public class Bulker {
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

    protected JPanel getApplicationPanel() {
        return applicationPanel;
    }

    protected JTextField getInputSearchTextField() {
        return inputSearchTextField;
    }

    protected JLabel getInputHeaderLabel() {
        return inputHeaderLabel;
    }

    protected JLabel getInputSearchLabel() {
        return inputSearchLabel;
    }

    protected JPanel getInputConfigPanel() {
        return inputConfigPanel;
    }

    protected JPanel getInputFilesPanel() {
        return inputFilesPanel;
    }

    protected JPanel getInputPanel() {
        return inputPanel;
    }

    protected JPanel getOutputPanel() {
        return outputPanel;
    }

    protected JPanel getOutputConfigPanel() {
        return outputConfigPanel;
    }

    protected JPanel getOutputFilesPanel() {
        return outputFilesPanel;
    }

    protected JList getOutputFileList() {
        return outputFileList;
    }

    protected JLabel getOutputHeaderLabel() {
        return outputHeaderLabel;
    }

    protected JLabel getOutputReplaceLabel() {
        return outputReplaceLabel;
    }

    protected JTextField getOutputReplaceTextField() {
        return outputReplaceTextField;
    }

    protected JButton getRunBulkRenameButton() {
        return runBulkRenameButton;
    }

    protected JButton getResetFileSelectionButton() {
        return resetFileSelectionButton;
    }

    protected JScrollPane getOutputListScroller() {
        return outputListScroller;
    }

    protected JScrollPane getInputScrollPane() {
        return inputScrollPane;
    }

    protected JSplitPane getBulkerSplitPane() {
        return bulkerSplitPane;
    }

    protected JPanel getControlPanel() {
        return controlPanel;
    }

    protected JDroppableList getInputFileList() {
        return inputFileList;
    }

    public Bulker() {
    }
}
