package me.kabachel

import java.io.File
import java.io.PrintWriter

class CodeWriter(
    private val outputFile: File,
) {

    private val writer = PrintWriter(outputFile)

    private var labelCounter = 0

    fun writeArithmetic(command: String) {
        when (command) {
            ADD_COMMAND.command -> writeBinaryOperation(ADD_COMMAND.operation)
            SUB_COMMAND.command -> writeBinaryOperation(SUB_COMMAND.operation)
            AND_COMMAND.command -> writeBinaryOperation(AND_COMMAND.operation)
            OR_COMMAND.command -> writeBinaryOperation(OR_COMMAND.operation)
            NEG_COMMAND.command -> writeUnaryOperation(NEG_COMMAND.operation)
            NOT_COMMAND.command -> writeUnaryOperation(NOT_COMMAND.operation)
            EQ_COMMAND.command -> writeComparisonOperation(EQ_COMMAND.operation)
            GT_COMMAND.command -> writeComparisonOperation(GT_COMMAND.operation)
            LT_COMMAND.command -> writeComparisonOperation(LT_COMMAND.operation)
        }
    }

    private fun writeBinaryOperation(operation: String) {
        val code = """
            @SP
            AM=M-1
            D=M
            A=A-1
            $operation
            """.trimIndent()
        writeln(code)
    }

    private fun writeUnaryOperation(operation: String) {
        val code = """
            @SP
            A=M-1
            $operation
            """.trimIndent()
        writeln(code)
    }

    private fun writeComparisonOperation(operation: String) {
        val labelTrue = $$"$true$$labelCounter"
        val labelEnd = $$"end$$labelCounter"

        val code = """
            @SP
            AM=M-1
            D=M
            A=A-1
            D=M-D
            @$labelTrue
            D;$operation
            @SP
            A=M-1
            M=0
            @$labelEnd
            0;JMP
            ($labelTrue)
            @SP
            A=M-1
            M=-1
            ($labelEnd)
            """.trimIndent()
        writeln(code)

        labelCounter++
    }

    fun writePushPop(command: CommandType, segment: String, index: Int) {
        when (command) {
            CommandType.C_PUSH -> writePush(segment, index)
            CommandType.C_POP -> writePop(segment, index)
            else -> error("Invalid command, use push or pop only")
        }
    }

    private fun writePush(segment: String, index: Int) {
        when (segment) {
            "constant" -> {
                val code = """
                    @$index
                    D=A
                    """.trimIndent()
                writeln(code)
            }

            "local", "argument", "this", "that" -> {
                val symbol = SEGMENT_SYMBOLS[segment] ?: error("Invalid segment, use pop only")
                val code = """
                    @$symbol
                    D=M
                    @$index
                    A=D+A
                    D=M
                    """.trimIndent()
                writeln(code)
            }

            "temp" -> {
                val code = """
                    @${5 + index}
                    D=M
                    """.trimIndent()
                writeln(code)
            }

            "pointer" -> {
                val symbol = if (index == 0) "THIS" else "THAT"
                val code = """
                    @$symbol
                    D=M
                    """.trimIndent()
                writeln(code)
            }

            "static" -> {
                val fileName = outputFile.nameWithoutExtension
                val code = """
                    @$fileName.$index
                    D=M
                    """.trimIndent()
                writeln(code)
            }

            else -> error("Invalid segment, use pop only")
        }
        pushD()
    }

    private fun writePop(segment: String, index: Int) {
        when (segment) {
            "local", "argument", "this", "that" -> {
                val symbol = SEGMENT_SYMBOLS[segment] ?: error("Invalid segment, use push only")
                val code = """
                    @$symbol
                    D=M
                    @$index
                    D=D+A
                    @R13
                    M=D
                    @SP
                    AM=M-1
                    D=M
                    @R13
                    A=M
                    M=D
                """.trimIndent()
                writeln(code)
            }

            "temp" -> {
                val code = """
                    @SP
                    AM=M-1
                    D=M
                    @${5 + index}
                    M=D
                """.trimIndent()
                writeln(code)
            }

            "pointer" -> {
                val symbol = if (index == 0) "THIS" else "THAT"

                val code = """
                    @SP
                    AM=M-1
                    D=M
                    @$symbol
                    M=D
                """.trimIndent()
                writeln(code)
            }

            "static" -> {
                val fileName = outputFile.nameWithoutExtension
                val code = """
                    @SP
                    AM=M-1
                    D=M
                    @$fileName.$index
                    M=D
                """.trimIndent()
                writeln(code)
            }

            "constant" -> {
                error("Cannot pop to constant segment")
            }

            else -> error("Invalid segment, use push only")
        }
    }

    fun close() {
        writer.flush()
        writer.close()
    }

    private fun writeln(str: String) {
        writer.println(str)
    }

    /**
     * *SP=D
     * SP++
     */
    private fun pushD() {
        val code = """
            @SP
            A=M
            M=D
            @SP
            M=M+1
        """.trimIndent()
        writeln(code)
    }

    private data class Command(
        val command: String,
        val operation: String,
    )

    private companion object {
        // arithmetic commands
        private val ADD_COMMAND = Command("add", "M=D+M")
        private val SUB_COMMAND = Command("sub", "M=M-D")
        private val AND_COMMAND = Command("and", "M=D&M")
        private val OR_COMMAND = Command("or", "M=D|M")
        private val NEG_COMMAND = Command("neg", "M=-M")
        private val NOT_COMMAND = Command("not", "M=!M")
        private val EQ_COMMAND = Command("eq", "JEQ")
        private val GT_COMMAND = Command("gt", "JGT")
        private val LT_COMMAND = Command("lt", "JLT")

        private val SEGMENT_SYMBOLS = mapOf(
            "local" to "LCL",
            "argument" to "ARG",
            "this" to "THIS",
            "that" to "THAT",
        )
    }
}