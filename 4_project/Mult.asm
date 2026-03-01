// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// The algorithm is based on repetitive addition.

    // if (R0 == 0) goto STOP
    @R0
    D=M
    @STOP
    D;JEQ
    // if (R1 == 0) goto STOP
    @R1
    D=M
    @STOP
    D;JEQ
    // i = R1; mul = 0
    @R1
    D=M
    @i
    M=D
    @mul
    M=0
(LOOP)
    // if (i == 0) goto STOP
    @i
    D=M
    @STOP
    D;JEQ
    // mul = mul + R0
    @mul
    D=M
    @R0
    D=D+M
    @mul
    M=D
    // i = i - 1
    @i
	M=M-1
	// goto LOOP
	@LOOP
	0;JMP
(STOP)
    // R2 = mul
    @mul
    D=M
    @R2
    M=D
(END)
    @END
    0;JMP