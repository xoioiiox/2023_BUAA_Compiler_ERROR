package parser.expression;

public class PrimaryExp {
    Exp exp;
    LVal lVal;
    Number number;

    public PrimaryExp(Exp exp) {
        this.exp = exp;
    }

    public PrimaryExp(LVal lVal) {
        this.lVal = lVal;
    }

    public PrimaryExp(Number number) {
        this.number = number;
    }
}
