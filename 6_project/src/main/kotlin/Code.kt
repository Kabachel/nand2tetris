package me.kabachel.nand2tetris

class Code {

    fun dest(dest: Dest?): String = when (dest) {
        null -> "000"
        Dest.M -> "001"
        Dest.D -> "010"
        Dest.DM -> "011"
        Dest.A -> "100"
        Dest.AM -> "101"
        Dest.AD -> "110"
        Dest.ADM -> "111"
    }

    fun comp(comp: Comp): String = when (comp) {
        Comp.ZERO -> "0101010"
        Comp.ONE -> "0111111"
        Comp.MINUS_ONE -> "0111010"
        Comp.D -> "0001100"
        Comp.A -> "0110000"
        Comp.M -> "1110000"
        Comp.NOT_D -> "0001101"
        Comp.NOT_A -> "0110001"
        Comp.NOT_M -> "1110001"
        Comp.MINUS_D -> "0001111"
        Comp.MINUS_A -> "0110011"
        Comp.MINUS_M -> "1110011"
        Comp.D_PLUS_ONE -> "0011111"
        Comp.A_PLUS_ONE -> "0110111"
        Comp.M_PLUS_ONE -> "1110111"
        Comp.D_MINUS_ONE -> "0001110"
        Comp.A_MINUS_ONE -> "0110010"
        Comp.M_MINUS_ONE -> "1110010"
        Comp.D_PLUS_A -> "0000010"
        Comp.D_PLUS_M -> "1000010"
        Comp.D_MINUS_A -> "0010011"
        Comp.D_MINUS_M -> "1010011"
        Comp.A_MINUS_D -> "0000111"
        Comp.M_MINUS_D -> "1000111"
        Comp.D_AND_A -> "0000000"
        Comp.D_AND_M -> "1000000"
        Comp.D_OR_A -> "0010101"
        Comp.D_OR_M -> "1010101"
    }

    fun jump(jump: Jump?): String = when (jump) {
        null -> "000"
        Jump.JGT -> "001"
        Jump.JEQ -> "010"
        Jump.JGE -> "011"
        Jump.JLT -> "100"
        Jump.JNE -> "101"
        Jump.JLE -> "110"
        Jump.JMP -> "111"
    }
}