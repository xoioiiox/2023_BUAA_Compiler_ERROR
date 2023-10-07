package parser.statement;

import parser.expression.Cond;

public class StmtIf extends Stmt{
    Cond cond;
    Stmt stmtIf;
    Stmt stmtElse;

    public StmtIf(Cond cond, Stmt stmtIf) {
        this.cond = cond;
        this.stmtIf = stmtIf;
        this.stmtElse = null;
    }

    public StmtIf(Cond cond, Stmt stmtIf, Stmt stmtElse) {
        this.cond = cond;
        this.stmtIf = stmtIf;
        this.stmtElse = stmtElse;
    }
}
