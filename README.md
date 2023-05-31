# compiler
This is the code for my semester project for my Compiler Class. The compiler can generate the assembly code for the Java 0 programming langauge.

It has the ability to implement:
1. Deeply Nested If-Then-Else Statements
2. Deeply Nested While-Do Loops
3. Decently Complex Boolean Expressions
4. Complex Mathematical Equations with nested parentheses
5. Subroutines and Function Calls
6. Recursive Functions

The project is split into two parts:

1. Lexical Analyzer:
    - The lexical analyzer recognize Java 0 programming language and will break down code written in excode.txt into a Token List and Symbol       Table
    - Uses a Finite State Automaton in order to build these

2. Syntax Analyzer:
    - The Syntax Analyzer reads from the Token List and creates a quads intermediate format of the code following correct order of operations
    - Uses a Push Down Automaton with an operator precedence table to ensure correct order

