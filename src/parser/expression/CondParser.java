package parser.expression;

import error.SymbolTable;
import io.Output;
import io.ParserOutput;
import lexer.LexType;
import lexer.LexerIterator;
import lexer.Token;

import java.util.ArrayList;

public class CondParser {
    private LexerIterator iterator;
    private SymbolTable curSymbolTable;

    public CondParser(LexerIterator iterator, SymbolTable curSymbolTable) {
        this.iterator = iterator;
        this.curSymbolTable = curSymbolTable;
    }

    public Cond parseCond() {
        LOrExp lOrExp = parseLOrExp();
        Output output = new Output("<Cond>");
        ParserOutput.addOutput(output);
        //System.out.println("<Cond>");
        return new Cond(lOrExp);
    }

    public LOrExp parseLOrExp() {
        ArrayList<LAndExp> lAndExps = new ArrayList<>();
        lAndExps.add(parseLAndExp());
        while (iterator.preRead(1).getLexType() == LexType.OR) {
            Output output = new Output("<LOrExp>");
            ParserOutput.addOutput(output);
            //System.out.println("<LOrExp>");
            iterator.read(); // ||
            lAndExps.add(parseLAndExp());
        }
        Output output = new Output("<LOrExp>");
        ParserOutput.addOutput(output);
        //System.out.println("<LOrExp>");
        return new LOrExp(lAndExps);
    }

    public LAndExp parseLAndExp() {
        ArrayList<EqExp> EqExps = new ArrayList<>();
        EqExps.add(parseEqExp());
        while (iterator.preRead(1).getLexType() == LexType.AND) {
            Output output = new Output("<LAndExp>");
            ParserOutput.addOutput(output);
            //System.out.println("<LAndExp>");
            iterator.read(); // &&
            EqExps.add(parseEqExp());
        }
        Output output = new Output("<LAndExp>");
        ParserOutput.addOutput(output);
        //System.out.println("<LAndExp>");
        return new LAndExp(EqExps);
    }

    public EqExp parseEqExp() {
        ArrayList<RelExp> relExps = new ArrayList<>();
        ArrayList<Token> signs = new ArrayList<>();
        relExps.add(parseRelExp());
        while (iterator.preRead(1).getLexType() == LexType.EQL
                || iterator.preRead(1).getLexType() == LexType.NEQ) {
            Output output = new Output("<EqExp>");
            ParserOutput.addOutput(output);
            //System.out.println("<EqExp>");
            signs.add(iterator.read()); // == | !=
            relExps.add(parseRelExp());
        }
        Output output = new Output("<EqExp>");
        ParserOutput.addOutput(output);
        //System.out.println("<EqExp>");
        return new EqExp(relExps, signs);
    }

    public RelExp parseRelExp() {
        ArrayList<AddExp> addExps = new ArrayList<>();
        ArrayList<Token> signs = new ArrayList<>();
        ExpParser expParser = new ExpParser(iterator, curSymbolTable);
        addExps.add(expParser.parseAddExp());
        while (iterator.preRead(1).getLexType() == LexType.LSS
                || iterator.preRead(1).getLexType() == LexType.LEQ
                || iterator.preRead(1).getLexType() == LexType.GRE
                || iterator.preRead(1).getLexType() == LexType.GEQ) {
            Output output = new Output("<RelExp>");
            ParserOutput.addOutput(output);
            //System.out.println("<RelExp>");
            signs.add(iterator.read());
            addExps.add(expParser.parseAddExp());
        }
        Output output = new Output("<RelExp>");
        ParserOutput.addOutput(output);
        //System.out.println("<RelExp>");
        return new RelExp(addExps, signs);
    }

}
