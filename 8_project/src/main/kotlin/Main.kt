package me.kabachel

import java.io.File

fun main(args: Array<String>) {
    args.forEach { arg ->
        val input = File(arg)
        if (!input.exists()) error("Could not find path ${input.absolutePath}")

        val vmTranslator = VmTranslator()

        if (input.isDirectory) {
            val outFile = File(input, input.name + ASM_EXTENSION).apply {
                if (!exists()) createNewFile()
            }
            vmTranslator.translateDirectory(input, outFile)
        } else {
            val outFile = input.resolveSibling(input.nameWithoutExtension + ASM_EXTENSION).apply {
                if (!exists()) createNewFile()
            }
            vmTranslator.translate(input, outFile)
        }
    }
}

const val ASM_EXTENSION = ".asm"
