import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomDocumentListener implements DocumentListener {

    private final Bulker bulker;

    public CustomDocumentListener(Bulker bulker) {
        this.bulker = bulker;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        bulker.refreshOutputFileList();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        bulker.refreshOutputFileList();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        bulker.refreshOutputFileList();
    }
}
