public class Quad {
    String op;
    String arg1;
    String arg2;
    String arg3;

    public Quad(String o, String a1, String a2, String a3) {
        this.op = o;
        this.arg1 = a1;
        this.arg2 = a2;
        this.arg3 = a3;
    }

    public void printQuad() {
        System.out.println(this.op + "," + this.arg1 + "," + this.arg2 + "," + this.arg3);
    }
}
