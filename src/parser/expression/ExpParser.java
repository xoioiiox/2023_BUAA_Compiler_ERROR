package parser.expression;

import error.ErrorType;
import error.Error;
import lexer.LexType;
import lexer.LexerIterator;
import lexer.Token;

import java.util.ArrayList;

public class ExpParser {
    LexerIterator iterator;
    private boolean printOrNot;

    public ExpParser(LexerIterator iterator) {
        this.iterator = iterator;
        this.printOrNot = true; // 默认打印
    }

    public void setPrintOrNot(boolean printOrNot) {
        this.printOrNot = printOrNot;
    }

    public Exp parseExp() {
        Exp exp = new Exp(parseAddExp());
        if (printOrNot) {
            System.out.println("<Exp>");
        }
        return exp;
    }

    public AddExp parseAddExp() {
        ArrayList<Token> signs = new ArrayList<>();
        ArrayList<MulExp> mulExps = new ArrayList<>();
        mulExps.add(parseMulExp());
        while (iterator.preRead(1).getLexType() == LexType.PLUS
                || iterator.preRead(1).getLexType() == LexType.MINU) {
            if (printOrNot) {
                System.out.println("<AddExp>");
            }
            signs.add(iterator.read());
            mulExps.add(parseMulExp());
        }
        if (printOrNot) {
            System.out.println("<AddExp>");
        }
        return new AddExp(signs, mulExps);
    }

    public MulExp parseMulExp() {
        ArrayList<Token> signs = new ArrayList<>();
        ArrayList<UnaryExp> unaryExps = new ArrayList<>();
        unaryExps.add(parseUnaryExp());
        while (iterator.preRead(1).getLexType() == LexType.MULT
                || iterator.preRead(1).getLexType() == LexType.DIV
                || iterator.preRead(1).getLexType() == LexType.MOD) {
            if (printOrNot) {
                System.out.println("<MulExp>");
            }
            signs.add(iterator.read());
            unaryExps.add(parseUnaryExp());
        }
        if (printOrNot) {
            System.out.println("<MulExp>");
        }
        return new MulExp(signs, unaryExps);
    }

    public UnaryExp parseUnaryExp() {
        UnaryExp unaryExp;
        if (iterator.preRead(1).getLexType() == LexType.IDENFR
                && iterator.preRead(2).getLexType() == LexType.LPARENT) {
            FuncRParams funcRParams = null;
            Token Ident = iterator.read(); //Ident
            iterator.read(); // (
            if (iterator.preRead(1).getLexType() != LexType.RPARENT) {
                funcRParams = parseFuncRParams();
            }
            //iterator.read(); // )
            judgeErrorJ();
            unaryExp = new UnaryExp(Ident, funcRParams);
        }
        else if (iterator.preRead(1).getLexType() == LexType.PLUS
                || iterator.preRead(1).getLexType() == LexType.MINU
                || iterator.preRead(1).getLexType() == LexType.NOT) {
            Token op = iterator.read();
            UnaryOp unaryOp = new UnaryOp(op);
            if (printOrNot) {
                System.out.println("<UnaryOp>");
            }
            unaryExp = new UnaryExp(unaryOp, parseUnaryExp());
        }
        else {
            PrimaryExp primaryExp = parsePrimaryExp();
            unaryExp = new UnaryExp(primaryExp);
        }
        if (printOrNot) {
            System.out.println("<UnaryExp>");
        }
        return unaryExp;
    }

    public FuncRParams parseFuncRParams() {
        ArrayList<Exp> exps = new ArrayList<>();
        exps.add(parseExp());
        while (iterator.preRead(1).getLexType() == LexType.COMMA) {
            iterator.read(); // ,
            exps.add(parseExp());
        }
        if (printOrNot) {
            System.out.println("<FuncRParams>");
        }
        return new FuncRParams(exps);
    }

    public PrimaryExp parsePrimaryExp() {
        PrimaryExp primaryExp;
        if (iterator.preRead(1).getLexType() == LexType.LPARENT) {
            iterator.read(); // (
            Exp exp = parseExp();
            //iterator.read(); // )
            judgeErrorJ();
            primaryExp = new PrimaryExp(exp);
        }
        else if (iterator.preRead(1).getLexType() == LexType.IDENFR) {
            LVal lVal = parseLVal();
            primaryExp = new PrimaryExp(lVal);
        }
        else {
            Token intConst = iterator.read();
            Number number = new Number(intConst);
            if (printOrNot) {
                System.out.println("<Number>");
            }
            primaryExp = new PrimaryExp(number);
        }
        if (printOrNot) {
            System.out.println("<PrimaryExp>");
        }
        return primaryExp;
    }

    public LVal parseLVal() {
        ArrayList<Exp> exps = new ArrayList<>();
        Token Ident = iterator.read(); // Ident
        while (iterator.preRead(1).getLexType() == LexType.LBRACK) {
            iterator.read(); // [
            exps.add(parseExp());
            //iterator.read(); // ]
            judgeErrorK();
        }
        if (printOrNot) {
            System.out.println("<LVal>");
        }
        return new LVal(Ident, exps);
    }

    public ConstExp parseConstExp() {
        AddExp addExp = parseAddExp();
        System.out.println("<ConstExp>");
        return new ConstExp(addExp);
    }

    public void judgeErrorJ() {
        if (iterator.preRead(1).getLexType() == LexType.RPARENT) {
            iterator.read();
        }
        else {
            Error error = new Error(iterator.getCurLineNum(), ErrorType.j);
        }
    }

    public void judgeErrorK() {
        if (iterator.preRead(1).getLexType() == LexType.RBRACK) {
            iterator.read();
        }
        else {
            Error error = new Error(iterator.getCurLineNum(), ErrorType.k);
        }
    }

}
