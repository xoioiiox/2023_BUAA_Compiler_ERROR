package parser.statement;

import parser.expression.LVal;

public class StmtGetInt extends Stmt {
    LVal lVal;

    public StmtGetInt(LVal lVal) {
        this.lVal = lVal;
    }
}
