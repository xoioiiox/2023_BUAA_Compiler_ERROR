package parser.expression;

import lexer.Token;

public class UnaryExp {
    Token Ident;
    FuncRParams funcRParams;
    PrimaryExp primaryExp;
    UnaryOp unaryOp;
    UnaryExp unaryExp;

    public UnaryExp(UnaryOp unaryOp, UnaryExp unaryExp) {
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    public UnaryExp(Token Ident, FuncRParams funcRParams) {
        this.Ident = Ident;
        this.funcRParams = funcRParams;
    }

    public UnaryExp(PrimaryExp primaryExp) {
        this.primaryExp = primaryExp;
    }
}
