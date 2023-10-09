package parser.function;

import lexer.LexType;
import lexer.Token;

public class FuncType {
    private Token type;

    public FuncType(Token type) {
        this.type = type;
    }

    public int getFuncType() {
        if (type.getLexType() == LexType.VOIDTK) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
