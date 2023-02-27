package pass1;
import java.util.*;
import java.io.*;

public class FSATable {
    final int ROWS = 17;
    final int COLS = 8;
    private int stateT[][];

    public FSATable() {
        stateT = new int[ROWS][COLS];
        stateT[0][0] = 5; stateT[0][1] = 3; stateT[0][2] = 2; stateT[0][3] = 7; stateT[0][4] = 11; stateT[0][5] = 14; stateT[0][6] = 0; stateT[0][7] = 1;
        stateT[1][0] = 1; stateT[1][1] = 1; stateT[1][2] = 1; stateT[1][3] = 1; stateT[1][4] = 1; stateT[1][5] = 1; stateT[1][6] = 1; stateT[1][7] = 1;
        stateT[2][0] = 0; stateT[2][1] = 0; stateT[2][2] = 0; stateT[2][3] = 0; stateT[2][4] = 0; stateT[2][5] = 0; stateT[2][6] = 0; stateT[2][7] = 0;
        stateT[3][0] = 4; stateT[3][1] = 3; stateT[3][2] = 4; stateT[3][3] = 4; stateT[3][4] = 4; stateT[3][5] = 4; stateT[3][6] = 4; stateT[3][7] = 1;
        stateT[4][0] = 0; stateT[4][1] = 0; stateT[4][2] = 0; stateT[4][3] = 0; stateT[4][4] = 0; stateT[4][5] = 0; stateT[4][6] = 0; stateT[4][7] = 0;
        stateT[5][0] = 5; stateT[5][1] = 5; stateT[5][2] = 6; stateT[5][5] = 6; stateT[5][4] = 6; stateT[5][5] = 6; stateT[5][6] = 6; stateT[5][7] = 1;
        stateT[6][0] = 0; stateT[6][1] = 0; stateT[6][2] = 0; stateT[6][3] = 0; stateT[6][4] = 0; stateT[6][5] = 0; stateT[6][6] = 0; stateT[6][7] = 0;
        stateT[7][0] = 10; stateT[7][1] = 10; stateT[7][2] = 8; stateT[7][3] = 10; stateT[7][4] = 10; stateT[7][5] = 10; stateT[7][6] = 10; stateT[7][7] = 1;
        stateT[8][0] = 8; stateT[8][1] = 8; stateT[8][2] = 9; stateT[8][3] = 8; stateT[8][4] = 8; stateT[8][5] = 8; stateT[8][6] = 8; stateT[8][7] = 1;
        stateT[9][0] = 8; stateT[9][1] = 8; stateT[9][2] = 8; stateT[9][3] = 0; stateT[9][4] = 8; stateT[9][5] = 8; stateT[9][6] = 8; stateT[9][7] = 1;
        stateT[10][0] = 1; stateT[10][1] = 1; stateT[10][2] = 1; stateT[10][3] = 1; stateT[10][4] = 1; stateT[10][5] = 1; stateT[10][6] = 1; stateT[10][7] = 1;            
        stateT[11][0] = 12; stateT[11][1] = 12; stateT[11][2] = 12; stateT[11][3] = 12; stateT[11][4] = 13; stateT[11][5] = 12; stateT[11][6] = 12; stateT[11][7] = 1;
        stateT[12][0] = 1; stateT[12][1] = 1; stateT[12][2] = 1; stateT[12][3] = 1; stateT[12][4] = 1; stateT[12][5] = 1; stateT[12][6] = 1; stateT[12][7] = 1;
        stateT[13][0] = 1; stateT[13][1] = 1; stateT[13][2] = 1; stateT[13][3] = 1; stateT[13][4] = 1; stateT[13][5] = 1; stateT[13][6] = 1; stateT[13][7] = 1;
        stateT[14][0] = 15; stateT[14][1] = 15; stateT[14][2] = 15; stateT[14][3] = 15; stateT[14][4] = 16; stateT[14][5] = 15; stateT[14][6] = 15; stateT[14][7] = 1;
        stateT[15][0] = 1; stateT[15][1] = 1; stateT[15][2] = 1; stateT[15][3] = 1; stateT[15][4] = 1; stateT[15][5] = 1; stateT[15][6] = 1; stateT[15][7] = 1;
        stateT[16][0] = 1; stateT[16][1] = 1; stateT[16][2] = 1; stateT[16][3] = 1; stateT[16][4] = 1; stateT[16][5] = 1; stateT[16][6] = 1; stateT[16][7] = 1;
    }
    
    public FSATable(String fname) throws FileNotFoundException
    {
        File csv = new File(fname);
        Scanner scan = new Scanner(csv);
        scan.useDelimiter(",");
        stateT = new int[ROWS][COLS];

            
            for(int i = 0; i < COLS-1; i++) {
                String[] line = scan.nextLine().split(",");
                for (int j=0; j < line.length; j++) {
                    stateT[i][j] = Integer.parseInt(line[j].trim());
                }
                scan.nextLine();
            }
        

        
        scan.close();
    }

    public int next_state(int curr, char ch) {
        
        switch (curr)
        {
            case 0: return stateT[0][token_to_index(ch)];
            case 1: return 1;
            case 2: return 0;
            case 3: return stateT[3][token_to_index(ch)];
            case 4: return 0;
            case 5: return stateT[5][token_to_index(ch)];
            case 6: return 0;
            case 7: return stateT[7][token_to_index(ch)];
            case 8: return stateT[8][token_to_index(ch)];
            case 9: return stateT[9][token_to_index(ch)];
            case 10: return 0;
            case 11: return stateT[11][token_to_index(ch)];
            case 12: return 0;
            case 13: return 0;
            case 14: return stateT[14][token_to_index(ch)];
            case 15: return 0;
            case 16: return 0;
            default: return 1;
        }
    }

    public static int token_to_index(char ch) {
        if(Character.isAlphabetic(ch)) {
            return 0;
        }
        else if(Character.isDigit(ch)) {
            return 1;
        }
        else if(ch == '*') {
            return 2;
        }
        else if(ch == '/') {
            return 3;
        }
        else if(ch == '=') {
            return 4;
        }
        else if(ch == '<') {
            return 5;
        }
        else if(Character.isWhitespace(ch)) {
            return 6;
        }
        else {
            return 7;
        }
    }

    public void printT() {
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLS; j++) {
                System.out.print(" " + stateT[i][j]);
            }
            System.out.println();
        }
    }
        
}
