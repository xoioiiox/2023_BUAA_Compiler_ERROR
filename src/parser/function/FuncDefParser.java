package parser.function;

import error.*;
import error.Error;
import io.Output;
import io.ParserOutput;
import lexer.LexType;
import lexer.LexerIterator;
import lexer.Token;
import parser.declaration.Btype;
import parser.expression.ConstExp;
import parser.expression.ExpParser;
import parser.statement.*;

import java.util.ArrayList;

public class FuncDefParser {
    private LexerIterator iterator;
    private SymbolTable curSymbolTable;

    public FuncDefParser(LexerIterator iterator, SymbolTable curSymbolTable) {
        this.iterator = iterator;
        this.curSymbolTable = curSymbolTable;
    }

    public FuncDef parseFuncDef() {
        ArrayList<FuncFParam> funcFParams = new ArrayList<>();
        FuncType funcType = new FuncType(iterator.read());
        boolean checkVoidReturn = funcType.getFuncType() == -1;
        Output output = new Output("<FuncType>");
        ParserOutput.addOutput(output);
        //System.out.println("<FuncType>");
        Token Ident = iterator.read();
        /*（若未重定义）新建函数符号，并加入原符号表*/
        SymbolFunc symbolFunc = new SymbolFunc(Ident.getVal(), Ident.getLineNum(), funcType.getFuncType());
        if (curSymbolTable.checkReName(Ident.getVal())) {
            Error error = new Error(Ident.getLineNum(), ErrorType.b);
            ErrorTable.addError(error);
        }
        else {
            curSymbolTable.addSymbol(symbolFunc);
        }
        /*创建新的符号表，之后读到的函数参数都加入新符号表*/
        curSymbolTable = new SymbolTable(curSymbolTable);
        iterator.read(); // (
        if (iterator.preRead(1).getLexType() == LexType.INTTK) {
            funcFParams.add(parseFuncFParam(symbolFunc));
            while (iterator.preRead(1).getLexType() == LexType.COMMA) {
                iterator.read(); // ,
                funcFParams.add(parseFuncFParam(symbolFunc));
            }
            Output output1 = new Output("<FuncFParams>");
            ParserOutput.addOutput(output1);
            //System.out.println("<FuncFParams>");
        }
        //iterator.read(); // )
        checkErrorJ();
        BlockParser blockParser = new BlockParser(iterator, curSymbolTable, 0);
        //Block block = blockParser.parseBlock(); //block 这里不再新建符号表
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        iterator.read(); // {
        while (iterator.preRead(1).getLexType() != LexType.RBRACE) {
            blockItems.add(blockParser.parseBlockItem(checkVoidReturn));
        }
        iterator.read(); // }
        Output output2 = new Output("<Block>");
        ParserOutput.addOutput(output2);
        //System.out.println("<Block>");
        Block block = new Block(blockItems);
        /*检查return语句是否与函数类型匹配*/
        if (funcType.getFuncType() == 0) { //int
            Stmt stmt = block.getBlockItems().get(block.getBlockItems().size() - 1).getStmt();
            if (!(stmt instanceof StmtReturn)) { // 无需考虑数据流？
                Error error = new Error(iterator.readLast().getLineNum(), ErrorType.g);
                ErrorTable.addError(error);
            }
        }
        Output output3 = new Output("<FuncDef>");
        ParserOutput.addOutput(output3);
        //System.out.println("<FuncDef>");
        return new FuncDef(funcType, Ident, funcFParams, block);
    }

    public FuncFParam parseFuncFParam(SymbolFunc symbolFunc) {
        ConstExp constExp = null;
        iterator.read();
        Btype btype = new Btype("int");
        Token Ident = iterator.read();
        int cnt = 0;
        while (iterator.preRead(1).getLexType() == LexType.LBRACK) {
            cnt++;
            iterator.read(); // [
            if (iterator.preRead(1).getLexType() != LexType.RBRACK
                    && iterator.preRead(1).getLexType() != LexType.LBRACK) { //考虑右框缺失
                ExpParser expParser = new ExpParser(iterator, curSymbolTable);
                constExp = expParser.parseConstExp(); //parseConstExp
            }
            //iterator.read(); // ]
            checkErrorK();
        }
        SymbolVar symbolVar = new SymbolVar(Ident.getVal(), Ident.getLineNum(), cnt);
        if (curSymbolTable.checkReName(Ident.getVal())) {
            Error error = new Error(Ident.getLineNum(), ErrorType.b);
            ErrorTable.addError(error);
        }
        else {
            curSymbolTable.addSymbol(symbolVar);
            symbolFunc.addParams(symbolVar);
        }
        Output output = new Output("<FuncFParam>");
        ParserOutput.addOutput(output);
        //System.out.println("<FuncFParam>");
        return new FuncFParam(btype, Ident, constExp);
    }

    public void checkErrorJ() {
        if (iterator.preRead(1).getLexType() == LexType.RPARENT) {
            iterator.read();
        }
        else {
            Error error = new Error(iterator.readLast().getLineNum(), ErrorType.j);
            ErrorTable.addError(error);
        }
    }

    public void checkErrorK() {
        if (iterator.preRead(1).getLexType() == LexType.RBRACK) {
            iterator.read();
        }
        else {
            Error error = new Error(iterator.readLast().getLineNum(), ErrorType.k);
            ErrorTable.addError(error);
        }
    }
}
