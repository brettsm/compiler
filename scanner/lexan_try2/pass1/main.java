package pass1;

import java.io.*;
import java.util.*;

public class main {
    public static void main(String args[]) throws IOException{
        String file_name;
        int current_state = 0;
        int r;
        char ch = ' ';
        Scanner user = new Scanner(System.in);
        String current_token = "";

        //need another function calling this one so that it can loop repeatedly

        file_name = "/Users/brettsmith/compiler/scanner/lexan_try2/pass1/excode.txt";
        FileReader file = new FileReader(file_name);
        BufferedReader buff = new BufferedReader(file);

        //FSATable lex = new FSATable("/Users/brettsmith/compiler/scanner/lexan_try2/pass1/lexFSA.csv");
        FSATable lex = new FSATable();

        //works sometimes, now need to create new class for communicating with fsa while main only reads in characters to the new class
        //need to fix when returning a variable, will skip over a few tokens (maybe recall with current token after returning a value [call for current in the case statement and then break;])
        while( (r = buff.read()) != -1 )
        {
            ch = (char)r;
            //if(buff.markSupported())
                //buff.mark(1);
            System.out.println("Ch: " + ch);
            System.out.println("current_token: " + current_token);
            //System.out.println("current_state: " + current_state);
            current_state = lex.next_state(current_state, ch);
            System.out.println("current_state: " + current_state);
            switch (current_state) {
                case 0: break;
                case 1:
                    System.out.println("Error: character not in table");
                    break;
                case 2: //add *, <mop> to symbol table
                    System.out.println("<mop>, " + current_token + " returned");
                    break;
                case 6: //add (varname), <var> to symbol table
                    System.out.println("Variable: " + current_token + " returned");
                    current_token = "";
                    //call function here to process for input here, because it will be skipped over in the next loop
                    break;
                case 10: //add /, <mop> to symbol table
                    System.out.println("<mop>, " + current_token + " returned");
                    current_token = "";
                    break;
                case 12: //add =, <assignment> to symbol table
                    System.out.println("<assignment>, " + current_token + " returned");
                    current_token = "";
                    break;
                case 13: //add ==, <relop> to symbol table
                    System.out.println("<relop>, " + current_token + " returned");
                    current_token = "";
                    break;
                case 15: //add <, <relop> to symbol table
                    System.out.println("<relop>, " + current_token + " returned");
                    current_token = "";
                    break;
                case 16: //add <=, <relop> to symbol table
                    System.out.println("<relop>, " + current_token + " returned");
                    current_token = "";
                    break;
                default:
                    current_token = current_token + ch;
                    break;
            }
        }

        //lex.printT();
        buff.close();
    }
}
