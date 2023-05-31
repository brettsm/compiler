	mov bx, OFFSET EnterMessage
	call PrintString
	call GetAnInteger
	mov ax,[ReadInt]
	mov [A ], ax
	mov bx, OFFSET EnterMessage
	call PrintString
	call GetAnInteger
	mov ax,[ReadInt]
	mov [B ], ax
	mov bx, OFFSET EnterMessage
	call PrintString
	call GetAnInteger
	mov ax,[ReadInt]
	mov [C ], ax
W1:	mov ax,[A]
	cmp ax,[Lit20]
	JLE	L1
	mov ax,[A]
	cmp ax,[Lit1]
	JGE	L2
W2:	mov ax,[A]
	cmp ax,[Lit7]
	JGE	L3
	mov ax,[A]
	add ax,[Lit2]
	mov [T1], ax
	mov ax,[T1]
	mul [Lit2]
	mov [T2], ax
	mov ax,[T2]
	mov [A], ax
	mov ax,[A]
	cmp ax,[Lit15]
	JLE	L4
	mov ax,[A]
	sub ax,[Lit2]
	mov [T1], ax
	mov ax,[T1]
	mov [A], ax
W3:	mov ax,[A]
	cmp ax,[Lit5]
	JLE	L5
	mov ax,[A]
	sub ax,[Lit1]
	mov [T1], ax
	mov ax,[T1]
	mov [A], ax
	jmp W3
L5:	nop
L4:	nop
	jmp W2
L3:	nop
L2:	nop
	jmp W1
L1:	nop
W4:	mov ax,[A]
	cmp ax,[D]
	JLE	L6
	mov ax,[A]
	sub ax,[Lit2]
	mov [T1], ax
	mov ax,[T1]
	mov [A], ax
W5:	mov ax,[A]
	cmp ax,[C]
	JG	L7
	mov ax,[A]
	add ax,[Lit1]
	mov [T1], ax
	mov ax,[T1]
	mov [A], ax
W6:	mov ax,[C]
	cmp ax,[A]
	JE	L8
	mov ax,[C]
	sub ax,[Lit1]
	mov [T1], ax
	mov ax,[T1]
	mov [C], ax
W7:	mov ax,[D]
	cmp ax,[Lit5]
	JNE	L9
	mov ax,[D]
	add ax,[Lit5]
	mov [T1], ax
	mov ax,[T1]
	mov [D], ax
W8:	mov ax,[D]
	cmp ax,[Lit4]
	JLE	L10
	mov ax,[D]
	sub ax,[Lit1]
	mov [T1], ax
	mov ax,[T1]
	mov [D], ax
	jmp W8
L10:	nop
	jmp W7
L9:	nop
	jmp W6
L8:	nop
	jmp W5
L7:	nop
	jmp W4
L6:	nop
	mov ax,[A]
	add ax,[B]
	mov [T1], ax
	mov ax,[T1]
	cmp ax,[C]
	JLE	L11
	mov ax,[C]
	cmp ax,[A]
	JG	L12
	mov ax,[A]
	add ax,[Lit4]
	mov [T1], ax
	mov ax,[T1]
	mov [A], ax
	mov ax,[B]
	add ax,[Lit5]
	mov [T2], ax
	mov ax,[T2]
	mov [B], ax
	mov ax,[D]
	cmp ax,[Lit4]
	JL	L13
	mov ax,[Lit3]
	mov [D], ax
	mov ax,[D]
	cmp ax,[Lit3]
	JNE	L14
	mov ax,[Lit5]
	mov [D], ax
	mov ax,[D]
	cmp ax,[Lit3]
	JLE	L15
	mov ax,[Lit3]
	mov [D], ax
L15:	nop
L14:	nop
L13:	nop
L12:	nop
	mov ax,[A]
	add ax,[Lit1]
	mov [T1], ax
	mov ax,[T1]
	mov [A], ax
L11:	nop
	mov ax,[A]
	add ax,[Lit2]
	mov [T1], ax
	mov ax,[T1]
	add ax,[Lit3]
	mov [T2], ax
	mov ax,[T2]
	add ax,[Lit4]
	mov [T3], ax
	mov ax,[T3]
	mov [A], ax
	mov ax,[A]
	add ax,[Lit1]
	mov [T1], ax
	mov ax,[A]
	mul [T1]
	mov [T2], ax
	mov ax,[T2]
	mov [C], ax
	mov ax,[Lit2]
	mul [Lit1]
	mov [T1], ax
	mov ax,[Lit13]
	add ax,[T1]
	mov [T2], ax
	mov ax,[Lit1]
	mul [T2]
	mov [T3], ax
	mov ax,[Lit12]
	sub ax,[Lit2]
	mov [T4], ax
	mov dx,0
	mov ax,[T3]
	mov bx,[T4]
	div bx
	mov [T5], ax
	mov ax,[Lit1]
	add ax,[T5]
	mov [T6], ax
	mov ax,[T6]
	add ax,[Lit1]
	mov [T7], ax
	mov ax,[T7]
	mov [B], ax
	mov ax,[A]
	call convertIntegerToString
	mov bx,Offset Result
	call PrintString
