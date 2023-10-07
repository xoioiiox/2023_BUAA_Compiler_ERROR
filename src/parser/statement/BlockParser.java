package parser.statement;

import lexer.LexType;
import lexer.LexerIterator;
import parser.declaration.DeclParser;

import java.util.ArrayList;

public class BlockParser {
    LexerIterator iterator;

    public BlockParser(LexerIterator iterator) {
        this.iterator = iterator;
    }

    public Block parseBlock() {
        ArrayList<BlockItem> blockItems = new ArrayList<>();
        iterator.read(); // {
        while (iterator.preRead(1).getLexType() != LexType.RBRACE) {
            blockItems.add(parseBlockItem());
        }
        iterator.read(); // }
        System.out.println("<Block>");
        return new Block(blockItems);
    }

    public BlockItem parseBlockItem() {
        BlockItem blockItem;
        if (iterator.preRead(1).getLexType() == LexType.CONSTTK
                || iterator.preRead(1).getLexType() == LexType.INTTK) {
            DeclParser declParser = new DeclParser(iterator);
            blockItem = new BlockItem(declParser.parseDecl());
        }
        else {
            StmtParser stmtParser = new StmtParser(iterator);
            blockItem = new BlockItem(stmtParser.parseStmt());
        }
        return blockItem;
    }

}
