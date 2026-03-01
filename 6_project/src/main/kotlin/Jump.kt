package me.kabachel.nand2tetris

enum class Jump {
    JGT,
    JEQ,
    JGE,
    JLT,
    JNE,
    JLE,
    JMP;

    companion object {
        fun from(mnemonic: String?): Jump? =
            entries.find { it.name == mnemonic }
    }
}