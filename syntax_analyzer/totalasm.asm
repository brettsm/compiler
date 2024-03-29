;IO1NasmLinux32.asm also 64 bit
	;
        ;assemble:	nasm -f elf -l IO1Nasm32Linux.lst  IO1Nasm32Linux.asm
        ;link:  	gcc -o IO1Nasm32Linux  IO1Nasm32Linux.o
        ;run:           ./IO1Nasm32Linux
	;
	;For 64 bit Linux
	;nasm -felf64 IO1Nasm32Linux.asm 
             ;ld IO1Nasm32Linux.o 
             ;./a.out
	;
	;for debugging with gdb or DDD try
	;nasm -g dwarf2 -f elf64 IO1Nasm32Linux.asm -l IO1Nasm32Linux.lst 
             ;                                           -o IO1Nasm32Linux.o  
	;ld -g -o IO1Nasm32Linux IO1Nasm32Linux.o
	;./IO1Nasm32Linux
sys_exit	equ	1
sys_read	equ	3
sys_write	equ	4
stdin		equ	0 ; default keyboard
stdout		equ	1 ; default terminal screen
stderr		equ	3
 

section .data		;used to declare constants	
	userMsg		db      'Enter an integer(less than 32,765): '
	lenUserMsg	equ	$-userMsg
	displayMsg	db	'You entered: '
	lenDisplayMsg	equ	$-displayMsg
	newline		db	0xA 	; 0xA 0xD is ASCII <LF><CR>

	Ten             DW      10  ;Used in converting to base ten.

	printTempchar	db	'Tempchar = : '
	lenprintTempchar	equ 	$-printTempchar


	Result          db      'Ans = '
	ResultValue	db	'aaaaa'
			db	0xA		
	ResultEnd       equ 	$-Result    ; $=> here, subtract address Result

	num		times 6 db 'ABCDEF'
	numEnd		equ	$-num


	Lit1	DW	1

	Lit10	DW	10

	Lit8	DW	8

	Lit6	DW	6

	Lit4	DW	4

	Lit2	DW	2

	Lit5	DW	5

	Lit3	DW	3
; Start of user variable area    ----------------------------------------------

section	.bss		;used to declare uninitialized variables

	TempChar        RESB    1              ;temp space for use by GetNextChar
	testchar        RESB    1	
	ReadInt         RESW    1              ;Temporary storage GetAnInteger.	
	tempint         RESW	1              ;Used in converting to base ten.
	negflag         RESB    1              ;P=positive, N=negative



	count	RESW	1

	T1	RESW	1

	T2	RESW	1

	T3	RESW	1

	T4	RESW	1

	T5	RESW	1

	T6	RESW	1

	T7	RESW	1


	global _start

section .text

_start:


	mov ax,[Lit1]
	mov [count], ax
W1:	mov ax,[count]
	cmp ax,[Lit10]
	JG	L1
W2:	mov ax,[count]
	cmp ax,[Lit8]
	JG	L2
W3:	mov ax,[count]
	cmp ax,[Lit6]
	JG	L3
W4:	mov ax,[count]
	cmp ax,[Lit4]
	JG	L4
W5:	mov ax,[count]
	cmp ax,[Lit2]
	JG	L5
	mov ax,[Lit5]
	call ConvertIntegerToString
	mov eax, 4
	mov ebx, 1
	mov ecx, Result
	mov edx, ResultEnd
	int 80h
	mov ax,[count]
	add ax,[Lit1]
	mov [T1], ax
	mov ax,[T1]
	mov [count], ax
	jmp W5
L5:	nop
	mov ax,[Lit4]
	call ConvertIntegerToString
	mov eax, 4
	mov ebx, 1
	mov ecx, Result
	mov edx, ResultEnd
	int 80h
	mov ax,[count]
	add ax,[Lit1]
	mov [T1], ax
	mov ax,[T1]
	mov [count], ax
	jmp W4
L4:	nop
	mov ax,[Lit3]
	call ConvertIntegerToString
	mov eax, 4
	mov ebx, 1
	mov ecx, Result
	mov edx, ResultEnd
	int 80h
	mov ax,[count]
	add ax,[Lit1]
	mov [T1], ax
	mov ax,[T1]
	mov [count], ax
	jmp W3
L3:	nop
	mov ax,[Lit2]
	call ConvertIntegerToString
	mov eax, 4
	mov ebx, 1
	mov ecx, Result
	mov edx, ResultEnd
	int 80h
	mov ax,[count]
	add ax,[Lit1]
	mov [T1], ax
	mov ax,[T1]
	mov [count], ax
	jmp W2
L2:	nop
	mov ax,[Lit1]
	call ConvertIntegerToString
	mov eax, 4
	mov ebx, 1
	mov ecx, Result
	mov edx, ResultEnd
	int 80h
	mov ax,[count]
	add ax,[Lit1]
	mov [T1], ax
	mov ax,[T1]
	mov [count], ax
	jmp W1
L1:	nop
fini:
	mov eax, 1
	xor ebx, ebx
	int	0x80

PrintString:
	push    ax              ;Save registers;
	push    dx
; subpgm:
	; prompt user	
	mov eax, 4		;Linux print device register
; conventions
	mov ebx, 1		; print default output device
	mov ecx, userMsg	; pointer to string
	mov edx, lenUserMsg	
	int	80h		; interrupt 80 hex, call kernel
	pop     dx              ;Restore registers.
	pop     ax
	ret
;PrintString     ENDP

;%NEWPAGE

;

; Subroutine to get an integer 
;(character string) from the keyboard buffer
; and convert it to a 16 bit binary number.
;
; Input: none
;
; Output: The integer is returned in the AX
;  register as well as the global
;         variable ReadInt .
;
; Registers Destroyed: AX, BX, CX, DX, SI
;
; Globals Destroyed: ReadInt, TempChar, tempint, negflag
;
;GetAnInteger    PROC

GetAnInteger:	;Get an integer as a string
	;get response
	mov eax,3	;read
	mov ebx,2	;device
	mov ecx, num	;buffer address
	mov edx,6	;max characters
	int 0x80

	;print number    ;works
	mov edx,eax 	; eax contains the number of 
; character read including <lf>
	mov eax, 4
	mov ebx, 1
	mov ecx, num
	int 80h

ConvertStringToInteger:
	mov ax,0	;hold integer
	mov [ReadInt],ax ;initialize 16 bit number to zero
	mov ecx,num	;pt - 1st or next digit of number as a string 
				;terminated by <lf>.
	mov bx,0	
	mov bl, byte [ecx] ;contains first or next digit
Next:	sub bl,'0'	;convert character to number
	mov ax,[ReadInt]
	mov dx,10
	mul dx		;eax = eax * 10
	add ax,bx
	mov [ReadInt], ax

	mov bx,0
	add ecx,1 	;pt = pt + 1
	mov bl, byte[ecx]

	cmp bl,0xA	;is it a <lf>
	jne Next	; get next digit   
	ret
;	ENDP GetAnInteger



;%NEWPAGE
;
; Subroutine to convert a 16 bit integer to a text string
;
; input:
;       AX = number to convert
;       DS:BX = pointer to end of string to store text
;       CX = number of digits to convert
;
; output: none
;
; Registers destroyed: AX, BX, CX, DX, SI
; Globals destroyed negflag 
;
;ConvertIntegerToString PROC

ConvertIntegerToString:
	mov ebx, ResultValue + 4   ;Store the integer as a
; five digit char string at Result for printing

ConvertLoop:
	sub dx,dx  ; repeatedly divide dx:ax by 10
; to obtain last digit of number
	mov cx,10  ; as the remainder in the DX
; register.  Quotient in AX.
	div cx
	add dl,'0' ; Add '0' to dl to 
;convert from binary to character.
	mov [ebx], dl
	dec ebx
	cmp ebx,ResultValue
	jge ConvertLoop

	ret

;ConvertIntegerToString  ENDP
