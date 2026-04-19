package me.kabachel

import java.io.File
import java.util.logging.Logger

class VmTranslator {

    private val logger = Logger.getLogger(this::class.java.name)

    fun translate(input: File, output: File) {
        logger.info("translate(): called for file='${input.path}'")

        val codeWriter = CodeWriter(output)

        output.resolveSibling(output.nameWithoutExtension + ASM_EXTENSION).apply {
            codeWriter.setFileName(path)
        }

        translateFile(input, codeWriter)

        codeWriter.close()
    }

    fun translateDirectory(inputDir: File, output: File) {
        logger.info("translateDirectory(): called for dir='${inputDir.path}'")

        val vmFiles = inputDir.listFiles { f -> f.extension == "vm" }
            ?.sortedBy { it.name }
            ?: emptyList()

        require(vmFiles.isNotEmpty()) { "No .vm files found in '${inputDir.path}'" }

        val codeWriter = CodeWriter(output)

        codeWriter.writeInit()

        for (vmFile in vmFiles) {
            output.resolveSibling(output.nameWithoutExtension + ASM_EXTENSION).apply {
                codeWriter.setFileName(path)
            }
            translateFile(vmFile, codeWriter)
        }

        codeWriter.close()

        val fileList = vmFiles.joinToString(", ") { it.name }
        logger.info("translateDirectory(): translated [$fileList] -> ${output.name}")
    }

    private fun translateFile(input: File, codeWriter: CodeWriter) {
        logger.info("translateFile(): processing '${input.name}'")

        with(Parser(input)) {
            while (true) {
                advance()

                if (!hasMoreLines()) break
                when (commandType()) {
                    CommandType.C_ARITHMETIC -> codeWriter.writeArithmetic(arg1())
                    CommandType.C_PUSH -> codeWriter.writePushPop(CommandType.C_PUSH, arg1(), arg2())
                    CommandType.C_POP -> codeWriter.writePushPop(CommandType.C_POP, arg1(), arg2())
                    CommandType.C_LABEL -> codeWriter.writeLabel(arg1())
                    CommandType.C_GOTO -> codeWriter.writeGoto(arg1())
                    CommandType.C_IF -> codeWriter.writeIf(arg1())
                    CommandType.C_FUNCTION -> codeWriter.writeFunction(arg1(), arg2())
                    CommandType.C_RETURN -> codeWriter.writeReturn()
                    CommandType.C_CALL -> codeWriter.writeCall(arg1(), arg2())
                }
            }
        }
    }
}
