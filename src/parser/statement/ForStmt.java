package parser.statement;

import parser.expression.Exp;
import parser.expression.LVal;

public class ForStmt {
    LVal lVal;
    Exp exp;

    public  ForStmt(LVal lVal, Exp exp) {
        this.lVal = lVal;
        this.exp = exp;
    }
}
