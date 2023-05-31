//Brett Smith
//Brett Smith
//Sun Apr 23
//====================================================================================================================

import java.util.*;
import java.io.*;

public class SyntaxAnalyzer {
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

    public SyntaxAnalyzer(String tlName, String stName, String ptName) throws FileNotFoundException {
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
        PDA.push(new Token("$", "$start_sym"));
        boolean justpopped = false;
        boolean pushedRelOrAssign = false;
        String[] line;
        QuadsTable quadsT = new QuadsTable();
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

            if(previous.val.equals("$") && next.val.equals("}")) {
                next = new Token("`","$end");
            }

            System.out.println("Token in stack: " + previous.val);
            System.out.println("Next token: " + next.val);

            if(previous.type.equals("<lb>") && next.type.equals("$semi")) {
                line = tokenList.nextLine().split(" ");
                next = new Token(line[0], line[1]);
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

                System.out.println( previous.val + " " + precedenceTable.getRel(previous,next) + " " + next.val);
                if(precedenceTable.getRel(previous, next).equals("<") || precedenceTable.getRel(previous, next).equals("=")) {
                    System.out.println("Add " + next.val + " to stack");
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
                        System.out.println("AFTER POPPING BRACES: ");
                        for(Token i : PDA) {
                            System.out.println(i.val);
                        }
                        System.out.println("previous now: " + previous.val);
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
                        System.out.println("AFTER POPPING IFTHEN: ");
                        for(Token t : PDA) {
                            System.out.println(t.val);
                        }
                        System.out.println("VALUE OF NEXT AFTER POPPING IF THEN");
                        
                        if(!tokenList.hasNextLine()) {
                            next = new Token("`", "$end");
                        }
                        continue;
                    }
                    //END IF;

                    if(next.type.equals("$Then")) {
                        fixUpLabeler = gen.generateThen(jumpType);
                        fixUp.push(fixUpLabeler);
                        System.out.println("GENERATING LABEL NOP");
                    } else if(next.type.equals("$Do")) {
                        endFixup.push(gen.generateDo(jumpType));
                        System.out.println("PUSHED DO LABEL");
                    } 
                    //END IF;


                    previous = next;
                    System.out.println("Previous now : " + previous.val);
                    next = null;
                    //UPDATED CORRECTLY

                    
                } else if(precedenceTable.getRel(previous, next).equals(">")) {

                    if(previous.type.equals("$Get")) {
                        gen.writeRead(PDA.pop().val);
                        PDA.pop();
                        previous = getTopOp(PDA);
                        continue;
                    }
                    if(previous.type.equals("$Print")) {
                        gen.writePrint(PDA.pop().val);
                        PDA.pop();
                        previous = getTopOp(PDA);
                        continue;
                    }
                    if(previous.type.equals("$Call")) {
                        gen.writeCall(PDA.pop().val);
                        PDA.pop();
                        previous = getTopOp(PDA);
                        continue;
                    }
                    if(previous.type.equals("$Return")) {
                        gen.writeRet();
                        PDA.pop();
                        previous = getTopOp(PDA);
                        continue;
                    }
                    if(previous.type.equals("$Procedure")) {
                        gen.writeMethodEnd();
                        PDA.pop();
                        PDA.pop();
                        previous = getTopOp(PDA);
                        continue;
                    }

                    if(next.type.equals("$rp")) {

                        //THEN POP THE STACK FOR BOTH PARENS
                        System.out.println("VALUE OF PREVIOUS: " + previous.val + " VALUE OF NEXT: " + next.val);
                        System.out.println("POPPING PARENS");
                        System.out.println("2 POPPing: " + PDA.peek().val);
                        System.out.println("THE STACK BEGINNING: " );
                        for(Token t : PDA) {
                            System.out.print(t.val + ", ");
                        }
                        System.out.println();
                        while(!PDA.isEmpty() && !PDA.peek().val.equals("(")) { //POP THE TOKENS BEFORE ( SO THAT WE CAN GET RID OF (
                            System.out.println("3 POPPING " + PDA.peek().val);
                            parenStack.push(PDA.pop()); //STORE IN STACK TO BE PUSHED BACK IN
                        }
                        System.out.println("REACHED: " + PDA.peek().val);
                        if(!PDA.isEmpty())
                            System.out.println("4 POPPING: " + PDA.peek().val);
                            //PDA.pop(); //POP THE (
                        System.out.println("THE STACK after popping: ");
                        for(Token t : PDA) {
                            System.out.print(t.val + ", ");
                        }
                        System.out.println("");
                        while(!parenStack.isEmpty()) {
                            PDA.push(parenStack.pop()); //PUSH THE POPPED TOKENS BACK IN
                        }
                        System.out.println("THE STACK AFTER PUSHING AGAIN:");
                        for(Token t : PDA) {
                            System.out.print(t.val + ", ");
                        }
                        System.out.println();
                        System.out.println("VAL NEXT: " + next.val);
                        previous = getTopOp(PDA); //PREVIOUS NOW EQUAL TO THE TOP OPERATOR OF THE STACK
                        System.out.println("AFTER GETTIng top op: ");
                        for(Token t : PDA) {
                            System.out.print(t.val + ", ");
                        }
                        System.out.println();
                        //^^NOT SURE IF IT SHOULD?
                        //SHOULD PREVIOUS BE UPDATED AFTER POPPING THE PARENTHESES?
                            //IT SHOULD BE UPDATED AFTER THE OUTPUT QUEUE STUFF
                        System.out.println("ADDING 1" + next.val);
                        //PDA.push(next);
                        
                        line = tokenList.nextLine().split(" ");
                        next = new Token(line[0], line[1]);
                        System.out.println("PREVIOUS2: " + previous.val);
                        //poppedParens = true;
                        
                        //continue;
                    }
                    System.out.println("PDA before output queue");
                    for(Token t : PDA) {
                        System.out.print(t.val + ", ");
                    }
                    System.out.println();
                    do {
                        
                        if(PDA.peek().val.equals("$")) {
                            break;
                        }
                        System.out.println("adding " + PDA.peek().val + " to the output queue!!!!!!!!!!!!!!!!");
                        testQueue.add(PDA.pop());
                        temp = PDA.peek();
                        System.out.println("========================");
                        for(Token t : PDA) {
                            System.out.print(t.val);
                        }
                        System.out.println();
                        System.out.println("Temp " + temp.val);
                        if(!isOperator(temp.val)){
                            System.out.println("adding " + PDA.peek().val + " to the output queue");
                            testQueue.add(PDA.peek());
                            outputQueue.add(PDA.pop());
                            temp = getTopOp(PDA);
                        } else if(  temp.val.equals("=") || temp.val.equals("<") || temp.val.equals(">") || temp.val.equals("<=") || 
                                    temp.val.equals(">=") || temp.val.equals("==") || temp.val.equals("!=")) {
                                        pushedRelOrAssign = true;
                                    }
                        //end if
                        System.out.println( previous.val + " " + precedenceTable.getRel(previous,next) + " " + next.val);
                        System.out.println("Temp: " + temp.val);
                    } while(!precedenceTable.getRel(temp,previous).equals("<"));
                    if(temp.val.equals("(")) {
                        PDA.pop();
                    }
                    
                    previous = getTopOp(PDA);
                    System.out.println("PREVIOUS ============= " + previous.val);
                    System.out.println("Previous now 3: " + previous.val);
                    temp = new Token("$","$start_sym");
                    justpopped=true;
                    outputQueue.add(popDelim);
                    testQueue.add(popDelim);

                    
                    gen.generateQuad(testQueue); //THIS WILL DEFINITELY NOT WORK NEEDS MORE WORK BUT NO TIME
                    testQueue = new LinkedList<Token>();
                    if(!pushedRelOrAssign) {
                        l = new Token(label.genLabel(), "label");
                        PDA.push(l);
                    }
                    pushedRelOrAssign = false;
                    //line = tokenList.nextLine().split(" ");
                    System.out.println("NExT========= " + next.val);
                    testQueue = new LinkedList<Token>();
                

                    //add section here to generate code for the pop maybe
                } else {
                    System.out.println("ERROR: no relation between the operators");
                    for(Token i : PDA) {
                        System.out.println(i.val);
                    }
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
                        System.out.println("Skipped: " + next.val);
                        line = tokenList.nextLine().split(" ");
                        next = new Token(line[0], line[1]);
                    }
                    System.out.println("Skipped: " + next.val);
                } else {
                    System.out.println("ADDED " + next.val);
                    PDA.push(next);
                    for(Token t : PDA) {
                        System.out.print(t.val + ", ");
                    }
                    System.out.println();
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