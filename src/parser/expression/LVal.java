package parser.expression;

import lexer.Token;

import java.util.ArrayList;

public class LVal {
    Token Ident;
    ArrayList<Exp> exps;

    public LVal(Token Ident, ArrayList<Exp> exps) {
        this.Ident = Ident;
        this.exps = exps;
    }
}
