import java.io.*;
import java.util.*;


public class AsmGen {
    private Scanner quadScan;
    private FileWriter writeAsm;
    public AsmGen(String inname, String outname) throws FileNotFoundException, IOException {
        if(inname.equals(outname)) {
            System.out.println("ERROR: INFILE AND OUTFILE MUST BE DIFFERENT");
            return;
        } else {
            this.quadScan = new Scanner(new File(inname));
            this.writeAsm = new FileWriter(new File(outname));
        }
        //END IF;
    }

    public void makeAssembly() throws IOException {
        String[] line;
        while(quadScan.hasNextLine()) {
            line = quadScan.nextLine().split(",");

            switch(line[0].trim()) {
                case "+":
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tadd ax,[" + line[2] + "]\n");
                    writeAsm.write("\tmov [" + line[3] + "], ax\n");
                    break;
                case "-":
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tsub ax,[" + line[2] + "]\n");
                    writeAsm.write("\tmov [" + line[3] + "], ax\n");
                    break;
                case "*":
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tmul " + "word" + "[" + line[2] + "]\n");
                    writeAsm.write("\tmov [" + line[3] + "], ax\n");
                    break;
                case "/":
                    writeAsm.write("\tmov dx,0\n");
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tmov bx,[" + line[2] + "]\n");
                    writeAsm.write("\tdiv bx\n");
                    writeAsm.write("\tmov [" + line[3] + "], ax\n");
                    break;
                case "=":
                    writeAsm.write("\tmov ax,[" + line[2] + "]\n");
                    writeAsm.write("\tmov [" + line[1] + "], ax\n");
                    break;
                case "IF":
                    break;
                case ">":
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tcmp ax,[" + line[2] + "]\n");
                    break;
                case "<":
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tcmp ax,[" + line[2] + "]\n");
                    break;
                case ">=":
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tcmp ax,[" + line[2] + "]\n");
                    break;
                case "<=":
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tcmp ax,[" + line[2] + "]\n");
                    break;
                case "==":
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tcmp ax,[" + line[2] + "]\n");
                    break;
                case "!=":
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tcmp ax,[" + line[2] + "]\n");
                    break;
                case "THEN":
                    writeAsm.write("\t" + line[2] + "\t" + line[1] + "\n");
                    break;
                case "L":
                    writeAsm.write(line[1] + ":\tnop\n");
                    break;
                case "READ":
                    writeAsm.write("\tcall PrintString\n");
                    writeAsm.write("\tcall GetAnInteger\n");
                    writeAsm.write("\tmov ax,[ReadInt]\n");
                    writeAsm.write("\tmov [" + line[1] + "], ax\n");
                    break;
                case "PRINT":
                    writeAsm.write("\tmov ax,[" + line[1] + "]\n");
                    writeAsm.write("\tcall ConvertIntegerToString\n");
                    writeAsm.write("\tmov eax, 4\n");
                    writeAsm.write("\tmov ebx, 1\n");
                    writeAsm.write("\tmov ecx, Result\n");
                    writeAsm.write("\tmov edx, ResultEnd\n");
                    writeAsm.write("\tint 80h\n");
                    break;
                case "WHILE":
                    writeAsm.write(line[1] + ":");
                    break;
                case "DO":
                    writeAsm.write("\t" + line[2] + "\t" + line[1] + "\n");
                    break;
                case "W":
                    writeAsm.write("\tjmp " + line[1] + "\n");
                    break;
                case "CLASS":
                    writeAsm.write("fini:\n");
                    writeAsm.write("\tmov eax, 1\n");
                    writeAsm.write("\txor ebx, ebx\n");
                    writeAsm.write("\tint\t0x80");
                    break;
                case "PROCEDURE":
                    writeAsm.write("\n\n" + line[1] + ":\n");
                    break;
                case "RETURNH":
                    writeAsm.write("\tjmp ReturnHome\n");
                    break;
                case "RETURN":
                    writeAsm.write("ReturnHome:\n");
                    writeAsm.write("\tret\n");
                    break;
                case "CALL":
                    writeAsm.write("\tcall " + line[1] + "\n");
                    break;
                case "ELSE":
                    writeAsm.write("\t" + line[2] + " " + line[1] + "\n");
                    break;
                default:
                    break;
            }
        }
    }

    public void closeFile() throws IOException {
        this.writeAsm.close();
        this.quadScan.close();
    }
}
