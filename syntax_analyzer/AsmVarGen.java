import java.util.*;
import java.io.*;
public class AsmVarGen {
    public AsmVarGen() {

    }

    public void makeVars(String in, String data, String bss) throws FileNotFoundException, IOException {
        Scanner sTable = new Scanner(new File(in));
        Scanner dataScan = new Scanner(new File(data));
        Scanner bssScan = new Scanner(new File(bss));
        Scanner myScan = new Scanner(new File("myAssemble.txt"));
        Scanner lastScan = new Scanner(new File("lastasm.txt"));
        FileWriter finish = new FileWriter(new File("totalasm.asm"));
        String[] line = null;

        while(dataScan.hasNextLine()) {
            finish.write(dataScan.nextLine());
            finish.write("\n");
        }
        //END WHILE;
        dataScan.close();
        while(sTable.hasNextLine()) {
            line = sTable.nextLine().split(",");
            if(line[1].equals("Constvar")) {
                finish.write("\n\t" + line[0] + "\tDW\t" + line[2] + "\n");
            } else if(line[1].equals("NumLit")) {
                finish.write("\n\t" + line[0] + "\tDW\t" + line[2] + "\n");
            }
            //end if;
        }
        //END WHILE;
        sTable.close();
        //reset feed for symbol table file
        sTable = new Scanner(new File(in));
        while(bssScan.hasNextLine()) {
            finish.write(bssScan.nextLine());
            finish.write("\n");
        }
        //END WHILE;
        bssScan.close();
        while(sTable.hasNextLine()) {
            line = sTable.nextLine().split(",");
            if(line[1].equals("Var")) {
                finish.write("\n\t" + line[0] + "\tRESW\t1" + "\n");
            } else if(line[1].equals("$temp")) {
                finish.write("\n\t" + line[0] + "\tRESW\t1" + "\n");
            }
        }
        //END WHILE;
        sTable.close();
        finish.write("\n\n");
        finish.write("\tglobal _start\n\n");
        finish.write("section .text\n\n");
        finish.write("_start:\n\n");

        finish.write("\n");
        while(myScan.hasNextLine()) {
            finish.write(myScan.nextLine());
            finish.write("\n");
        }
        finish.write("\n");
        myScan.close();

        while(lastScan.hasNextLine()) {
            finish.write(lastScan.nextLine());
            finish.write("\n");
        }

        lastScan.close();
        finish.close();
    }
}
