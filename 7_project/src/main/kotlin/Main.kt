package me.kabachel

import java.io.File

fun main(args: Array<String>) {
    args.forEach { arg ->
        val vmFile = File(arg)
        if (!vmFile.exists()) error("Could not find vm file with path ${vmFile.absolutePath}")

        val outFile = vmFile.resolveSibling(vmFile.nameWithoutExtension + ASM_EXTENSION).apply {
            if (!exists()) createNewFile()
        }

        val vmTranslator = VmTranslator()

        vmTranslator.translate(vmFile, outFile)
    }
}

private const val ASM_EXTENSION = ".asm"