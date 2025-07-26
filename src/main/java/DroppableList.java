import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class DroppableList extends JList implements DropTargetListener, DragSourceListener, DragGestureListener {
    DropTarget dropTarget = new DropTarget(this, this);
    DragSource dragSource = DragSource.getDefaultDragSource();

    public DroppableList() {
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        setModel(new DefaultListModel());
    }

    public void dragDropEnd(DragSourceDropEvent DragSourceDropEvent) {
    }

    public void dragEnter(DragSourceDragEvent DragSourceDragEvent) {
    }

    public void dragExit(DragSourceEvent DragSourceEvent) {
    }

    public void dragOver(DragSourceDragEvent DragSourceDragEvent) {
    }

    public void dropActionChanged(DragSourceDragEvent DragSourceDragEvent) {
    }

    public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
        dropTargetDragEvent.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
    }

    public void dragExit(DropTargetEvent dropTargetEvent) {
    }

    public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
    }

    public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
    }

    public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
        try {
            Transferable tr = dropTargetDropEvent.getTransferable();
            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                java.util.List fileList = (java.util.List) tr.getTransferData(DataFlavor.javaFileListFlavor);
                Iterator iterator = fileList.iterator();
                while (iterator.hasNext()) {
                    File file = (File) iterator.next();
                    ((DefaultListModel) getModel()).addAll(buildHashtable(file));
                }
                dropTargetDropEvent.getDropTargetContext().dropComplete(true);
            } else {
                System.err.println("Rejected");
                dropTargetDropEvent.rejectDrop();
            }
        } catch (IOException io) {
            io.printStackTrace();
            dropTargetDropEvent.rejectDrop();
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
            dropTargetDropEvent.rejectDrop();
        }
    }

    public List<Hashtable> buildHashtable(File file) {
        List<Hashtable> tables = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File listedFile : files) {
                    tables.addAll(buildHashtable(listedFile));
                }
            }
        } else {
            Hashtable hashtable = new Hashtable();
            hashtable.put("name", file.getName());
            hashtable.put("path", file.getAbsolutePath());
            tables.add(hashtable);
        }
        return tables;
    }

    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
        if (getSelectedIndex() == -1) return;
        Object obj = getSelectedValue();
        if (obj == null) {
            // Nothing selected, nothing to drag
            System.out.println("Nothing selected - beep");
            getToolkit().beep();
        } else {
            Hashtable table = (Hashtable) obj;
            FileSelection transferable = new FileSelection(new File((String) table.get("path")));
            dragGestureEvent.startDrag(DragSource.DefaultCopyDrop, transferable, this);
        }
    }
}
