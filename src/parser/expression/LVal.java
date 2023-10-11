package parser.expression;

import error.Symbol;
import error.SymbolCon;
import error.SymbolTable;
import error.SymbolVar;
import lexer.Token;

import java.util.ArrayList;

public class LVal {
    private Token Ident;
    private ArrayList<Exp> exps;
    private SymbolTable curSymbolTable;

    public LVal(Token Ident, ArrayList<Exp> exps, SymbolTable curSymbolTable) {
        this.Ident = Ident;
        this.exps = exps;
        this.curSymbolTable = curSymbolTable;
    }

    public Token getIdent() {
        return Ident;
    }

    public int getDimension() {
        Symbol symbol = curSymbolTable.getSymbol(Ident.getVal());
        if (!(symbol instanceof SymbolVar) && !(symbol instanceof SymbolCon)) {
            return -2; //未定义
        }
        int dim1, dim2; //dim1为原定义维度
        if (symbol instanceof SymbolVar) {
            dim1 = ((SymbolVar) symbol).getDimension();
        }
        else {
            dim1 = ((SymbolCon) symbol).getDimension();
        }
        dim2 = exps.size();
        return dim1 - dim2;
    }
}
