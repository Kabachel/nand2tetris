package me.kabachel.nand2tetris

import java.io.File
import java.util.logging.Logger

// TODO: #3 do better error returning
class Parser(
    private val inputFile: File,
) {
    private val logger = Logger.getLogger(this::class.java.name)

    private val reader = inputFile.bufferedReader(
        charset = Charsets.UTF_8,
        bufferSize = DEFAULT_BUFFER_SIZE,
    )

    private var currentCommand: String? = null
    private var eof = false

    fun hasMoreLines(): Boolean = !eof

    fun advance() {
        var currentLine = ""

        while (currentLine.isBlank()) {
            val rawLine = reader.readLine()

            if (rawLine == null) {
                logger.info("advance(): EOF reached for file='${inputFile.path}'")
                reset()
                return
            }

            currentLine = rawLine
                .substringBefore(ONE_LINE_COMMENT_START)
                .filter { !it.isWhitespace() }
        }

        currentCommand = currentLine.also {
            logger.info("advance(): Current command set to: '$it'")
        }
    }

    // TODO: #2 add validation for each instruction type
    fun instructionType(): InstructionType {
        val cmd = currentCommand ?: error("Current command was null, use advance() instead.")

        return when {
            cmd.startsWith('@') -> InstructionType.A_INSTRUCTION
            cmd.startsWith('(') && cmd.endsWith(')') -> InstructionType.L_INSTRUCTION
            cmd.contains(';') || cmd.contains('=') -> InstructionType.C_INSTRUCTION

            else -> error("Unknown instruction: $cmd")
        }
    }

    fun symbol(): String {
        val cmd = currentCommand ?: error("Current command was null, use advance() instead.")

        return when (instructionType()) {
            InstructionType.L_INSTRUCTION -> cmd.trim('(', ')') // (xxx) -> xxx
            InstructionType.A_INSTRUCTION -> cmd.substringAfter('@') // @xxx -> xxx

            InstructionType.C_INSTRUCTION -> error("symbol() shouldn't call for C_INSTRUCTION.")
        }
    }

    // TODO: #4 maybe use null-safety for returning value
    fun dest(): Dest? {
        val cmd = currentCommand ?: error("Current command was null, use advance() instead.")

        return when (instructionType()) {
            InstructionType.C_INSTRUCTION -> {
                val dest = cmd.substringBefore('=', missingDelimiterValue = "")
                if (dest.isNotBlank()) {
                    Dest.from(dest) ?: error("Unknown dest command: '$dest'")
                } else null
            }

            InstructionType.A_INSTRUCTION,
            InstructionType.L_INSTRUCTION -> error("dest() should be called only for C_INSTRUCTION.")
        }
    }

    fun comp(): Comp {
        val cmd = currentCommand ?: error("Current command was null, use advance() instead.")

        return when (instructionType()) {
            InstructionType.C_INSTRUCTION -> {
                val afterDest = cmd.substringAfter('=', missingDelimiterValue = cmd)
                val comp = afterDest.substringBefore(';', missingDelimiterValue = afterDest)
                Comp.from(comp) ?: error("Unknown comp command: '$comp'")
            }

            InstructionType.A_INSTRUCTION,
            InstructionType.L_INSTRUCTION -> error("comp() should be called only for C_INSTRUCTION.")
        }
    }

    fun jump(): Jump? {
        val cmd = currentCommand ?: error("Current command was null, use advance() instead.")

        return when (instructionType()) {
            InstructionType.C_INSTRUCTION -> {
                val jump = cmd.substringAfter(';', missingDelimiterValue = "")
                if (jump.isNotBlank()) {
                    Jump.from(jump) ?: error("Unknown jump command: '$jump'")
                } else null
            }

            InstructionType.A_INSTRUCTION,
            InstructionType.L_INSTRUCTION -> error("jump() should be called only for C_INSTRUCTION.")
        }
    }

    private fun reset() {
        currentCommand = null
        eof = true
        reader.close()
    }

    private companion object {
        private const val ONE_LINE_COMMENT_START = "//"

        // TODO: #1 add support for multiline comments
        private const val MULTILINE_COMMENT_START = "/*"
        private const val MULTILINE_COMMENT_ENT = "*/"
    }
}