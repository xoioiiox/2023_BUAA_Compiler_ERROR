package parser.expression;

public class PrimaryExp {
    private Exp exp;
    private LVal lVal;
    private Number number;

    public PrimaryExp(Exp exp) {
        this.exp = exp;
    }

    public PrimaryExp(LVal lVal) {
        this.lVal = lVal;
    }

    public PrimaryExp(Number number) {
        this.number = number;
    }

    public int getDimension() {
        if (exp != null) {
            return exp.getDimension();
        }
        else if (lVal != null) {
            return lVal.getDimension();
        }
        else {
            return 0; //int
        }
    }
}
