package me.kabachel.nand2tetris

enum class Dest(val letters: Set<Char>) {
    M(setOf('M')),
    D(setOf('D')),
    DM(setOf('D', 'M')),
    A(setOf('A')),
    AM(setOf('A', 'M')),
    AD(setOf('A', 'D')),
    ADM(setOf('A', 'D', 'M'));

    companion object {
        fun from(mnemonic: String?): Dest? {
            if (mnemonic.isNullOrBlank()) return null
            val set = mnemonic.toSet()
            return entries.find { it.letters == set }
        }
    }
}
