//Brett Smith
//Mar 10, 2023
//parser will build the token list and then 
//the symbol table from the source code
//recognizes java0
//=========================================================================

import java.io.*;

public class parser {
    public static void main(String[] args) throws IOException {
        System.out.println();
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
        boolean hasSkipped = false;
        int i_char;
            //bufferedreader, read in next char as int (need to convert to char (c_char))
        char c_char;

        FSASymbol sFSA = new FSASymbol();
        SymbolTable sTable = new SymbolTable();
        String symb = "", classify = "", seg = "";

        Symbol nextSym = new Symbol();
        Symbol tempSym = new Symbol();
        Symbol wLabel = new Symbol();
        int wLabelK = 0;
        Symbol lLabel = new Symbol();
        int lLabelK = 0;
        int symState = 0;

            
        while((i_char = instream.read()) != -1 ) {

            //While there are characters in the stream, process them
            //get next state given current state and input character
            //next state will be returned from the FSA.java table

            //Put the next state into a switch statement, which will 
            //do 1 of 5 different things:
            // 1) Just break.
            // 2) Add the current char to the token and break
            // 3) Signal that it is a final state, finalize token,
            //    break.
            // 4) Signal final state, finalize token, signal that 
            //    there has been a character skipped.
            // 5) Return an error.

            //(finalize token means to set type and add missing character)

            //If the program signals that it is in a final state, the 
            //current token (val) will be added to the token list.
            //If the hasSkipped boolean has been set to true, the 
            //program will put the skipped character (current val
            //of c_char) through the same case statement as before, and
            //then move on to the next character.
            //  ^ This is the way that I ensure the character stream picks
            //    up where it left off

            c_char = (char)i_char;
            
            current_state = state_t.nextState(current_state, c_char);

            switch(current_state) {
                case 0: 
                    break;
                case 1:
                    System.out.println("ERROR: UNIDENTIFIABLE TOKEN");
                    break;
                case 2:
                    fin_state = true;
                    val = val + c_char;
                    type = "<mop>";
                    break;
                case 3:
                    val = val + c_char;
                    break;
                case 4:
                    fin_state = true;
                    hasSkipped = true;
                    type = "Lit";
                    break;
                case 5:
                    val = val + c_char;
                    
                    break;
                case 6:
                    type = getVarType(val);
                    

                    hasSkipped = true;
                    fin_state = true;
                    break;
                case 7:
                    val = val + c_char;
                    break;
                case 8:
                    break;
                case 9:
                    val = "";
                    break;
                case 10:
                    fin_state = true;
                    type = "<mop>";
                    hasSkipped = true;
                    
                    break;
                case 11:
                    val = val + c_char;
                    break;
                case 12:
                    fin_state = true;
                    type = "<assignment>";
                        
                    break;
                case 13:
                    fin_state = true;
                    val = val + c_char;
                    type = "<relop>";
                    
                    break;
                case 14:
                    val = val + c_char;
                    break;
                case 15:
                    fin_state = true;
                    type = "<relop>";
                        
                    break;
                case 16:
                    fin_state = true;
                    val = val + c_char;
                    type = "<relop>";
                    break;
                case 17:
                    fin_state = true;
                    val = val + c_char;
                    type = "<lb>";
                    break;
                case 18:
                    fin_state = true;
                    val = val + c_char;
                    type = "<rb>";
                    break;
                case 19:
                    fin_state = true;
                    val = val + c_char;
                    type = "$addop";
                    break;
                case 20:
                    fin_state = true;
                    val = val + c_char;
                    type = "$semi";
                    break;
                case 21:
                    fin_state = true;
                    val = val + c_char;
                    type = "$comma";
                    break;
                case 22:
                    fin_state = true;
                    val = val + c_char;
                    type = "$lp";
                    break;
                case 23:
                    fin_state = true;
                    val = val + c_char;
                    type = "$rp";
                    break;
                case 24:
                    val = val + c_char;
                    break;
                case 25:
                    fin_state = true;
                    hasSkipped = true;
                    type = "<relop>";
                    break;
                case 26:
                    fin_state = true;
                    type = "<relop>";
                    val = val + c_char;
                    break;
                case 27:
                    val = val + c_char;
                    break;
                case 28:
                    fin_state = true;
                    type = "<relop>";
                    val = val + c_char;
                    break;
                case 29:
                    fin_state = true;
                    type = "<min_op>";
                    val = val + c_char;
                    break;

                default:
                    System.out.println("FSA.nextState isnt working correctly");
                    break;
            }

            

            if(fin_state) {
            //if reached a final state add token to list, reset variables, process skipped character if there has been one skipped
            
                next_token = new token(val, type);
                tlist.addToken(next_token);
                
                val = "";
                type = "";
                fin_state = false;
                current_state = 0;

                if(hasSkipped) {
                    skipped = c_char;
                    
                    current_state = state_t.nextState(0, skipped);
                    

                    switch(current_state) {
                        case 0: 
                            break;
                        case 1:
                            System.out.println("ERROR: UNIDENTIFIABLE TOKEN");
                            val = "";
                            break;
                        case 2:
                            fin_state = true;
                            type = "<mop>";
                            val = "*";
                            break;
                        case 3:
                            val = val + skipped;
                            break;
                        case 4:
                            fin_state = true;
                            type = "Lit";
                            break;
                        case 5:
                            val = val + skipped;
                            type = "<var>";
                            
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
                            val = val + skipped;
                            break;
                        case 12:
                            fin_state = true;
                            type = "<assignment>";
                            
                            break;
                        case 13:
                            fin_state = true;
                            type = "<relop>";
                        
                            break;
                        case 14:
                            val = val + skipped;
                            break;
                        case 15:
                            fin_state = true;
                            type = "<relop>";
                            
                            break;
                        case 16:
                            fin_state = true;
                            val = val + skipped;
                            type = "<relop>";
                            
                            break;
                        case 17:
                            fin_state = true;
                            type = "<lb>";
                            val = val + skipped;
                            break;
                        case 18:
                            fin_state = true;
                            val = val + skipped;
                            type = "<rb>";
                            break;
                        case 19:
                            fin_state = true;
                            val = val + skipped;
                            type = "$addop";
                            break;
                        case 20:
                            fin_state = true;
                            val = val + skipped;
                            type = "$semi";
                            break;
                        case 21:
                            fin_state = true;
                            val = val + skipped;
                            type = "$comma";
                            break;
                        case 22:
                            fin_state = true;
                            val = val + skipped;
                            type = "$lp";
                            break;
                        case 23:
                            fin_state = true;
                            val = val + skipped;
                            type = "$rp";
                            break;
                        case 24:
                            val = val + skipped;
                            break;
                        case 25:
                            fin_state = true;
                            type = "<relop>";
                            break;
                        case 26:
                            fin_state = true;
                            type = "<relop>";
                            val = val + skipped;
                            break;
                        case 27:
                            val = val + skipped;
                            break;
                        case 28:
                            fin_state = true;
                            type = "<relop>";
                            val = val + skipped;
                            break;
                        case 29:
                            fin_state = true;
                            type = "<min_op>";
                            val = val + c_char;
                            break;

                        default:
                            System.out.println("FSA.nextState isnt working correctly");
                            break;
                    }   
                    if(fin_state) {
                            next_token = new token(val, type);
                            tlist.addToken(next_token);
                            
                            val = "";
                            type = "";
                            current_state = 0;
                            fin_state = false;
                            
                    }
                    hasSkipped = false;
                }
                
                

            }
        }

        next_token = new token(val, type);
        tlist.addToken(next_token);
            //add the last token to the token list
        instream.close();
        tlist.printTokenList("TokenList.txt");
        //prints the token list to "TokenList.txt"


        //create SymbolTable
        //go through token list and use that information to create symbol table
        //pretty much the same idea as creating the token list, except it
        //uses a different DFSA and uses the classes of the token list as input


        for(int i = 0; i < tlist.getLength() - 1; i++) {
            nextSym = new Symbol();
            nextSym.setToken(tlist.getValue(i));
            nextSym.setClassify(tlist.getType(i));

           
            System.out.println(nextSym.getToken());
            

            symState = sFSA.nextState(symState, nextSym.getClassify());
            
            switch(symState){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    nextSym.setClassify("$pgmname");
                    nextSym.setSeg("CS");
                    
                    sTable.addSymbol(nextSym);
                    
                    break;
                case 3:
                    break;
                case 4:
                    classify = "Constvar";
                    break;
                case 5:
                    
                    symb = tlist.getValue(i);
                    break;
                case 6:
                    break;
                case 7:
                    tempSym = new Symbol();
                    tempSym.setToken(symb);
                    tempSym.setClassify(classify);
                    tempSym.setVal(tlist.getValue(i));
                    tempSym.setSeg("DS");
                    sTable.addSymbol(tempSym);

                    break;
                case 8:
                    classify = "Var";
                    seg = "DS";
                    break;
                case 9:
                    tempSym = new Symbol();
                    tempSym.setToken(tlist.getValue(i));
                    tempSym.setClassify(classify);
                    tempSym.setVal("?");
                    tempSym.setSeg(seg);
                    
                    sTable.addSymbol(tempSym);
                    break;
                case 10:
                    break;
                case 11:
                    tempSym = new Symbol();
                    tempSym.setVal(nextSym.getToken());
                    tempSym.setToken("Lit" + tempSym.getVal());
                    if(sTable.exists(tempSym.getToken())) {
                        break;
                    }
                    //END IF;
                    tempSym.setSeg("DS");
                    tempSym.setClassify("NumLit");
                    sTable.addSymbol(tempSym);
                    break;
                case 12:
                    wLabel = new Symbol();
                    wLabel.setToken("W" + (++wLabelK));
                    wLabel.setVal("-");
                    wLabel.setSeg("CS");
                    wLabel.setClassify("Label");
                    sTable.addSymbol(wLabel);
                    lLabel = new Symbol();
                    lLabel.setToken("L" + (++lLabelK));
                    lLabel.setVal("-");
                    lLabel.setSeg("CS");
                    lLabel.setClassify("Label");
                    sTable.addSymbol(lLabel);
                    break;
                case 13:
                    System.out.println("There was an error with either the table or format entered");
                    break;
                case 14:
                    lLabel = new Symbol();
                    lLabel.setToken("L" + (++lLabelK));
                    lLabel.setVal("-");
                    lLabel.setSeg("CS");
                    lLabel.setClassify("Label");
                    sTable.addSymbol(lLabel);
                    break;
                default:
                    break;
            }

        }
        //add temporary veriables to symbol table
        Symbol temp;
        for(int i = 1; i <= 7; i++) {
            temp = new Symbol();
            temp.setToken("T" + i);
            temp.setSeg("DS");
            temp.setClassify("$temp");
            temp.setVal("?");
            sTable.addSymbol(temp);
        }
        
        sTable.printTable("SymbolTable.txt");

        System.out.println("PROGRAM END");
        System.out.println();
    }

    public static String getVarType(String in) {
        if(in.equals("CONST")) {
            return "$Const";
        }
        else if(in.equals("IF")) {
            return "$If";
        }
        else if(in.equals("VAR")) {
            return "$VARDec";
        }
        else if(in.equals("THEN")) {
            return "$Then";
        }
        else if(in.equals("PROCEDURE")) {
            return "$Procedure";
        }
        else if(in.equals("WHILE")) {
            return "$While";
        }
        else if(in.equals("CALL")) {
            return "$Call";
        }
        else if(in.equals("DO")) {
            return "$Do";
        }
        else if(in.equals("ODD")) {
            return "$Odd";
        }
        else if(in.equals("CLASS")) {
            return "$Class";
        } else if (in.equals("PRINT")) {
            return "$Print";
        } else if(in.equals("GET")) {
            return "$Get";
        } else if(in.equals("RETURN")) {
            return "$Return";
        } else if(in.equals("ELSE")) {
            return "$Else";
        }else {
            return "<var>";
        }
        
        
    }
    public static boolean isNumeric(String str) { 
        try {  
          Double.parseDouble(str);  
          return true;
        } catch(NumberFormatException e){  
          return false;  
        }  
      }
}

