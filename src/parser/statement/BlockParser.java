package parser.statement;

import error.SymbolTable;
import io.Output;
import io.ParserOutput;
import lexer.LexType;
import lexer.LexerIterator;
import parser.declaration.DeclParser;

import java.util.ArrayList;

public class BlockParser {
    private LexerIterator iterator;
    private SymbolTable curSymbolTable;

    public BlockParser(LexerIterator iterator, SymbolTable curSymbolTable) {
        this.iterator = iterator;
        this.curSymbolTable = curSymbolTable;
    }

    public Block parseBlock() {
        /*进入block需要新建符号表*/
        curSymbolTable = new SymbolTable(curSymbolTable);
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        iterator.read(); // {
        while (iterator.preRead(1).getLexType() != LexType.RBRACE) {
            blockItems.add(parseBlockItem(false));
        }
        iterator.read(); // }
        Output output = new Output("<Block>");
        ParserOutput.addOutput(output);
        //System.out.println("<Block>");
        return new Block(blockItems);
    }

    public BlockItem parseBlockItem(boolean checkVoidReturn) {
        BlockItem blockItem;
        if (iterator.preRead(1).getLexType() == LexType.CONSTTK
                || iterator.preRead(1).getLexType() == LexType.INTTK) {
            DeclParser declParser = new DeclParser(iterator, curSymbolTable);
            blockItem = new BlockItem(declParser.parseDecl());
        }
        else {
            StmtParser stmtParser = new StmtParser(iterator, curSymbolTable, checkVoidReturn);
            blockItem = new BlockItem(stmtParser.parseStmt());
        }
        return blockItem;
    }

}
