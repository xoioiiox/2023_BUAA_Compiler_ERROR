package parser.expression;

import lexer.Token;

import java.util.ArrayList;

public class MulExp {
    ArrayList<Token> signs;
    ArrayList<UnaryExp> unaryExps;

    public MulExp(ArrayList<Token> signs, ArrayList<UnaryExp> unaryExps) {
        this.signs = signs;
        this.unaryExps = unaryExps;
    }
}
