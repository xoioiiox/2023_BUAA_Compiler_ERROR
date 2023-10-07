package parser.expression;

import lexer.Token;

public class Number {
    Token intConst;

    public Number(Token intConst) {
        this.intConst = intConst;
    }
}
