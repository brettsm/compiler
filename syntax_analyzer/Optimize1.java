import java.util.*;
import java.io.*;
public class Optimize1 {
    ArrayList<String[]> quadTable;
    public Optimize1() {
        this.quadTable = new ArrayList<String[]>();
        Scanner in = new Scanner("Quads.txt");
        while(in.hasNextLine()) {
            quadTable.add(in.nextLine().split(","));
        }
    }

    public void optimize() throws FileNotFoundException, IOException {
        String[] quad1, quad2, write;
        FileWriter out = new FileWriter(new File("opquads.txt"));
        for(int r = 0; r < this.quadTable.size();  r++) {
            quad1 = quadTable.get(r);
            for(int r2 = r + 1; r < quadTable.size(); r2++) {
                quad2 = quadTable.get(r2);
            }
        }

    }
}
