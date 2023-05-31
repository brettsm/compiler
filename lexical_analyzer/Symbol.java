//Symbol.java
//Brett Smith
//March 10, 2023
//Defines the "Symbol", or the information in a row of the
//Symbol Table.
//=========================================================
public class Symbol {
    private String token;
    private String classify;
    private String value;
    private int address;
    private String seg;

    public Symbol() {
        this.token = "";
        this.classify = "";
        this.value = "";
        this.address = 0;
        this.seg = "CS";
    }
    public Symbol(Symbol s) {
        this.token = s.getToken();
        this.classify = s.getClassify();
        this.value = s.getVal();
        this.address = s.getAddress();
        this.seg = s.getSeg();
    }

    public String getToken() {
        return token;
    }

    public String getClassify() {
        return classify;
    }

    public String getVal() {
        return value;
    }

    public int getAddress() {
        return address;
    }

    public String getSeg() {
        return seg;
    }

    public void setToken(String s) {
        token = s;
    }

    public void setClassify(String s) {
        classify = s;
    }

    public void setVal(String s) {
        value = s;
    }

    public void setAddress(int s) {
        address = s;
    }

    public void setSeg(String s) {
        seg = s;
    }
}