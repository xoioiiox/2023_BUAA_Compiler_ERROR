package parser.expression;

public class Exp {
    AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    public int getDimension() {
        return this.addExp.getDimension();
    }
}
