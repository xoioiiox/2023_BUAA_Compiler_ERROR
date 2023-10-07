package lexer;

public class Token {
    private LexType lexType;
    private String val;
    private int lineNum;

    public Token(LexType lexType, String val, int lineNum) {
        this.lexType = lexType;
        this.val = val;
        this.lineNum = lineNum;
    }

    public LexType getLexType() {
        return lexType;
    }

    public String getVal() {
        return val;
    }

    public int getLineNum() {
        return lineNum;
    }
}
