package parser.declaration;

import lexer.Token;
import parser.expression.ConstExp;

import java.util.ArrayList;

public class VarDef {
    Token Ident;
    ArrayList<ConstExp> constExps;
    InitVal initVal;

    public VarDef(Token Ident, ArrayList<ConstExp> constExps, InitVal initVal) {
        this.Ident = Ident;
        this.constExps = constExps;
        this.initVal = initVal;
    }
}
