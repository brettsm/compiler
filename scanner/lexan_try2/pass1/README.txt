Pass 1 is a package for the lexical analyser portion of the compiler project

What Pass 1 should do:

1. Create Finite State Automaton from text file
2. Read character by character (including whitespace)
3. With each character input, feed the input into the table for the given state
    3.a. concatenate character if not whitespace
    3.b. State Table will take current_state and the next character as input
         and will return the next state

4. The Scanner will check if the current state is a final state, and if it is return
   the current token and its type to the symbol table

5. Continue reading in characters and inputing them into the state machine until out of characters

6. Program should output both a symbol table and Token List...

^^^all of these steps will use a lot of character manipulation and return concattenated character strings to the tables

