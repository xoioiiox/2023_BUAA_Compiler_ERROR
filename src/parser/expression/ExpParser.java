package parser.expression;

import error.*;
import error.Error;
import io.Output;
import io.ParserOutput;
import lexer.LexType;
import lexer.LexerIterator;
import lexer.Token;

import java.util.ArrayList;

public class ExpParser {
    private LexerIterator iterator;
    private boolean printOrNot;
    private SymbolTable curSymbolTable;

    public ExpParser(LexerIterator iterator, SymbolTable curSymbolTable) {
        this.iterator = iterator;
        this.printOrNot = true; // 默认打印
        this.curSymbolTable = curSymbolTable;
    }

    public void setPrintOrNot(boolean printOrNot) {
        this.printOrNot = printOrNot;
    }

    public Exp parseExp() {
        Exp exp = new Exp(parseAddExp());
        if (printOrNot) {
            Output output = new Output("<Exp>");
            ParserOutput.addOutput(output);
            //System.out.println("<Exp>");
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
                Output output = new Output("<AddExp>");
                ParserOutput.addOutput(output);
                //System.out.println("<AddExp>");
            }
            signs.add(iterator.read());
            mulExps.add(parseMulExp());
        }
        if (printOrNot) {
            Output output = new Output("<AddExp>");
            ParserOutput.addOutput(output);
            //System.out.println("<AddExp>");
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
                Output output = new Output("<MulExp>");
                ParserOutput.addOutput(output);
                //System.out.println("<MulExp>");
            }
            signs.add(iterator.read());
            unaryExps.add(parseUnaryExp());
        }
        if (printOrNot) {
            Output output = new Output("<MulExp>");
            ParserOutput.addOutput(output);
            //System.out.println("<MulExp>");
        }
        return new MulExp(signs, unaryExps);
    }

    public UnaryExp parseUnaryExp() {
        UnaryExp unaryExp;
        if (iterator.preRead(1).getLexType() == LexType.IDENFR
                && iterator.preRead(2).getLexType() == LexType.LPARENT) {
            FuncRParams funcRParams = null;
            Token Ident = iterator.read(); //Ident
            /*---C：未定义---*/
            boolean isErrorC = checkErrorC1(Ident);
            iterator.read(); // (
            if (isNextExp()) {
                funcRParams = parseFuncRParams();
            }
            /*---D：参数数目不匹配---*/
            boolean isErrorD = false;
            if (!isErrorC) {
                isErrorD = checkErrorD(Ident, funcRParams);
            }
            /*---E：参数类型不匹配---*/
            if (!isErrorC && !isErrorD) {
                checkErrorE(Ident, funcRParams);
            }
            //iterator.read(); // )
            checkErrorJ();
            unaryExp = new UnaryExp(Ident, funcRParams, curSymbolTable);
        }
        else if (iterator.preRead(1).getLexType() == LexType.PLUS
                || iterator.preRead(1).getLexType() == LexType.MINU
                || iterator.preRead(1).getLexType() == LexType.NOT) {
            Token op = iterator.read();
            UnaryOp unaryOp = new UnaryOp(op);
            if (printOrNot) {
                Output output = new Output("<UnaryOp>");
                ParserOutput.addOutput(output);
                //System.out.println("<UnaryOp>");
            }
            unaryExp = new UnaryExp(unaryOp, parseUnaryExp(), curSymbolTable);
        }
        else {
            PrimaryExp primaryExp = parsePrimaryExp();
            unaryExp = new UnaryExp(primaryExp, curSymbolTable);
        }
        if (printOrNot) {
            Output output = new Output("<UnaryExp>");
            ParserOutput.addOutput(output);
            //System.out.println("<UnaryExp>");
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
            Output output = new Output("<FuncRParams>");
            ParserOutput.addOutput(output);
            //System.out.println("<FuncRParams>");
        }
        return new FuncRParams(exps);
    }

    public PrimaryExp parsePrimaryExp() {
        PrimaryExp primaryExp;
        if (iterator.preRead(1).getLexType() == LexType.LPARENT) {
            iterator.read(); // (
            Exp exp = parseExp();
            //iterator.read(); // )
            checkErrorJ();
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
                Output output = new Output("<Number>");
                ParserOutput.addOutput(output);
                //System.out.println("<Number>");
            }
            primaryExp = new PrimaryExp(number);
        }
        if (printOrNot) {
            Output output = new Output("<PrimaryExp>");
            ParserOutput.addOutput(output);
            //System.out.println("<PrimaryExp>");
        }
        return primaryExp;
    }

    public LVal parseLVal() {
        ArrayList<Exp> exps = new ArrayList<>();
        Token Ident = iterator.read(); // Ident
        /*---Error C---*/
        checkErrorC2(Ident);
        while (iterator.preRead(1).getLexType() == LexType.LBRACK) {
            iterator.read(); // [
            exps.add(parseExp());
            //iterator.read(); // ]
            checkErrorK();
        }
        if (printOrNot) {
            Output output = new Output("<LVal>");
            ParserOutput.addOutput(output);
            //System.out.println("<LVal>");
        }
        return new LVal(Ident, exps, curSymbolTable);
    }

    public ConstExp parseConstExp() {
        AddExp addExp = parseAddExp();
        Output output = new Output("<ConstExp>");
        ParserOutput.addOutput(output);
        //System.out.println("<ConstExp>");
        return new ConstExp(addExp);
    }

    public boolean checkErrorC1(Token Ident) { //Func
        Symbol symbol = curSymbolTable.getSymbol(Ident.getVal());
        if (!(symbol instanceof SymbolFunc)) {
            Error error = new Error(Ident.getLineNum(), ErrorType.c);
            ErrorTable.addError(error);
            return true;
        }
        return false;
    }

    public void checkErrorC2(Token Ident) { // Var Con
        Symbol symbol = curSymbolTable.getSymbol(Ident.getVal());
        if (!(symbol instanceof SymbolVar) && !(symbol instanceof SymbolCon)) {
            Error error = new Error(Ident.getLineNum(), ErrorType.c);
            ErrorTable.addError(error);
        }
    }

    public boolean checkErrorD(Token Ident, FuncRParams funcRParams) {
        int cnt = 0;
        if (funcRParams != null) {
            cnt = funcRParams.getParamsNum();
        }
        SymbolFunc symbolFunc = (SymbolFunc) curSymbolTable.getSymbol(Ident.getVal());
        if (cnt != symbolFunc.getParamsNum()) {
            Error error = new Error(Ident.getLineNum(), ErrorType.d);
            ErrorTable.addError(error);
            return true;
        }
        return false;
    }

    public void checkErrorE(Token Ident, FuncRParams funcRParams) {
        if (funcRParams == null) { // 函数无参数，且参数数量相等则不出错
            return;
        }
        SymbolFunc symbolFunc = (SymbolFunc) curSymbolTable.getSymbol(Ident.getVal());
        ArrayList<SymbolVar> symbols = symbolFunc.getParams();
        ArrayList<Exp> exps = funcRParams.getExps();
        for (int i = 0; i < exps.size(); i++) {
            if (symbols.get(i).getDimension() != exps.get(i).getDimension()) {
                //System.out.println("need" + symbols.get(i).getDimension() + "has" + exps.get(i).getDimension());
                Error error = new Error(Ident.getLineNum(), ErrorType.e);
                ErrorTable.addError(error);
                return; //TODO 如何统计实参维度
            }
        }
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

    public boolean isNextExp() {
        LexType lexType = iterator.preRead(1).getLexType();
        if (lexType == LexType.PLUS || lexType == LexType.MINU || lexType == LexType.NOT
                || lexType == LexType.LPARENT || lexType == LexType.IDENFR || lexType == LexType.INTCON) {
            return true;
        }
        return false;
    }

}
