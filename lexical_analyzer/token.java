//token.java
//Brett Smith
//March 10, 2023
//Defines token, or the information in a row of the token list
//============================================================
public class token {
    private String value;
    private String type;

    public token(String v, String t) {
        this.value = v;
        this.type = t;
    }

    public token() {
        this.value = null;
        this.type = null;
    }

    public String getValue() {
        return this.value;
    }

    public String getType() {
        return this.type;
    }
    public void setValue(String v) {
        this.value = v;
    }

    public void setType(String t) {
        this.type = t;
    }

}
