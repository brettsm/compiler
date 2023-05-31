//Brett Smith
//Tue Apr 11, 2023

import java.io.*;
import java.util.*;
public class QuadsGen {
    //input: outputQueue from SyntaxAnalyzer.java

    LabelGenerator gen = new LabelGenerator();
    int label;

    FileWriter quadOut;
    public QuadsGen() throws FileNotFoundException, IOException {
        this.quadOut = new FileWriter(new File("Quads.txt"));
        this.label = 0;
    }


    //if(next.val.equals(null) && the two operators in the stack are equal) => pop the operators in the stack?
    public boolean generateQuad(Queue<Token> out) throws IOException{
        Quad r = new Quad("+", "t", "t", "t");
        Stack<String> operandStack = new Stack<String>();
        Stack<String> operatorStack = new Stack<String>();
        String next = "", op = "", arg1 = "", arg2 = "", result = "";
        boolean tempUsed = true;
       
        
        while(!out.isEmpty()) {
            next = out.poll().val;
            
            if(next.equals("~")) { //end of pop cycle
                while(!operatorStack.isEmpty()) {
                    op = operatorStack.pop();
                    
                    if(op.equals("RETURN")) {
                        quadOut.write("RETURNH,_,_,_\n");
                        return tempUsed = false;
                    }
                    arg1 = operandStack.pop();
                    if(op.equals("CLASS")) {
                        
                        quadOut.write("CLASS," + arg1 + ",_,_\n");
                        return tempUsed = false;
                    } else if(op.equals("CALL")) {
                        quadOut.write("CALL," + arg1 + ",_,_\n");
                        return tempUsed = false;
                    } else if(op.equals("GET")) {
                        quadOut.write("READ," + arg1 + ",_,_\n");
                        return tempUsed = false;
                    } else if(op.equals("PROCEDURE")) {
                        quadOut.write("RETURN,_,_,_");
                        return tempUsed = false;
                    } else if(op.equals("PRINT")) {
                        quadOut.write("PRINT," + arg1 + ",_,_\n");
                         return tempUsed = false;
                    }
                    arg2 = operandStack.pop();
                    result = "-";
                }

                if( !op.equals("=") && !op.equals("<") && !op.equals(">") && !op.equals("<=") && !op.equals(">=") && !op.equals("!=") && !op.equals("==")) {
                    result = gen.genLabel();
                    tempUsed = true;
                } else {
                    tempUsed = false;
                }

                r.op = op;
                r.arg1 = arg1;
                r.arg2 = arg2;
                r.arg3 = result;
                r.printQuad();
                quadOut.write(r.op + "," + r.arg1 + "," + r.arg2 + "," + r.arg3 + "\n");
                
                
                return tempUsed;
                
            } else if(isOperator(next)) {
                if(next.equals("(") || next.equals(")")) {
                    continue;
                } else {
                    
                    operatorStack.push(next);
                }
                //end if
            } else {
                
                operandStack.push(next);
            }
            //end if

        }
        //end while
        
        
        return tempUsed;
    }

    public String generateThen(String op) throws IOException {
        String l = "L" + (++label);
        quadOut.write("THEN" + "," + l + "," + op + "\n");
        return l;
    }
    public String generateElse() throws IOException {
        String l = "L" + (++label);
        quadOut.write("ELSE," + l + ",jmp,_" + "\n");
        return l;
    }

    public String generateDo(String op) throws IOException {
        String l = "L" + (++label);
        quadOut.write("DO" + "," + l + "," + op + "\n");
        return l;
    }

    public void writeRead(String op) throws IOException {
        quadOut.write("READ," + op.trim() + " ,_,_\n");
    }

    public void writePrint(String op) throws IOException {
        quadOut.write("PRINT," + op.trim() + ",_,_\n");
    }
    public void writeLabel(String lab) throws IOException {
        quadOut.write("L" + "," + lab.trim() + "\n");
    }
    public void writeWLabel(String lab) throws IOException {
        quadOut.write("WHILE" + "," + lab + "\n");
    }
    public void writeCall(String lab) throws IOException {
        quadOut.write("CALL," + lab.trim() + "\n");
    }
    public void writeProc(String name) throws IOException {
        quadOut.write("PROCEDURE," + name + ",_,_\n");
    }
    public void writeRet() throws IOException {
        quadOut.write("RETURNH,_,_,_\n");
    }
    public void writeMethodEnd() throws IOException {
        quadOut.write("RETURN,_,_,_");
    }

    public void writeIf() throws IOException {
        quadOut.write("IF,_,_,_\n");
    }
    public void resetGen() {
        this.gen.reset();
    }

    public void writeJmp(String in) throws IOException {
        quadOut.write("W," + in + "\n");
    }
    public void closeFile() throws IOException {
        quadOut.close();
    }

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

    
}
