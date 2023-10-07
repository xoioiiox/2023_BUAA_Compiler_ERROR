package parser.statement;

import parser.expression.Exp;

public class StmtReturn extends Stmt {
    Exp exp;
    public StmtReturn(Exp exp) {
        this.exp = exp;
    }
}
