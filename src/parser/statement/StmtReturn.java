package parser.statement;

import lexer.Token;
import parser.expression.Exp;

public class StmtReturn extends Stmt {
    private Token token;
    private Exp exp;

    public StmtReturn(Token token, Exp exp) {
        this.token = token;
        this.exp = exp;
    }

    public Exp getExp() {
        return exp;
    }

    public Token getToken() {
        return token;
    }
}
