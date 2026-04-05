package me.kabachel

import java.io.File
import java.util.logging.Logger

class VmTranslator {

    private val logger = Logger.getLogger(this::class.java.name)

    fun translate(input: File, output: File) {
        logger.info("translate(): called for file='${input.path}'")

        val codeWriter = CodeWriter(output)

        with(Parser(input)) {
            while (true) {
                advance()

                if (!hasMoreLines()) break
                when (commandType()) {
                    CommandType.C_ARITHMETIC -> codeWriter.writeArithmetic(arg1())
                    CommandType.C_PUSH -> codeWriter.writePushPop(CommandType.C_PUSH, arg1(), arg2())
                    CommandType.C_POP -> codeWriter.writePushPop(CommandType.C_POP, arg1(), arg2())
                    CommandType.C_LABEL -> TODO()
                    CommandType.C_GOTO -> TODO()
                    CommandType.C_IF -> TODO()
                    CommandType.C_FUNCTION -> TODO()
                    CommandType.C_RETURN -> TODO()
                    CommandType.C_CALL -> TODO()
                }
            }
        }

        codeWriter.close()
    }
}
