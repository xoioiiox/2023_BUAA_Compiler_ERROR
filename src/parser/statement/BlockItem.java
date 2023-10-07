package parser.statement;

import parser.declaration.Decl;

public class BlockItem {
    Decl decl;
    Stmt stmt;

    public BlockItem(Decl decl) {
        this.decl = decl;
        this.stmt = null;
    }

    public BlockItem(Stmt stmt) {
        this.stmt = stmt;
        this.decl = null;
    }
}
