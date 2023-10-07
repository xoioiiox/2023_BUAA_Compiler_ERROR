package parser.statement;

import parser.expression.Cond;

public class StmtFor extends Stmt {
    ForStmt forStmt1;
    Cond cond;
    ForStmt forStmt2;
    Stmt stmt;
    public StmtFor(ForStmt forStmt1, Cond cond, ForStmt forStmt2, Stmt stmt) {
        this.forStmt1 = forStmt1;
        this.cond = cond;
        this.forStmt2 = forStmt2;
        this.stmt = stmt;
    }
    public StmtFor(Cond cond, ForStmt forStmt2, Stmt stmt) {
        this.cond = cond;
        this.forStmt2 = forStmt2;
        this.stmt = stmt;
    }
    public StmtFor(ForStmt forStmt1, ForStmt forStmt2, Stmt stmt) {
        this.forStmt1 = forStmt1;
        this.forStmt2 = forStmt2;
        this.stmt = stmt;
    }
    public StmtFor(ForStmt forStmt1, Cond cond, Stmt stmt) {
        this.forStmt1 = forStmt1;
        this.cond = cond;
        this.stmt = stmt;
    }
    public StmtFor(ForStmt forStmt, Stmt stmt, int pos) {
        if (pos == 1) {
            this.forStmt1 = forStmt;
        }
        else {
            this.forStmt2 = forStmt;
        }
        this.stmt = stmt;
    }
    public StmtFor(Cond cond, Stmt stmt) {
        this.cond = cond;
        this.stmt = stmt;
    }
    public StmtFor(Stmt stmt) {
        this.stmt = stmt;
    }
}
