package me.kabachel.nand2tetris

import java.io.File

fun main(args: Array<String>) {
    args.forEach { arg ->
        val asmFile = File(arg)
        if (!asmFile.exists()) error("Could not find asm file with path ${asmFile.absolutePath}")

        val outFile = asmFile.resolveSibling(asmFile.nameWithoutExtension + HACK_EXTENSION).apply {
            if (!exists()) createNewFile()
        }

        val assemblerHack = AssemblerHack()

        assemblerHack.assemble(asmFile, outFile)
    }
}

private const val HACK_EXTENSION = ".hack"