import java.util.*;
import java.io.*;

public class PrecedenceTable {
    String[][] table;
    int rows, cols;
    public PrecedenceTable(String fname) throws FileNotFoundException {
        Scanner init = new Scanner(new File(fname));
        this.rows = this.cols = 0;
        
        int row = 0;
        String line[];
        while(init.hasNextLine()) {
            line = init.nextLine().split(",");
            rows++;
            cols = line.length;
        }

        table = new String[rows][cols];
        init.close();
        init = new Scanner(new File(fname));
        while(init.hasNextLine() && row < table.length) {
            line = init.nextLine().split(",");
            for(int c = 0; c < line.length; c++) {
                table[row][c] = line[c];
            }
            row++;
        }

        init.close();
    }

    public static int toIndex(String in) {
        switch(in) {
            case "$": 
                return 17;
            case ";":
                return 0;
            case "=":
                return 1;
            case "+":
                return 2;
            case "-":
                return 3;
            case "(":
                return 4;
            case ")":
                return 5;
            case "*":
                return 6;
            case "/":
                return 7;
            case "IF":
                return 8;
            case "<":
                return 9;
            case ">":
                return 9;
            case "<=":
                return 9;
            case ">=":
                return 9;
            case "==":
                return 9;
            case "!=":
                return 9;
            case "THEN":
                return 10;
            case "CALL":
                return 11;
            case "WHILE":
                return 12;
            case "{":
                return 13;
            case "}":
                return 14;
            case "ODD":
                return 15;
            case "DO":
                return 16;
            case "`":
                return 18;
            case "PRINT":
                return 19;
            case "GET":
                return 20;
            case "CLASS":
                return 21;
            case "PROCEDURE":
                return 22;
            case "RETURN":
                return 23;
            case "ELSE":
                return 24;
            default:
                return -1;
        }
    }

    public String getRel(Token t1, Token t2) {
        return this.table[toIndex(t1.val)][toIndex(t2.val)];
    }

    public boolean isOperator(String in) {
        switch(in) {
            case "$": 
                return true;
            case ";":
                return true;
            case "=":
                return true;
            case "+":
                return true;
            case "-":
                return true;
            case "(":
                return true;
            case ")":
                return true;
            case "*":
                return true;
            case "/":
                return true;
            case "IF":
                return true;
            case "<":
                return true;
            case ">":
                return true;
            case "<=":
                return true;
            case ">=":
                return true;
            case "==":
                return true;
            case "!=":
                return true;
            case "THEN":
                return true;
            case "CALL":
                return true;
            case "WHILE":
                return true;
            case "{":
                return true;
            case "}":
                return true;
            case "ODD":
                return true;
            case "DO":
                return true;
            case "`":
                return true;
            case "PRINT":
                return true;
            case "GET":
                return true;
            case "CLASS":
                return true;
            case "PROCEDURE":
                return true;
            case "RETURN":
                return true;
            case "ELSE":
                return true;
            default:
                return false;
        }
    }
    public static String toToken(int in) {
        switch(in) {
            case 17: 
                return "$";
            case 0:
                return ";";
            case 1:
                return "=";
            case 2:
                return "+";
            case 3:
                return "-";
            case 4:
                return "(";
            case 5:
                return ")";
            case 6:
                return "*";
            case 7:
                return "/";
            case 8:
                return "IF";
            case 9:
                return "RELOP";
            case 10:
                return "THEN";
            case 11:
                return "CALL";
            case 12:
                return "WHILE";
            case 13:
                return "{";
            case 14:
                return "}";
            case 15:
                return "ODD";
            case 16:
                return "DO";
            case 18:
                return "`";
            case 19:
                return "PRINT";
            case 20:
                return "GET";
            case 21:
                return "CLASS";
            case 22:
                return "PROCEDURE";
            case 23:
                return "RETURN";
            case 24:
                return "ELSE";
            default:
                return "NONE";
        }
    }

    public void getPossibleNext(String in) {
        for(int i = 0; i < this.cols; i++) {
            if(!table[toIndex(in)][i].equals(".")) {
                System.out.print(toToken(i) + ", ");
            }
        }
    }
    
}
