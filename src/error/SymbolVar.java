package error;

public class SymbolVar extends Symbol{
    private int dimension;

    public SymbolVar(String name, int lineNum, int dimension) {
        super(name, lineNum);
        this.dimension = dimension;
    }

    public int getDimension() {
        return dimension;
    }
}
