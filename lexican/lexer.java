//Brett Smith
//You don't have permission to use this code for compiler class..
//I just need a VCS, so I am uploading it to git

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
        char skipped;
            //stores a skipped character
        String val = "";
        String type = "";
        token next_token = new token();
            //will use one token object to add to tokenlist object
        tokenlist tlist = new tokenlist();
            //use tlist.addToken(state_t.processInput(r)) to add to list
        boolean fin_state = false;
            //flag to determine if we are in a final state
        int i_char;
            //bufferedreader, read in next char as int (need to convert to char (c_char))
        char c_char;
            
        while((i_char = instream.read()) != -1 ) {
            
            
            c_char = (char)i_char;
            System.out.println("current char: " + c_char);
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
                    break;
                case 3:
                    val = val + c_char;
                    break;
                case 4:
                    fin_state = true;
                    type = "<int>";
                    break;
                case 5:
                    val = val + c_char;
                    type = "<var>";
                    System.out.println("Val = " + val);
                    break;
                case 6:
                    type = "<var>";
                        
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
                    
                    break;
                case 11:
                    val = val + c_char;
                    break;
                case 12:
                    fin_state = true;
                    type = "assignment";
                        
                    break;
                case 13:
                    fin_state = true;
                    type = "<relop>";
                    
                    break;
                case 14:
                    val = val + c_char;
                case 15:
                    fin_state = true;
                    type = "<relop>";
                        
                    break;
                case 16:
                    fin_state = true;
                    type = "<relop>";
                        
                    break;
                default:
                    System.out.println("FSA.nextState isnt working correctly");
                    break;
            }

            

            if(fin_state) {
            //if reached a final state add token to list, reset variables, process skipped character
                next_token = new token(val, type);
                tlist.addToken(next_token);
                System.out.println("added token: " + next_token.getValue() + " " + next_token.getType());
                val = "";
                type = "";
                skipped = c_char;
                System.out.println("Skipped: " + skipped);
                current_state = state_t.nextState(0, skipped);
                fin_state = false;
                
                switch(current_state) {
                    case 0: 
                        break;
                    case 1:
                        System.out.println("ERROR: UNIDENTIFIABLE TOKEN");
                        break;
                    case 2:
                        fin_state = true;
                        type = "<mop>";
                        val = "*";
                        break;
                    case 3:
                        val = val + c_char;
                        break;
                    case 4:
                        fin_state = true;
                        type = "<int>";
                        break;
                    case 5:
                        val = val + c_char;
                        type = "<var>";
                        System.out.println("Val = " + val);
                        break;
                    case 6:
                        type = "<var>";
                            
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
                        
                        break;
                    case 11:
                        val = val + c_char;
                        break;
                    case 12:
                        fin_state = true;
                        type = "assignment";
                            
                        break;
                    case 13:
                        fin_state = true;
                        type = "<relop>";
                        
                        break;
                    case 14:
                        val = val + c_char;
                    case 15:
                        fin_state = true;
                        type = "<relop>";
                            
                        break;
                    case 16:
                        fin_state = true;
                        type = "<relop>";
                            
                        break;
                    default:
                        System.out.println("FSA.nextState isnt working correctly");
                        break;
                }
                if(fin_state) {
                        next_token = new token(val, type);
                        tlist.addToken(next_token);
                        System.out.println("added token: " + next_token.getValue() + " " + next_token.getType());
                        val = "";
                        type = "";
                        current_state = 0;
                        fin_state = false;
                }
                
                

            }
        }

        next_token = new token(val, type);
        tlist.addToken(next_token);
        System.out.println("added token: " + next_token.getValue() + " " + next_token.getType());
            //add the token before the input stream ended to token list
        instream.close();
        tlist.printTokenList();

        System.out.println("PROGRAM END");
        System.out.println();
    }



}
