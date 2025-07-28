package de.griefed

import Bulker
import com.formdev.flatlaf.FlatLaf
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme
import org.apache.commons.io.FileUtils
import java.awt.Dimension
import java.awt.Font
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.io.File
import java.io.IOException
import java.util.*
import javax.swing.*
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener
import javax.swing.plaf.FontUIResource

class BulkerKt : Bulker() {

    private var frame: JFrame? = null
    private val docListener = CustomDocumentListener(this)

    fun initGui() {
        frame = JFrame("Bulker - Bulk File Renamer")
        frame!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame!!.iconImage = Toolkit.getDefaultToolkit().getImage(this.javaClass.getResource("/bulker-icon.png"))
        frame!!.preferredSize = Dimension(920, 590)
        frame!!.contentPane = applicationPanel

        FlatDraculaIJTheme.setup()
        FlatJetBrainsMonoFont.install()
        FlatLaf.setPreferredMonospacedFontFamily(FlatJetBrainsMonoFont.FAMILY)
        UIManager.put("defaultFont", FontUIResource(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 12))
        FlatDraculaIJTheme.updateUI()

        frame!!.pack()
        frame!!.setLocationRelativeTo(null)
        bulkerSplitPane.dividerLocation = 450

        modifyInputList()
        addListeners()
        addButtonActions()
    }

    fun makeVisible() {
        frame!!.isVisible = true
    }

    private fun modifyInputList() {
        inputFileList.cellRenderer = CustomCellRenderer()
        inputFileList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        inputFileList.toolTipText = "Drag and drop files or directories here."
    }

    private fun addListeners() {

        inputSearchTextField.document.addDocumentListener(docListener)
        outputReplaceTextField.document.addDocumentListener(docListener)
        inputFileList.model.addListDataListener(object : ListDataListener {
            override fun intervalAdded(e: ListDataEvent?) {
                refreshOutputFileList()
            }

            override fun intervalRemoved(e: ListDataEvent?) {
                refreshOutputFileList()
            }

            override fun contentsChanged(e: ListDataEvent?) {
                refreshOutputFileList()
            }
        })
    }

    private fun addButtonActions() {
        runBulkRenameButton.addActionListener { e: ActionEvent? ->
            val result = JOptionPane.showConfirmDialog(
                frame,
                """
                    This will rename every file which contains:
                    ${inputSearchTextField.text}
                    Approximately ${countPotentialRenames()} files will be renamed.
                    Are you ABSOLUTELY sure about this?
                    This operation CAN NOT be undone!
                    """.trimIndent(),
                "Run Rename Operation?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            )
            if (result == JOptionPane.YES_OPTION) {
                val renamed = renameFiles(inputSearchTextField.text, outputReplaceTextField.text, getFileList())
                JOptionPane.showMessageDialog(
                    frame,
                    """
                        The renamed files have been added to the bottom of the file-input.
                        In case you want to further rename them.
                        """.trimIndent(),
                    "Renamed " + renamed.size + " file(s) successfully!",
                    JOptionPane.INFORMATION_MESSAGE
                )
            }
        }
        resetFileSelectionButton.addActionListener { e: ActionEvent? ->
            val result = JOptionPane.showConfirmDialog(
                frame,
                "This will empty the file-list to the left, removing any entry there is.",
                "Clear input?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            )
            if (result == JOptionPane.YES_OPTION) {
                (inputFileList.model as DefaultListModel<Any>).clear()
            }
        }
    }

    fun renameFiles(searchFor: String, replaceWith: String, files: List<File>): MutableList<File> {
        val renamedFiles = ArrayList<File>()
        val parsedFiles = files.filter { file -> file.name.contains(searchFor) }
        for (file in parsedFiles) {
            val oldFile = file.absoluteFile
            val parentPath = file.parentFile.absolutePath
            val fileName = oldFile.name
            val newFilename = fileName.replace(searchFor, replaceWith)
            var destinationFile = File(parentPath, newFilename)
            val i = 1
            while (destinationFile.exists()) {
                destinationFile = File(parentPath, "$newFilename ($i)")
            }
            try {
                FileUtils.moveFile(oldFile, destinationFile)
                if (destinationFile.exists()) {
                    renamedFiles.add(destinationFile)
                    if (frame?.isVisible ?: false) {
                        (inputFileList.model as DefaultListModel<Any>).removeElement(
                            hashMapOf<Any, Any>(
                                "name" to oldFile.name,
                                "path" to oldFile.absolutePath
                            )
                        )
                        (inputFileList.model as DefaultListModel<Any>).addAll(
                            inputFileList.buildHashtable(
                                destinationFile
                            )
                        )
                    }
                }
            } catch (ex: IOException) {
                throw RuntimeException(ex)
            }
        }
        return renamedFiles
    }

    private fun getFileList(): List<File> {
        val inputs = (inputFileList.model as DefaultListModel<Any>).elements().asIterator()
        val files = ArrayList<File>()
        while (inputs.hasNext()) {
            val hashtable = inputs.next() as Hashtable<Any, Any>
            files.add(File(hashtable["path"].toString()))
        }
        return files
    }

    fun countPotentialRenames(): Int {
        val inputModel = inputFileList.model as DefaultListModel<Any>
        val inputIterator: MutableIterator<Any> = inputModel.elements().asIterator()
        var counter = 0
        while (inputIterator.hasNext()) {
            val hashtable = inputIterator.next() as Hashtable<Any, Any>
            val oldFilename = hashtable["name"].toString()
            if (oldFilename.contains(inputSearchTextField.text)) {
                counter++
            }
        }
        return counter
    }

    fun refreshOutputFileList() {
        val inputModel = inputFileList.model as DefaultListModel<Any>
        val outputModel = outputFileList.model as DefaultListModel<Any>
        val inputIterator = inputModel.elements().asIterator()
        val inputFileNames = mutableListOf<String>()

        while (inputIterator.hasNext()) {
            val hashtable = inputIterator.next() as Hashtable<Any, Any>
            val oldFilename = hashtable["name"].toString()
            val oldFilepath = hashtable["path"].toString()
            val newFilename = oldFilename.replace(inputSearchTextField.text, outputReplaceTextField.text)
            val newFilepath = oldFilepath.substring(0, oldFilepath.length - oldFilename.length) + newFilename
            inputFileNames.add("$newFilename ==> $newFilepath")
        }

        outputModel.clear()
        outputModel.addAll(inputFileNames)
        outputFileList.setModel(outputModel)
    }
}