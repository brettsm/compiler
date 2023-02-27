/*
 * Brett Smith
 * Feb 14th, 2023
 * Program for parser using FSA for pass 1 in Language Translator
 * It will read until EOF, passing file pointer to FSA
 * FSA will return tokens and this program will make a table from the FSAs output
 */

package lexan;

import java.io.*;

//Pg 44 in notes helps a lot with this stuff
//Character.isLetter(char)
//Character.isDigit(char)
// ^^^^ Helpful for determining which state to go to next
// dont need to list all letters of alphabet to determine if character is a - Z


public class parser {
    String token = "";
    public parser(String source) throws FileNotFoundException
    {
        Reader scan = new FileReader(source);

    }

    public static String generateTable(String source) throws IOException {
        boolean fin = false;
        int next_state = 0;
        Reader reader = new FileReader(source);
        
        int r;
        myFSA = new FSA();
        String token;
        char nextch;

        //read in a line, send to FSA
        //FSA will go through character by character

        while(r = reader.read() != -1)
        {
            do {
                switch(next_state) {
                    case 0: myFSA.stateT[0][myFSA]
                }
            }while(!fin)
        }

    }


    public static void main(String[] args) {
        

    }

    
}