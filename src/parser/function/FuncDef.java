package parser.function;

import lexer.Token;
import parser.statement.Block;

import java.util.ArrayList;

public class FuncDef {
    FuncType funcType;
    Token Ident;
    ArrayList<FuncFParam> funcFParams;
    Block block;

    public FuncDef(FuncType funcType, Token Ident, ArrayList<FuncFParam> funcFParams, Block block) {
        this.funcType = funcType;
        this.Ident = Ident;
        this.funcFParams = funcFParams;
        this.block = block;
    }
}
