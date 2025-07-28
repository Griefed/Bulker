package de.griefed

import java.io.File
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

/**
 * --search, singular
 * --replace, singular
 * --input, multiple
 */
fun main(args: Array<String>) {
    val bulker = BulkerKt()

    if (args.isNotEmpty()) {
        if (!args.contains("--search") || !args.contains("--replace") || !args.contains("--input")) {
            println("Missing vital arguments. You must specify:")
            println("--search \"Your search sequence\"")
            println("--replace \"What you want your search sequence to be replaced with\"")
            println("--input \"/path/to/file/or/folder\"")
            println("You can specify multiple --input-arguments, but only one --search or --replace")
            exitProcess(1)
        }

        val search = getSearchFor(args)
        val replace = getReplaceWith(args)
        val files = getInputs(args)
        val renamed = bulker.renameFiles(search, replace, files)
        println("Renamed the following files:")
        renamed.forEach {
            println(it)
        }

    } else {
        SwingUtilities.invokeLater {
            bulker.initGui()
            bulker.makeVisible()
        }
    }
}

fun getSearchFor(args: Array<String>): String {
    val searchPos = args.indexOf("--search")
    return args[searchPos + 1]
}

fun getReplaceWith(args: Array<String>): String {
    val searchPos = args.indexOf("--replace")
    return args[searchPos + 1]
}

fun getInputs(args: Array<String>): List<File> {
    val files = mutableListOf<File>()
    val inputPositions = mutableListOf<Int>()
    for (i in args.indices) {
        if (args[i] == "--input") {
            inputPositions.add(i + 1)
        }
    }
    for (i in inputPositions) {
        val file = File(args[i])
        if (file.isFile) {
            files.add(file)
        }
        if (file.isDirectory) {
            file.walk(FileWalkDirection.TOP_DOWN).forEach { walked ->
                if (walked.isFile) {
                    files.add(walked)
                }
            }
        }
    }
    return files
}