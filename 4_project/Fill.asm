// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

    @status
    M=0          // start white

(LOOP)
    // Read keyboard
    @KBD
    D=M
    @DRAW_WHITE
    D;JEQ       // if no key -> draw white
    @DRAW_BLACK
    0;JMP       // else -> draw black

(DRAW_WHITE)
    @R0
    M=0
    @UPDATE
    0;JMP

(DRAW_BLACK)
    @R0
    M=-1

(UPDATE)
    // If desired color == status, skip redraw
    @R0
    D=M
    @status
    D=D-M
    @LOOP
    D;JEQ

    // Update status to new color
    @R0
    D=M
    @status
    M=D

    // i will walk screen memory from end to start
    @SCREEN
    D=A
    @8192
    D=D+A      // D = SCREEN + 8192 (one past last word)
    @i
    M=D

(FILL_LOOP)
    @i
    M=M-1
    D=M
    @LOOP
    D;JLT      // if i < SCREEN, done, go poll keyboard again

    @status
    D=M        // D = current color
    @i
    A=M        // A = current screen word
    M=D        // paint 16 pixels

    @FILL_LOOP
    0;JMP