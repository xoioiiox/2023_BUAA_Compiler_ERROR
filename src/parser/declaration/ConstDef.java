package parser.declaration;

import lexer.Token;
import parser.expression.ConstExp;

import java.util.ArrayList;

public class ConstDef {
    Token Ident;
    ArrayList<ConstExp> constExps;
    ConstInitVal constInitVal;

    public ConstDef(Token Ident, ArrayList<ConstExp> constExps, ConstInitVal constInitVal) {
        this.Ident = Ident;
        this.constExps = constExps;
        this.constInitVal = constInitVal;
    }
}
