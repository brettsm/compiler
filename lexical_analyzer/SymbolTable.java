//SymbolTable.java
//Brett Smith
//March 10, 2023
//Creates an empty table of Symbols to create the SymbolTable
//functions to add and return symbols from the symbol table
//===========================================================

import java.util.*;
import java.io.*;

public class SymbolTable {
    private ArrayList<Symbol> stable;
    private int codek = 0;
    private int datak = 0;

    public SymbolTable() {
        this.stable = new ArrayList<Symbol>();
    }

    public void addSymbol(Symbol s) {
        switch(s.getSeg()) {
            case "DS":
                s.setAddress(datak);
                this.datak+=2;
                break;
            case "CS":
                s.setAddress(codek);
                this.codek+=2;
                break;
        }
        
        stable.add(s);
    }

    public Symbol getSymbol(int i) {
        return stable.get(i);
    }

    public void printTable() {
        for(int i = 0; i < this.stable.size(); i++) {
            System.out.print(this.stable.get(i).getToken() + ", " + this.stable.get(i).getClassify() + ", ");
            System.out.print(this.stable.get(i).getVal() + ", " + this.stable.get(i).getAddress() + ", ");
            System.out.println(this.stable.get(i).getSeg());
        }
    }

    public void printTable(String fname) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(new File(fname)));
        for(int i = 0; i < this.stable.size(); i++) {
            out.write(stable.get(i).getToken() + "," + stable.get(i).getClassify() + "," + stable.get(i).getVal() + "," + stable.get(i).getAddress() + "," + stable.get(i).getSeg());
            out.newLine();
        }

        out.close();
    }

    public int getLength() {
        return this.stable.size();
    }

    public boolean exists(String in) {
        for(int i = 0; i < this.stable.size(); i++) {
            if(stable.get(i).getToken().equals(in)) {
                return true;
            }
        }
        return false;
    }   
}