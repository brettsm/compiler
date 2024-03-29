//Brett Smith
//March 2nd, 2023
//The FSA used for creating the token list

import java.io.*;
import java.util.Scanner;

public class FSA {
    
    private int[][] table;
    Scanner s;
    private int rows, cols;

    //creates the transition table from a text file with values separated by commas
    public FSA(String fname) throws IOException{
        s = new Scanner(new File(fname));
        rows = cols = 0; //count num cols and rows so that it fits the number in file
        int row;
        String line; //used to store all 
        String[] values;


        //
        while(s.hasNextLine()) {
            line = s.nextLine(); 
                //get entire line including commas
            values = line.split(","); 
                //separate values by comma and store into String array
                //values = ["1", "3", "5", ...] (format of values)
            cols = values.length;
                //update cols to length of the values (will end up being the number of cols of last row [could use max to get largest])
            rows++;
                //increment rows (each get line is a row)
        }

        s.close();
            //close to reset s back to beginning of file


        table = new int[rows][cols];
            //create table from num rows and cols from text file

        s = new Scanner(new File(fname));
            //open same file

        row = 0;
            //used as increment index

        while(s.hasNextLine() && row < table.length) {
            line = s.nextLine();
                //read in the entire line
            values = line.split(",");
                //store values as strings into String[] values with comma as delimiter
                //values acts as the current row, moving from top to bottom in each iteration of the loop

            for(int c = 0; c < values.length; c++) {
                table[row][c] = Integer.parseInt(values[c].trim());
                    //left to right, convert each String value in the current row into an Integer and store into the FSA transition table

            }

            row++;
                //move to the next row, end after last row
        }
        s.close();
    }

    public void printTable() {

        //basic loop through and print out 2d array
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                System.out.print(this.table[i][j] + " ");

            }
            System.out.println();
        }
    }

    //given a character, maps it to the an index of the state table
    public static int char_to_index(char c) {
        if(Character.isWhitespace(c) || c == '\n') {
            return 6;
        }
        else if(Character.isLetter(c)) {
            return 0;
        }
        else if(Character.isDigit(c)) {
            return 1;
        }
        else if(c == '*') {
            return 2;
        }
        else if(c == '/') {
            return 3;
        }
        else if(c == '=') {
            return 4;
        }
        else if(c == '<') {
            return 5;
        }
        else if(c == '{') {
            return 7;
        }
        else if(c == '}') {
            return 8;
        }
        else if(c == '+') {
            return 9;
        }
        else if(c == ';') {
            return 10;
        }
        else if(c == ',') {
            return 11;
        }
        else if(c == '(') {
            return 12;
        }
        else if(c == ')') {
            return 13;
        }
        else if(c == '>') {
            return 14;
        }
        else if(c == '!') {
            return 15;
        } 
        else if(c == '-') {
            return 16;
        }
        else {
            return -1;
        }
            //These are basically just the indices of the columns of the heuristic parser table
    }

    //given the current state and an input character returns next state
    //with a row col lookup for the state table here
    public int nextState(int curst, char in) {
        
        return this.table[curst][char_to_index(in)];
        
    }


    //used to determine if in a final state
    //Unused
    public boolean isFinal(int state) {
        if(table[state][2] == 1) {
            return true;
        }
        else {
            return false;
        }
    }


}
