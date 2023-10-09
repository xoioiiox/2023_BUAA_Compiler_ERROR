package parser;
import error.ErrorTable;
import error.ErrorType;
import error.SymbolTable;
import error.Error;
import lexer.LexType;
import lexer.Token;
import lexer.LexerIterator;
import parser.declaration.Decl;
import parser.declaration.DeclParser;
import parser.function.FuncDef;
import parser.function.FuncDefParser;
import parser.statement.Block;
import parser.statement.BlockParser;
import parser.statement.Stmt;
import parser.statement.StmtReturn;

import java.util.ArrayList;

public class CompUnit {
    private LexerIterator iterator;
    private ArrayList<Decl> decls;
    private ArrayList<FuncDef> funcDefs;
    private Block block;
    private SymbolTable curSymbolTable;

    public CompUnit (LexerIterator iterator, SymbolTable curSymbolTable) {
        this.iterator = iterator;
        this.decls = new ArrayList<>();
        this.funcDefs = new ArrayList<>();
        this.block = null;
        this.curSymbolTable = curSymbolTable;
    }

    public void parseCompUnit() {
        parseDecls();
        parseFuncDefs();
        parseMainFuncDef();
        System.out.println("<CompUnit>");
    }

    public void parseDecls() {
        while (iterator.hasNext()) {
            Token preToken3 = iterator.preRead(3); //null?
            if (preToken3.getLexType() == LexType.LPARENT) { //FuncDef
                return;
            }
            DeclParser declParser = new DeclParser(iterator, curSymbolTable);
            decls.add(declParser.parseDecl());
        }
    }

    public void parseFuncDefs() {
        while (iterator.hasNext()) {
            Token preToken2 = iterator.preRead(2);
            if (preToken2.getLexType() == LexType.MAINTK) {
                return;
            }
            FuncDefParser funcDefParser = new FuncDefParser(iterator, curSymbolTable);
            funcDefs.add(funcDefParser.parseFuncDef());
        }
    }

    public void parseMainFuncDef() {
        iterator.read(); // int
        iterator.read(); // main
        iterator.read(); // (
        iterator.read(); // )
        BlockParser blockParser = new BlockParser(iterator, curSymbolTable);
        block = blockParser.parseBlock();
        Stmt stmt = block.getBlockItems().get(block.getBlockItems().size() - 1).getStmt();
        if (!(stmt instanceof StmtReturn)) {
            Error error = new Error(iterator.readLast().getLineNum(), ErrorType.g);
        }
        System.out.println("<MainFuncDef>");
    }

}
