public class LabelGenerator {
    private int count;

    public LabelGenerator() {
        this.count = 1;
    }

    public String genLabel() {
        String label = "T" + count;
        count++;
        return label;
    }

    public void reset() {
        this.count = 1;
    }
}