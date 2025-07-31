package de.griefed

import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.File
import java.io.IOException
import java.io.StringReader
import java.util.*

class FileSelection(file: File?) : Vector<Any?>(), Transferable {
    var flavors: Array<DataFlavor> =
        arrayOf(DataFlavor.javaFileListFlavor, DataFlavor.stringFlavor, DataFlavor.plainTextFlavor)

    init {
        addElement(file)
    }

    /* Returns the array of flavors in which it can provide the data. */
    @Synchronized
    override fun getTransferDataFlavors(): Array<DataFlavor> {
        return flavors
    }

    /* Returns whether the requested flavor is supported by this object. */
    override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
        var b = false
        b = b or flavor.equals(flavors[FILE])
        b = b or flavor.equals(flavors[STRING])
        b = b or flavor.equals(flavors[PLAIN])
        return (b)
    }

    /**
     * If the data was requested in the "java.lang.String" flavor,
     * return the String representing the selection.
     */
    @Synchronized
    @Throws(UnsupportedFlavorException::class, IOException::class)
    override fun getTransferData(flavor: DataFlavor): Any {
        return if (flavor.equals(flavors[FILE])) {
            this
        } else if (flavor.equals(flavors[PLAIN])) {
            StringReader((elementAt(0) as File).absolutePath)
        } else if (flavor.equals(flavors[STRING])) {
            (elementAt(0) as File).absolutePath
        } else {
            throw UnsupportedFlavorException(flavor)
        }
    }

    companion object {
        const val FILE: Int = 0
        const val STRING: Int = 1
        const val PLAIN: Int = 2
    }
}