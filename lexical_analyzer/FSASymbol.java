//FSASymbol.java
//Brett Smith
//March 10, 2023
//This is the Finite State Automaton for the construction of the 
//Symbol Table from the Token List
//===============================================================================================

import java.util.*;
import java.io.*;

public class FSASymbol {
    private String fname = "symbolFSA.txt";
    private Scanner s;
    private int[][] table;
    private int rows, cols;

    //The constructor builds the state table from a csv text
    //file. The size of the table is based on how many rows
    //and cols are in the state table text file

    public FSASymbol() throws FileNotFoundException {
        s = new Scanner(new File(fname));
        int row;
        rows = cols = 0;
        String line;
        String[] vals;
        while(s.hasNextLine()) {
            line = s.nextLine();
            vals = line.split(",");
            cols = vals.length;
            rows++;
        }
        s.close();

        table = new int[rows][cols];

        s = new Scanner(new File(fname));

        row = 0;

        while(s.hasNextLine() && row < table.length) {
            line = s.nextLine();
            vals = line.split(",");

            for(int c = 0; c < vals.length; c++) {
                table[row][c] = Integer.parseInt(vals[c].trim());
            }

            row++;
        }
        s.close();
    }

    //prints the state table

    public void printTable() {

        //basic loop through and print out 2d array
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                System.out.print(this.table[i][j] + " ");
            }
            System.out.println();
        }
    }

    //returns next state from a row col lookup using
    //current state, and input string (token class)
    public int nextState(int s, String in) {
        
        return this.table[s][toIndex(in)];
    }


    //converts the input token class to index of the cols
    //in the state table
    public int toIndex(String s) {
        
        if(s.equals("$Class"))
            return 0;
        else if(s.equals("<var>"))
            return 1;
        else if(s.equals("<lb>"))
            return 2;
        else if(s.equals("$Const"))
            return 3;
        else if(s.equals("<assignment>"))
            return 4;
        else if(s.equals("Lit"))
            return 5;
        else if(s.equals("$semi"))
            return 6;
        else if(s.equals("$VARDec"))
            return 7;
        else if(s.equals("$comma"))
            return 8;
        else if(s.equals("$If"))
            return 12;
        else if(s.equals("$Then"))
            return 9;
        else if(s.equals("$Procedure"))
            return 9;
        else if(s.equals("$While"))
            return 11;
        else if(s.equals("$Call") || s.equals("$Print") || s.equals("$Get") || s.equals("$Return") || s.equals("$Else"))
            return 9;
        else if(s.equals("$Do"))
            return 9;
        else if(s.equals("$Odd"))
            return 9;
        else if(s.equals("$lp"))
            return 10;
        else if(s.equals("$rp"))
            return 10;
        else if(s.equals("<mop>") || s.equals("$addop") || s.equals("<relop>") || s.equals("<min_op>"))
            return 10;
        else if(s.equals("<rb>"))
            return 10;
        else return -1;
        //end if
            
    }

    

}