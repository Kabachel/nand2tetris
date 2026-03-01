package me.kabachel.nand2tetris

import java.io.File
import java.util.logging.Logger

class AssemblerHack {

    private val logger = Logger.getLogger(this::class.java.name)

    fun assemble(input: File, output: File) {
        logger.info("advance(): called for file='${input.path}'")

        val sb = StringBuilder()
        val symbolTable = SymbolTable()
        val code = Code()
        var currentAddress: Short = START_USER_ADDRESS
        var commandCounter: Short = 0

        // first passage
        with(Parser(input)) {
            while (true) {
                advance()
                if (!hasMoreLines()) break

                when (instructionType()) {
                    InstructionType.L_INSTRUCTION -> {
                        val symbol = symbol()
                        when {
                            symbolTable.contains(symbol) -> symbolTable.getAddress(symbol).to16BinaryString()
                            else -> {
                                symbolTable.addEntry(symbol, commandCounter)
                            }
                        }
                    }

                    InstructionType.A_INSTRUCTION,
                    InstructionType.C_INSTRUCTION -> commandCounter++
                }
            }
        }

        // second passage
        with(Parser(input)) {
            while (true) {
                advance()
                if (!hasMoreLines()) break

                val outLine: String = when (instructionType()) {
                    InstructionType.A_INSTRUCTION -> {
                        val symbol = symbol()
                        when {
                            symbol.toShortOrNull() != null -> symbol.toShort().to16BinaryString()
                            symbolTable.contains(symbol) -> symbolTable.getAddress(symbol).to16BinaryString()
                            else -> {
                                val newAddress = currentAddress.also { currentAddress++ }
                                symbolTable.addEntry(symbol, newAddress)
                                newAddress.to16BinaryString()
                            }
                        }
                    }

                    InstructionType.L_INSTRUCTION -> ""

                    InstructionType.C_INSTRUCTION -> {
                        val comp = code.comp(comp())
                        val dest = code.dest(dest())
                        val jump = code.jump(jump())
                        "111$comp$dest$jump"
                    }
                }

                if (outLine.isNotBlank()) sb.appendLine(outLine.trim())
            }
        }

        output.writeText(sb.toString().trimIndent())
    }

    private fun Short.to16BinaryString(): String = toString(radix = 2).padStart(length = 16, padChar = '0')

    private companion object {
        const val START_USER_ADDRESS: Short = 16
    }
}