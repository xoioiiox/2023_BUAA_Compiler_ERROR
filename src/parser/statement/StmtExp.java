package parser.statement;

import parser.expression.Exp;

public class StmtExp extends Stmt {
    Exp exp;

    public StmtExp(Exp exp) {
        this.exp = exp;
    }
}
