package parser.expression;

import lexer.LexType;
import lexer.LexerIterator;
import lexer.Token;

import java.util.ArrayList;

public class CondParser {
    LexerIterator iterator;

    public CondParser(LexerIterator iterator) {
        this.iterator = iterator;
    }

    public Cond parseCond() {
        LOrExp lOrExp = parseLOrExp();
        System.out.println("<Cond>");
        return new Cond(lOrExp);
    }

    public LOrExp parseLOrExp() {
        ArrayList<LAndExp> lAndExps = new ArrayList<>();
        lAndExps.add(parseLAndExp());
        while (iterator.preRead(1).getLexType() == LexType.OR) {
            System.out.println("<LOrExp>");
            iterator.read(); // ||
            lAndExps.add(parseLAndExp());
        }
        System.out.println("<LOrExp>");
        return new LOrExp(lAndExps);
    }

    public LAndExp parseLAndExp() {
        ArrayList<EqExp> EqExps = new ArrayList<>();
        EqExps.add(parseEqExp());
        while (iterator.preRead(1).getLexType() == LexType.AND) {
            System.out.println("<LAndExp>");
            iterator.read(); // &&
            EqExps.add(parseEqExp());
        }
        System.out.println("<LAndExp>");
        return new LAndExp(EqExps);
    }

    public EqExp parseEqExp() {
        ArrayList<RelExp> relExps = new ArrayList<>();
        ArrayList<Token> signs = new ArrayList<>();
        relExps.add(parseRelExp());
        while (iterator.preRead(1).getLexType() == LexType.EQL
                || iterator.preRead(1).getLexType() == LexType.NEQ) {
            System.out.println("<EqExp>");
            signs.add(iterator.read()); // == | !=
            relExps.add(parseRelExp());
        }
        System.out.println("<EqExp>");
        return new EqExp(relExps, signs);
    }

    public RelExp parseRelExp() {
        ArrayList<AddExp> addExps = new ArrayList<>();
        ArrayList<Token> signs = new ArrayList<>();
        ExpParser expParser = new ExpParser(iterator);
        addExps.add(expParser.parseAddExp());
        while (iterator.preRead(1).getLexType() == LexType.LSS
                || iterator.preRead(1).getLexType() == LexType.LEQ
                || iterator.preRead(1).getLexType() == LexType.GRE
                || iterator.preRead(1).getLexType() == LexType.GEQ) {
            System.out.println("<RelExp>");
            signs.add(iterator.read());
            addExps.add(expParser.parseAddExp());
        }
        System.out.println("<RelExp>");
        return new RelExp(addExps, signs);
    }

}
