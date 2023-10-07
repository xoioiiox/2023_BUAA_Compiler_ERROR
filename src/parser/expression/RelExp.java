package parser.expression;

import lexer.Token;

import java.util.ArrayList;

public class RelExp {
    ArrayList<AddExp> addExps;
    ArrayList<Token> signs;

    public RelExp(ArrayList<AddExp> addExps, ArrayList<Token> signs) {
        this.addExps = addExps;
        this.signs = signs;
    }
}
