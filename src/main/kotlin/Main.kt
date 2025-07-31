package de.griefed

import javax.swing.SwingUtilities
import kotlin.system.exitProcess

/**
 * --search, singular, cannot be empty
 * --replace, singular, cannot be empty
 * --input, multiple, at least one, cannot be empty
 * --include-mask, singular, can be empty
 * --exclude-mask, singular, can be empty
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

        val search = bulker.getSearchFor(args)
        val replace = bulker.getReplaceWith(args)
        val files = bulker.getInputs(args)
        val includeMask = bulker.getIncludeMask(args)
        val excludeMask = bulker.getExcludeMask(args)
        val renamed = bulker.renameFiles(search, replace, files, includeMask, excludeMask)
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