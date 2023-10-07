package parser.function;

import lexer.Token;
import parser.declaration.Btype;
import parser.expression.ConstExp;

public class FuncFParam {
    Btype btype;
    Token Ident;
    ConstExp constExp;
    public FuncFParam(Btype btype, Token Ident, ConstExp constExp) {
        this.btype = btype;
        this.Ident = Ident;
        this.constExp = constExp;
    }
}
