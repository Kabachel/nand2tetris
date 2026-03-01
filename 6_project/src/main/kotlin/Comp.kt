package me.kabachel.nand2tetris

enum class Comp(val value: String) {
    ZERO("0"),
    ONE("1"),
    MINUS_ONE("-1"),
    D("D"),
    A("A"),
    M("M"),
    NOT_D("!D"),
    NOT_A("!A"),
    NOT_M("!M"),
    MINUS_D("-D"),
    MINUS_A("-A"),
    MINUS_M("-M"),
    D_PLUS_ONE("D+1"),
    A_PLUS_ONE("A+1"),
    M_PLUS_ONE("M+1"),
    D_MINUS_ONE("D-1"),
    A_MINUS_ONE("A-1"),
    M_MINUS_ONE("M-1"),
    D_PLUS_A("D+A"),
    D_PLUS_M("D+M"),
    D_MINUS_A("D-A"),
    D_MINUS_M("D-M"),
    A_MINUS_D("A-D"),
    M_MINUS_D("M-D"),
    D_AND_A("D&A"),
    D_AND_M("D&M"),
    D_OR_A("D|A"),
    D_OR_M("D|M"),
    ;

    companion object {
        fun from(mnemonic: String?): Comp? =
            entries.find { it.value == mnemonic }
    }
}