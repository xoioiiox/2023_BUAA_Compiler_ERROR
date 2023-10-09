package error;

public class SymbolCon extends Symbol{
    private int dimension;

    public SymbolCon(String name, int lineNum, int dimension) {
        super(name, lineNum);
        this.dimension = dimension;
    }

    public int getDimension() {
        return dimension;
    }
}
