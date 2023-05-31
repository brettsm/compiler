//Brett Smith
//Brett Smith
//Sun Apr 23
//====================================================================================================================

import java.util.*;
import java.io.*;

public class SyntaxAnalyzer2 {
    Scanner inTL; //scanner for token list
    Scanner inST; //scanner for symbol table
    Scanner inPT; //scanner for precedence table
    Stack<Token> PDA;
    ArrayList<Symbol> symbolTable;
    PrecedenceTable precedenceTable;
    Queue<Token> outputQueue;
    String temp;
    Token token;
    LabelGenerator label;

    public SyntaxAnalyzer2(String tlName, String stName, String ptName) throws FileNotFoundException {
        this.inTL = new Scanner(new File(tlName));
        this.inST = new Scanner(new File(stName));
        this.inPT = new Scanner(new File(ptName));
        this.PDA = new Stack<Token>();
        this.symbolTable = new ArrayList<Symbol>();
        this.precedenceTable = new PrecedenceTable(ptName);

        Symbol sym;
        String[] l;
        while(inST.hasNextLine()) {
            l = inST.nextLine().split(",");
            sym = new Symbol(l[0], l[1], l[2], l[3], l[4]);
            symbolTable.add(sym);
        }

        String[] line;
        int rows = 0, cols = 0;
        int r;

        while(inPT.hasNextLine()) {
            line = inPT.nextLine().split(",");
            cols = line.length;
            rows++;
        }

        this.outputQueue = new LinkedList<Token>();
        this.temp = "T";
        this.token = null;
        this.label = new LabelGenerator();

    }

    //iterate through token list, read token, process stack, quad generation maybe
    //may want to change return type to QuadsTable so that I can generate the quads
    // in this function. 
    public void parse(Scanner tokenList) throws FileNotFoundException, IOException {
        Token popDelim = new Token("~","$pop_delim");//used to mark the end of a pop
        Token next = new Token("$", "$start_sym");
        Token temp = new Token("$", "$start_sym"); //comparison when popping
        Token previous = new Token("$", "$start_sym");
        Token l;
        Stack<Token> parenStack = new Stack<Token>();
        Stack<Token> tempStack = new Stack<Token>();
        Stack<String> fixUp = new Stack<String>();
        Stack<String> startWhile = new Stack<String>();
        Stack<String> endFixup = new Stack<String>();
        Stack<String> endStack = new Stack<String>();
        PDA.push(new Token("$", "$start_sym"));
        boolean justpopped = false;
        boolean pushedRelOrAssign = false;
        boolean makeTemp = true;
        String[] line;
        QuadsGen gen = new QuadsGen();
        Queue<Token> testQueue = new LinkedList<Token>();
        String jumpType = "jle";
        String fixUpLabeler = "-";
        String wLabel = "-";
        int numWhiles = 0;
        

        while(tokenList.hasNextLine() || (PDA.size() > 1) && !(previous.val.equals("$") && next.val.equals("`"))) {
            
            if(tokenList.hasNextLine() && !justpopped){
                line = tokenList.nextLine().split(" ");
                next = new Token(line[0], line[1]);
            } 
            //endif
            
            justpopped = false;
            
            if(previous.val.equals(";") && !(next == null)) {
                PDA.pop();
                previous = getTopOp(PDA);
                label.reset();
                gen.resetGen();
            }
            //END IF;

            if(previous.type.equals("$Then") || previous.type.equals("$Do")) {
                label.reset();
                gen.resetGen();
            }

            if(previous.val.equals("$") && next.val.equals("}") && !tokenList.hasNextLine()) {
                next = new Token("`","$end");
            }

            
            
            if(isOperator(next.val)) {
                //if previous yields or eq to next, push
                //if previous takes, pop until previous yields or eq
                if(next.val.equals("<")) {
                    jumpType = "JGE";
                } else if(next.val.equals(">")) {
                    jumpType = "JLE";
                } else if(next.val.equals("<=")) {
                    jumpType = "JG"; 
                } else if(next.val.equals(">=")) {
                    jumpType = "JL"; 
                } else if(next.val.equals("!=")) {
                    jumpType = "JE";
                } else if(next.val.equals("==")) {
                    jumpType = "JNE";
                }
                // END IF;
                System.out.println(previous.val + " " + precedenceTable.getRel(previous, next) + " " + next.val);

                
                if(precedenceTable.getRel(previous, next).equals("<") || precedenceTable.getRel(previous, next).equals("=")) {
                    // If reached a point where ( = ), then get rid of the parens out of the PDA
                    // without removing or changing the value(s) between them
                    if(next.type.equals("$Else") && previous.type.equals("$Then")) {
                        
                        endStack.push(gen.generateElse());
                        gen.writeLabel(fixUp.pop());
                        PDA.push(next);
                        previous = getTopOp(PDA);
                        continue;
                    }
                    if(next.type.equals("$rp") && precedenceTable.getRel(previous,next).equals("=")) {
                        while(!PDA.isEmpty() && !PDA.peek().val.equals("(")) { //POP THE TOKENS BEFORE ( SO THAT WE CAN GET RID OF (
                            parenStack.push(PDA.pop()); //STORE IN STACK TO BE PUSHED BACK IN
                        } 
                        //Pop the stack until reach (, and push values into a temp to push back in

                        if(!PDA.isEmpty())
                            PDA.pop(); //POP THE (

                        while(!parenStack.isEmpty()) {
                            PDA.push(parenStack.pop()); //PUSH THE POPPED TOKENS BACK IN
                        }
                        
                        previous = getTopOp(PDA); //PREVIOUS NOW EQUAL TO THE TOP OPERATOR OF THE STACK
                        
                        continue;
                    }
                    PDA.push(next);
                    if(next.type.equals("$If")) {
                        gen.writeIf();
                    } else if(next.type.equals("$While")) {
                        wLabel = ("W" + (++numWhiles));
                        startWhile.push(wLabel);
                        gen.writeWLabel(wLabel);
                    } 
                    //END IF;
                    

                    //GET RID OF BRACES IF REACHED A POINT WHERE PREVIOUS IS { AND NEXT IS }
                    if(previous.type.equals("<lb>") && next.type.equals("<rb>")) {
                        gen.resetGen();
                        label.reset();
                        PDA.pop();
                        while(!PDA.peek().type.equals("<lb>")) {
                            tempStack.push(PDA.pop());
                        }
                        PDA.pop();

                        while(!tempStack.isEmpty()) {
                            PDA.push(tempStack.pop());
                        }
                        previous = getTopOp(PDA);

                        

                        //AFTER POPPING BRACES CHECK IF PREVIOUS WAS THEN OR DO
                        //THERE IS NO TOKEN THAT "DO" OR "THEN" TAKE PRECEDENCE OVER, SO 
                        //HAVE TO DO IT THIS WAY
                        if(previous.type.equals("$Then")) {
                            while(!PDA.peek().type.equals("$If")) {
                                PDA.pop();
                            }
                            PDA.pop();
                            previous = getTopOp(PDA);
                            //generate label
                            gen.writeLabel(fixUp.pop());
                            gen.resetGen();
                            label.reset();
                        } else if(previous.type.equals("$Do")) {
                            while(!PDA.peek().type.equals("$While")) {
                                PDA.pop();
                            }
                            PDA.pop();
                            previous = getTopOp(PDA);
                            gen.writeJmp(startWhile.pop());
                            gen.writeLabel(endFixup.pop());

                        }
                        
                        
                        if(!tokenList.hasNextLine()) {
                            next = new Token("`", "$end");
                        }
                        //END IF;
                        continue;
                    }
                    //END IF;

                    if(next.type.equals("$Then")) {
                        fixUpLabeler = gen.generateThen(jumpType);
                        fixUp.push(fixUpLabeler);
                        
                    } else if(next.type.equals("$Do")) {
                        endFixup.push(gen.generateDo(jumpType));
                        
                    } 
                    //END IF;


                    previous = next;
                    
                    next = null;
                    //UPDATED CORRECTLY

                    
                } else if(precedenceTable.getRel(previous, next).equals(">")) {

                    if(previous.type.equals("$Else")) {
                        gen.writeLabel(endStack.pop());
                    }
                    if(previous.type.equals("$Else") && next.type.equals("<rb>")) {

                    }
                   
                    do {
                        
                        if(PDA.peek().val.equals("$")) {
                            break;
                        }

                        
                       
                        testQueue.add(PDA.pop());
                        temp = PDA.peek();
                        
                        
                        if(!isOperator(temp.val)){
                            
                            testQueue.add(PDA.peek());
                            outputQueue.add(PDA.pop());
                            temp = getTopOp(PDA);
                        } else if(  temp.val.equals("=") || temp.val.equals("<") || temp.val.equals(">") || temp.val.equals("<=") || 
                                    temp.val.equals(">=") || temp.val.equals("==") || temp.val.equals("!=")) {
                                        pushedRelOrAssign = true;
                                    }
                        //end if
                        
                    } while(!precedenceTable.getRel(temp,previous).equals("<"));
                    
                    previous = getTopOp(PDA);
                    
                    temp = new Token("$","$start_sym");
                    justpopped=true;
                    outputQueue.add(popDelim);
                    testQueue.add(popDelim);

                    
                    makeTemp = gen.generateQuad(testQueue); 
                    testQueue = new LinkedList<Token>();
                    if(makeTemp) {
                        
                        l = new Token(label.genLabel(), "label");
                       
                        PDA.push(l);
                    }
                    pushedRelOrAssign = false;
                    
                    testQueue = new LinkedList<Token>();
                

                    //add section here to generate code for the pop maybe
                } else if(precedenceTable.getRel(previous, next).equals(".")) {
                    System.out.println("Previous: " + previous.val + " Next: " + next.val);
                    System.out.println("GOT: '" + next.val + "'");
                    System.out.println("WAS EXPECTING: ");
                    precedenceTable.getPossibleNext(previous.val);
                    break;
                }
                //end if
            }
            else {
                if(next.type.equals("Lit")) {
                    next.val = (next.type + next.val);
                }
                if(previous.type.equals("$Procedure")) {
                        gen.writeProc(next.val);
                }
                if(next.type.equals("$VARDec") || next.type.equals("$Const")) {
                    while(!next.type.equals("$semi")) {
                        
                        line = tokenList.nextLine().split(" ");
                        next = new Token(line[0], line[1]);
                    }
                    
                } else {
                    
                    PDA.push(next);
                    
                }

            }
            //end if
        }
        //end while
        gen.closeFile();
    }
    //end method parse


    //returns true if "in" is an operator recognized by the grammar
    public static boolean isOperator(String in) {
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

    
    public static Token getTopOp(final Stack<Token> PD) {
        Stack<Token> cop = new Stack<Token>();
        for(Token t : PD) {
            cop.push(t);
        }
        while(!cop.isEmpty()) {
            if(isOperator(cop.peek().val)) {
                return cop.peek();
            }
            cop.pop();
        }
        return null;
        
    }



}
