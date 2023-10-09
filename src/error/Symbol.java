package error;

public class Symbol {
    private String name;
    private int lineNum;

    public Symbol(String name, int lineNum) {
        this.name = name;
        this.lineNum = lineNum;
    }

    public String getName() {
        return name;
    }
}
