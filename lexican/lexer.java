//Brett Smith
//You don't have permission to use this code for compiler class..
//I just need a VCS, so I am uploading it to git

import java.util.*;
import java.io.*;

//right now 2/26 i am having trouble getting it to read the character it left off on
//try to put some print statements and figure it out
public class lexer {
    public static void main(String[] args) throws IOException {
        FSA state_t = new FSA("lexFSA.txt");
        state_t.printTable();
        int current_state = 0;
        Reader r = new FileReader("excode.txt");
        BufferedReader instream = new BufferedReader(r);
            //using BufferedReader to mark input stream and go back to character we left off on when we come accross a final state
        String val;
        String type;
        token next_token = new token();
            //will use one token object to add to tokenlist object
        tokenlist tlist = new tokenlist();
            //use tlist.addToken(state_t.processInput(r)) to add to list
        boolean fin_state;
            //flag to determine if we are in a final state
        boolean has_mark = false;
        int i_char;
            //bufferedreader, read in next char as int (need to convert to char (c_char))
        char c_char;
            
        while((i_char = instream.read()) != -1 ) {
            val = "";
            type = "";
            current_state = 0;
            fin_state = false;
            if(has_mark) {
                instream.reset();
                i_char = instream.read();
              }
                //resets back to character we left off on
            do {
                c_char = (char)i_char;
                current_state = state_t.nextState(current_state, c_char);
                System.out.println("current state: " + current_state);
                switch(current_state) {
                    case 0: 
                        break;
                    case 1:
                        System.out.println("ERROR: UNIDENTIFIABLE TOKEN");
                        break;
                    case 2:
                        fin_state = true;
                        type = "<mop>";
                        instream.mark(2);
                        has_mark = true;
                        break;
                    case 3:
                        val = val + c_char;
                        break;
                    case 4:
                        fin_state = true;
                        type = "<int>";
                        instream.mark(2);
                        has_mark = true;
                            //need to mark it back 1 somehow... problem for later
                        break;
                    case 5:
                        val = val + c_char;
                        System.out.println("Val = " + val);
                        break;
                    case 6:
                        type = "<var>";
                        instream.mark(2);
                        has_mark = true;
                        fin_state = true;
                        break;
                    case 7:
                        val = val + c_char;
                        break;
                    case 8:
                        break;
                    case 9:
                        break;
                    case 10:
                        fin_state = true;
                        type = "<mop>";
                        instream.mark(2);
                        has_mark = true;
                        break;
                    case 11:
                        val = val + c_char;
                        break;
                    case 12:
                        fin_state = true;
                        type = "assignment";
                        instream.mark(2);
                        has_mark = true;
                        break;
                    case 13:
                        fin_state = true;
                        type = "<relop>";
                        instream.mark(2);
                        has_mark = true;
                        break;
                    case 14:
                        val = val + c_char;
                    case 15:
                        fin_state = true;
                        type = "<relop>";
                        instream.mark(2);
                        has_mark = true;
                        break;
                    case 16:
                        fin_state = true;
                        type = "<relop>";
                        instream.mark(2);
                        has_mark = true;
                        break;
                    default:
                        System.out.println("nextState isnt working correctly");
                        break;
                }

                i_char = instream.read();
            } while(!fin_state && i_char != -1);

            if(!fin_state) { //handles last letter of file maybe
                switch(current_state) {
                    case 5: //LETTER
                        type = "<var>";
                        break;
                    case 3: //VAR
                        type = "<int>";
                        break;
                    default:
                        break;
                }
            }
            next_token = new token(val, type);
            tlist.addToken(next_token);
            System.out.println("added token: " + next_token.getValue() + " " + next_token.getType());
        }
        instream.close();
        tlist.printTokenList();
    }


}
