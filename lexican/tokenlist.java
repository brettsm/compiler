import java.util.*;

public class tokenlist {
    private ArrayList<token> alist;

    public tokenlist(token t) {
        this.alist = new ArrayList<token>();
        alist.add(t);
    }
    public tokenlist() {
        this.alist = new ArrayList<token>();
    }

    public void printTokenList() {
        for(int i = 0; i < this.alist.size(); i++) {
            System.out.print(alist.get(i).getValue() + " " + alist.get(i).getType());
            System.out.println();
        }
    }

    public void addToken(token t) {
        this.alist.add(t);
    }

    public String getValue(int i) throws IndexOutOfBoundsException {
        return this.alist.get(i).getValue();
    }

    public token getToken(int i) throws IndexOutOfBoundsException {
        return this.alist.get(i);
    }

    public String getType(int i) throws IndexOutOfBoundsException {
        return this.alist.get(i).getType();
    }
    
}