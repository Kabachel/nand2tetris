package me.kabachel

import java.io.File
import java.util.logging.Logger

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
                .trim()
        }

        currentCommand = currentLine.also {
            logger.info("advance(): Current command set to: '$it'")
        }
    }

    fun commandType(): CommandType {
        val cmd = currentCommand?.split(" ")[0] ?: error("Use advance() before")

        return when (cmd) {
            in ARITHMETIC_COMMANDS -> CommandType.C_ARITHMETIC
            PUSH_COMMAND -> CommandType.C_PUSH
            POP_COMMAND -> CommandType.C_POP
            LABEL_COMMAND -> CommandType.C_LABEL
            GOTO_COMMAND -> CommandType.C_GOTO
            IF_COMMAND -> CommandType.C_IF
            FUNCTION_COMMAND -> CommandType.C_FUNCTION
            CALL_COMMAND -> CommandType.C_CALL
            RETURN_COMMAND -> CommandType.C_RETURN

            else -> error("Unrecognized command type: $cmd")
        }
    }

    fun arg1(): String {
        val cmd = currentCommand ?: error("Use advance() before")

        val parts = cmd.split(" ")
        return if (commandType() == CommandType.C_ARITHMETIC) parts[0] else parts[1]
    }

    fun arg2(): Int {
        val cmd = currentCommand ?: error("Use advance() before")

        return cmd.split(" ")[2].toIntOrNull() ?: error("arg2() is not a number")
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

        // Commands
        private val ARITHMETIC_COMMANDS = listOf("add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not")
        private const val PUSH_COMMAND = "push"
        private const val POP_COMMAND = "pop"
        private const val LABEL_COMMAND = "label"
        private const val GOTO_COMMAND = "goto"
        private const val IF_COMMAND = "if-goto"
        private const val FUNCTION_COMMAND = "function"
        private const val CALL_COMMAND = "call"
        private const val RETURN_COMMAND = "return"
    }
}
