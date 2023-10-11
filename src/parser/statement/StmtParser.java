package parser.statement;

import error.*;
import error.Error;
import io.Output;
import io.ParserOutput;
import lexer.LexType;
import lexer.LexerIterator;
import lexer.Token;
import parser.expression.*;

import java.util.ArrayList;

public class StmtParser {
    private LexerIterator iterator;
    private SymbolTable curSymbolTable;
    private int inFor;
    private boolean checkVoidReturn;

    public StmtParser(LexerIterator iterator, SymbolTable curSymbolTable, boolean checkVoidReturn, int inFor) {
        this.iterator = iterator;
        this.curSymbolTable = curSymbolTable;
        this.inFor = inFor;
        this.checkVoidReturn = checkVoidReturn;
    }


    public Stmt parseStmt() {
        Stmt stmt = null;
        if (iterator.preRead(1).getLexType() == LexType.IFTK) {
            stmt = parseStmtIf();
        }
        else if (iterator.preRead(1).getLexType() == LexType.FORTK) {
            stmt = parseStmtFor();
        }
        else if (iterator.preRead(1).getLexType() == LexType.BREAKTK) {
            stmt = parseStmtBreak();
        }
        else if (iterator.preRead(1).getLexType() == LexType.CONTINUETK) {
            stmt = parseStmtContinue();
        }
        else if (iterator.preRead(1).getLexType() == LexType.RETURNTK) {
            stmt = parseStmtReturn();
        }
        else if (iterator.preRead(1).getLexType() == LexType.PRINTFTK) {
            stmt = parseStmtPrintf();
        }
        else if (iterator.preRead(1).getLexType() == LexType.LBRACE) { //block
            BlockParser blockParser = new BlockParser(iterator, curSymbolTable, inFor);
            stmt = blockParser.parseBlock();
        }
        else if (iterator.preRead(1).getLexType() == LexType.SEMICN) {
            iterator.read();
            stmt = new Stmt();
        }
        else if (iterator.preRead(1).getLexType() == LexType.IDENFR) {
            int prePos = iterator.getPos();
            ExpParser expParser = new ExpParser(iterator, curSymbolTable);
            iterator.setPrintOrNot(false);
            expParser.setPrintOrNot(false);
            expParser.parseExp();
            iterator.setPrintOrNot(true);
            expParser.setPrintOrNot(true);
            if (iterator.preRead(1).getLexType() == LexType.ASSIGN) {
                if (iterator.preRead(2).getLexType() == LexType.GETINTTK) {
                    iterator.setPos(prePos);
                    LVal lVal = expParser.parseLVal();
                    checkErrorH(lVal);
                    iterator.read(); // =
                    iterator.read(); // getint
                    iterator.read(); // (
                    //iterator.read(); // )
                    checkErrorJ();
                    //iterator.read(); // ;
                    checkErrorI();
                    stmt = new StmtGetInt(lVal);
                }
                else {
                    iterator.setPos(prePos);
                    LVal lVal = expParser.parseLVal();
                    checkErrorH(lVal);
                    iterator.read(); // =
                    Exp exp = expParser.parseExp();
                    //iterator.read(); // ;
                    checkErrorI();
                    stmt = new StmtAssign(lVal, exp);
                }
            }
            else {
                iterator.setPos(prePos);
                Exp exp = expParser.parseExp();
                //iterator.read(); // ;
                checkErrorI();
                stmt = new StmtExp(exp);
            }
        }
        else if (isNextExp()) {
            ExpParser expParser = new ExpParser(iterator, curSymbolTable);
            Exp exp = expParser.parseExp();
            //iterator.read(); // ;
            checkErrorI();
            stmt = new StmtExp(exp);
        }
        else {
            checkErrorI();
        }
        Output output = new Output("<Stmt>");
        ParserOutput.addOutput(output);
        //System.out.println("<Stmt>");
        return stmt;
    }


    public StmtIf parseStmtIf() {
        CondParser condParser = new CondParser(iterator, curSymbolTable);
        StmtIf stmtIf;
        iterator.read(); // if
        iterator.read(); // (
        Cond cond = condParser.parseCond();
        //iterator.read(); // )
        checkErrorJ();
        Stmt stmt1 = parseStmt();
        if (iterator.preRead(1).getLexType() == LexType.ELSETK) {
            iterator.read(); // else
            Stmt stmt2 = parseStmt();
            stmtIf = new StmtIf(cond, stmt1, stmt2);
        }
        else {
            stmtIf = new StmtIf(cond, stmt1);
        }
        return stmtIf;
    }

    public StmtFor parseStmtFor() {
        CondParser condParser = new CondParser(iterator, curSymbolTable);
        ForStmt forStmt1 = null;
        ForStmt forStmt2 = null;
        Cond cond = null;
        iterator.read(); // for
        iterator.read(); // (
        if (iterator.preRead(1).getLexType() != LexType.SEMICN) {
            forStmt1 = parseForStmt();
        }
        iterator.read(); // ;
        if (iterator.preRead(1).getLexType() != LexType.SEMICN) {
            cond = condParser.parseCond(); //parseCond
        }
        iterator.read(); // ;
        if (iterator.preRead(1).getLexType() != LexType.RPARENT) {
            forStmt2 = parseForStmt();
        }
        iterator.read(); // ) 保证不会缺失右括号
        inFor++;
        Stmt stmt = parseStmt();
        inFor--;
        return new StmtFor(forStmt1, cond, forStmt2, stmt);
    }

    public ForStmt parseForStmt() {
        ExpParser expParser = new ExpParser(iterator, curSymbolTable);
        LVal lVal = expParser.parseLVal();
        iterator.read(); // =
        Exp exp = expParser.parseExp();
        Output output = new Output("<ForStmt>");
        ParserOutput.addOutput(output);
        return new ForStmt(lVal, exp);
    }

    public StmtBreak parseStmtBreak() {
        Token token = iterator.read(); // break
        if (inFor == 0) {
            Error error = new Error(token.getLineNum(), ErrorType.m);
            ErrorTable.addError(error);
        }
        //iterator.read(); // ;
        checkErrorI();
        return new StmtBreak();
    }

    public StmtContinue parseStmtContinue() {
        Token token = iterator.read(); // continue
        if (inFor == 0) {
            Error error = new Error(token.getLineNum(), ErrorType.m);
            ErrorTable.addError(error);
        }
        //iterator.read(); // ;
        checkErrorI();
        return new StmtContinue();
    }

    public StmtReturn parseStmtReturn() {
        ExpParser expParser = new ExpParser(iterator, curSymbolTable);
        Token token = iterator.read(); // return
        Exp exp = null;
        if (isNextExp()) { //notice 单独return
            exp = expParser.parseExp(); //parseExp
        }
        if (checkVoidReturn && exp != null) { // TODO
            Error error = new Error(token.getLineNum(), ErrorType.f);
            ErrorTable.addError(error);
        }
        //iterator.read(); // ;
        checkErrorI();
        return new StmtReturn(token, exp);
    }

    public StmtPrintf parseStmtPrintf() {
        ExpParser expParser = new ExpParser(iterator, curSymbolTable);
        ArrayList<Exp> exps = new ArrayList<>();
        Token token = iterator.read(); // printf
        iterator.read(); // (
        Token formatString = iterator.read(); // formatString
        checkErrorA(token, formatString);
        int num = countFormat(formatString);
        int cnt = 0;
        while (iterator.preRead(1).getLexType() == LexType.COMMA) {
            iterator.read(); // ,
            exps.add(expParser.parseExp());//parseExp
            cnt++;
        }
        if (num != cnt) {
            Error error = new Error(token.getLineNum(), ErrorType.l);
            ErrorTable.addError(error);
        }
        //iterator.read(); // )
        checkErrorJ();
        //iterator.read(); // ;
        checkErrorI();
        return new StmtPrintf(formatString, exps);
    }

    public int countFormat(Token formatString) {
        int cnt = 0;
        String s = formatString.getVal();
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i - 1) == '%' && s.charAt(i) == 'd') {
                cnt++;
            }
        }
        return cnt;
    }

    private void checkErrorA(Token printf, Token formatString) {
        String s = formatString.getVal();
        int len = s.length();
        boolean isCorrect = true;
        for (int i = 1; i < len - 1; i++) {
            char c = s.charAt(i);
            if (c != 32 && c != 33 && !(c >= 40 && c <= 126)) {
                if (c == '%') {
                    if (i < len - 2 && s.charAt(i + 1) == 'd') {
                        continue;
                    }
                    else {
                        isCorrect = false;
                        break;
                    }
                }
                isCorrect = false;
                break;
            }
            if (c == 92 && (i >= len - 2 || s.charAt(i + 1) != 'n')) {
                isCorrect = false;
                break;
            }
        }
        if (!isCorrect) {
            Error error = new Error(printf.getLineNum(), ErrorType.a);
            ErrorTable.addError(error);
        }
    }

    public void checkErrorH(LVal lVal) {
        Symbol symbol = curSymbolTable.getSymbol(lVal.getIdent().getVal());
        if (symbol == null) { // 未定义
            return;
        }
        if (symbol instanceof SymbolCon) {
            Error error = new Error(lVal.getIdent().getLineNum(), ErrorType.h);
            ErrorTable.addError(error);
        }
    }

    public void checkErrorI() {
        if (iterator.preRead(1).getLexType() == LexType.SEMICN) {
            iterator.read(); // ;
        }
        else {
            Error error = new Error(iterator.readLast().getLineNum(), ErrorType.i);
            ErrorTable.addError(error);
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

    public boolean isNextExp() {
        LexType lexType = iterator.preRead(1).getLexType();
        if (lexType == LexType.PLUS || lexType == LexType.MINU || lexType == LexType.NOT
                || lexType == LexType.LPARENT || lexType == LexType.IDENFR || lexType == LexType.INTCON) {
            return true;
        }
        return false;
    }

}
