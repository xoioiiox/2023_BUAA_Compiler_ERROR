package parser;
import lexer.LexType;
import lexer.Token;
import lexer.LexerIterator;
import parser.declaration.Decl;
import parser.declaration.DeclParser;
import parser.function.FuncDef;
import parser.function.FuncDefParser;
import parser.statement.Block;
import parser.statement.BlockParser;

import java.util.ArrayList;

public class CompUnit {
    LexerIterator iterator;
    ArrayList<Decl> decls;
    ArrayList<FuncDef> funcDefs;
    Block block;

    public CompUnit (LexerIterator iterator) {
        this.iterator = iterator;
        this.decls = new ArrayList<>();
        this.funcDefs = new ArrayList<>();
        this.block = null;
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
            DeclParser declParser = new DeclParser(iterator);
            decls.add(declParser.parseDecl());
        }
    }

    public void parseFuncDefs() {
        while (iterator.hasNext()) {
            Token preToken2 = iterator.preRead(2);
            if (preToken2.getLexType() == LexType.MAINTK) {
                return;
            }
            FuncDefParser funcDefParser = new FuncDefParser(iterator);
            funcDefs.add(funcDefParser.parseFuncDef());
        }
    }

    public void parseMainFuncDef() {
        iterator.read(); // int
        iterator.read(); // main
        iterator.read(); // (
        iterator.read(); // )
        BlockParser blockParser = new BlockParser(iterator);
        block = blockParser.parseBlock();
        System.out.println("<MainFuncDef>");
    }

}
