package parser.statement;

import parser.expression.Exp;
import parser.expression.LVal;

public class StmtAssign extends Stmt {
    LVal lVal;
    Exp exp;

    public StmtAssign(LVal lVal, Exp exp) {
        this.lVal = lVal;
        this.exp = exp;
    }
}
