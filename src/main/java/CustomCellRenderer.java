import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class CustomCellRenderer implements ListCellRenderer {
    DefaultListCellRenderer listCellRenderer = new DefaultListCellRenderer();

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean hasFocus) {
        listCellRenderer.getListCellRendererComponent(list, value, index, selected, hasFocus);
        listCellRenderer.setText(getValueString(value));
        return listCellRenderer;
    }

    private String getValueString(Object value) {
        String returnString = "null";
        if (value != null) {
            if (value instanceof Hashtable) {
                Hashtable hashtable = (Hashtable) value;
                String name = (String) hashtable.get("name");
                String path = (String) hashtable.get("path");
                returnString = name + " ==> " + path;
            } else {
                returnString = "X: " + value;
            }
        }
        return returnString;
    }
}