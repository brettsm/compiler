//Brett Smith

import java.io.*;

public class PassTwo {
    public static void main(String[] args) throws FileNotFoundException, IOException{

        SyntaxAnalyzer2 syntax2 = new SyntaxAnalyzer2("../lexical_analyzer/TokenList.txt","../lexical_analyzer/SymbolTable.txt", "Precedence.txt");
        AsmGen generateAssembly = new AsmGen("Quads.txt", "myAssemble.txt");
        AsmVarGen generateData = new AsmVarGen();
        syntax2.parse(syntax2.inTL);
        generateAssembly.makeAssembly();
        generateAssembly.closeFile();
        generateData.makeVars("../lexical_analyzer/SymbolTable.txt","dataasm.txt","bssasm.txt");

    }
}
