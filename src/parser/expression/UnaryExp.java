package parser.expression;

import error.*;
import error.Error;
import lexer.Token;

public class UnaryExp {
    private Token Ident;
    private FuncRParams funcRParams;
    private PrimaryExp primaryExp;
    private UnaryOp unaryOp;
    private UnaryExp unaryExp;
    private SymbolTable curSymbolTable;

    public UnaryExp(UnaryOp unaryOp, UnaryExp unaryExp, SymbolTable curSymbolTable) {
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
        this.curSymbolTable = curSymbolTable;
    }

    public UnaryExp(Token Ident, FuncRParams funcRParams, SymbolTable curSymbolTable) {
        this.Ident = Ident;
        this.funcRParams = funcRParams;
        this.curSymbolTable = curSymbolTable;
    }

    public UnaryExp(PrimaryExp primaryExp, SymbolTable curSymbolTable) {
        this.primaryExp = primaryExp;
        this.curSymbolTable = curSymbolTable;
    }

    public int getDimension() {
        if (primaryExp != null) {
            return primaryExp.getDimension();
        }
        else if (unaryExp != null) {
            return unaryExp.getDimension();
        }
        else {
            Symbol symbol = curSymbolTable.getSymbol(Ident.getVal()); //TODO 若之前定义函数时重名怎么办？
            if (symbol instanceof SymbolFunc) {
                return ((SymbolFunc) symbol).getReType();
            }
            else {
                return -2;
            }
        }
    }
}
