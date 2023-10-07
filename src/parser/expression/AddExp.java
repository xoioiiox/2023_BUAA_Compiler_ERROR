package parser.expression;

import lexer.Token;

import java.util.ArrayList;

public class AddExp {
    ArrayList<Token> signs;
    ArrayList<MulExp> mulExps;

    public AddExp(ArrayList<Token> signs, ArrayList<MulExp> mulExps) {
        this.signs = signs;
        this.mulExps = mulExps;
    }
}
