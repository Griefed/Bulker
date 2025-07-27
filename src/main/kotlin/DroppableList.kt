package de.griefed

import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.dnd.*
import java.io.File
import java.io.IOException
import java.util.*
import javax.swing.DefaultListModel
import javax.swing.JList

@Suppress("unused")
open class DroppableList : JList<Any?>(), DropTargetListener, DragSourceListener, DragGestureListener {

    var dragSource: DragSource = DragSource.getDefaultDragSource()

    init {
        dropTarget = DropTarget(this, this)
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this)
        model = DefaultListModel<Any?>()
    }

    override fun dragDropEnd(DragSourceDropEvent: DragSourceDropEvent?) {
    }

    override fun dragEnter(DragSourceDragEvent: DragSourceDragEvent?) {
    }

    override fun dragExit(DragSourceEvent: DragSourceEvent?) {
    }

    override fun dragOver(DragSourceDragEvent: DragSourceDragEvent?) {
    }

    override fun dropActionChanged(DragSourceDragEvent: DragSourceDragEvent?) {
    }

    override fun dragEnter(dropTargetDragEvent: DropTargetDragEvent) {
        dropTargetDragEvent.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE)
    }

    override fun dragExit(dropTargetEvent: DropTargetEvent?) {
    }

    override fun dragOver(dropTargetDragEvent: DropTargetDragEvent?) {
    }

    override fun dropActionChanged(dropTargetDragEvent: DropTargetDragEvent?) {
    }

    @Synchronized
    override fun drop(dropTargetDropEvent: DropTargetDropEvent) {
        try {
            val tr = dropTargetDropEvent.transferable
            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE)
                val fileList = tr.getTransferData(DataFlavor.javaFileListFlavor) as MutableList<Any>
                val iterator: MutableIterator<Any> = fileList.iterator()
                while (iterator.hasNext()) {
                    val file = iterator.next() as File
                    (model as DefaultListModel<Any>).addAll(buildHashtable(file))
                }
                dropTargetDropEvent.dropTargetContext.dropComplete(true)
            } else {
                System.err.println("Rejected")
                dropTargetDropEvent.rejectDrop()
            }
        } catch (io: IOException) {
            io.printStackTrace()
            dropTargetDropEvent.rejectDrop()
        } catch (ufe: UnsupportedFlavorException) {
            ufe.printStackTrace()
            dropTargetDropEvent.rejectDrop()
        }
    }

    fun buildHashtable(file: File): MutableList<Hashtable<Any, Any>> {
        val tables: MutableList<Hashtable<Any, Any>> = ArrayList<Hashtable<Any, Any>>()
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null && files.size > 0) {
                for (listedFile in files) {
                    tables.addAll(buildHashtable(listedFile) as Collection<Hashtable<Any, Any>>)
                }
            }
        } else {
            val hashtable: Hashtable<Any, Any> = Hashtable<Any, Any>()
            hashtable["name"] = file.name
            hashtable["path"] = file.absolutePath
            tables.add(hashtable)
        }
        return tables
    }

    override fun dragGestureRecognized(dragGestureEvent: DragGestureEvent) {
        if (selectedIndex == -1) return
        val obj = getSelectedValue()
        if (obj == null) {
            // Nothing selected, nothing to drag
            println("Nothing selected - beep")
            toolkit.beep()
        } else {
            val table = obj as Hashtable<Any, Any>
            val transferable = FileSelection(File(table["path"] as String))
            dragGestureEvent.startDrag(DragSource.DefaultCopyDrop, transferable, this)
        }
    }
}
