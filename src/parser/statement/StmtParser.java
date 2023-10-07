package parser.statement;

import error.Error;
import error.ErrorType;
import lexer.LexType;
import lexer.LexerIterator;
import lexer.Token;
import parser.expression.*;

import java.util.ArrayList;

public class StmtParser {
    LexerIterator iterator;

    public StmtParser(LexerIterator iterator) {
        this.iterator = iterator;
    }


    public Stmt parseStmt() {
        Stmt stmt;
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
            BlockParser blockParser = new BlockParser(iterator);
            stmt = blockParser.parseBlock();
        }
        else if (iterator.preRead(1).getLexType() == LexType.SEMICN) {
            iterator.read();
            stmt = new Stmt();
        }
        else if (iterator.preRead(1).getLexType() == LexType.IDENFR) {
            int prePos = iterator.getPos();
            ExpParser expParser = new ExpParser(iterator);
            iterator.setPrintOrNot(false);
            expParser.setPrintOrNot(false);
            expParser.parseExp();
            iterator.setPrintOrNot(true);
            expParser.setPrintOrNot(true);
            if (iterator.preRead(1).getLexType() == LexType.ASSIGN) {
                if (iterator.preRead(2).getLexType() == LexType.GETINTTK) {
                    iterator.setPos(prePos);
                    LVal lVal = expParser.parseLVal();
                    iterator.read(); // =
                    iterator.read(); // getint
                    iterator.read(); // (
                    //iterator.read(); // )
                    judgeErrorJ();
                    //iterator.read(); // ;
                    stmt = new StmtGetInt(lVal);
                }
                else {
                    iterator.setPos(prePos);
                    LVal lVal = expParser.parseLVal();
                    iterator.read(); // =
                    Exp exp = expParser.parseExp();
                    //iterator.read(); // ;
                    stmt = new StmtAssign(lVal, exp);
                }
            }
            else {
                iterator.setPos(prePos);
                Exp exp = expParser.parseExp();
                //iterator.read(); // ;
                stmt = new StmtExp(exp);
            }
            judgeErrorI();
        }
        else {
            stmt = null;
        }
        System.out.println("<Stmt>");
        return stmt;
    }


    public StmtIf parseStmtIf() {
        CondParser condParser = new CondParser(iterator);
        StmtIf stmtIf;
        iterator.read(); // if
        iterator.read(); // (
        Cond cond = condParser.parseCond();
        //iterator.read(); // )
        judgeErrorJ();
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
        CondParser condParser = new CondParser(iterator);
        ForStmt forStmt1 = null;
        ForStmt forStmt2 = null;
        Cond cond = null;
        iterator.read(); // for
        iterator.read(); // (
        if (iterator.preRead(1).getLexType() != LexType.SEMICN) {
            forStmt1 = parseForStmt();
        }
        iterator.read(); // ; todo
        if (iterator.preRead(1).getLexType() != LexType.SEMICN) {
            cond = condParser.parseCond(); //parseCond
        }
        iterator.read(); // ;
        if (iterator.preRead(1).getLexType() != LexType.RPARENT) {
            forStmt2 = parseForStmt();
        }
        //iterator.read(); // )
        judgeErrorJ();
        Stmt stmt = parseStmt();
        return new StmtFor(forStmt1, cond, forStmt2, stmt);
    }

    public ForStmt parseForStmt() {
        ExpParser expParser = new ExpParser(iterator);
        LVal lVal = expParser.parseLVal();
        iterator.read(); // =
        Exp exp = expParser.parseExp();
        System.out.println("<ForStmt>");
        return new ForStmt(lVal, exp);
    }

    public StmtBreak parseStmtBreak() {
        iterator.read(); // break
        //iterator.read(); // ;
        judgeErrorI();
        return new StmtBreak();
    }

    public StmtContinue parseStmtContinue() {
        iterator.read(); // continue
        //iterator.read(); // ;
        judgeErrorI();
        return new StmtContinue();
    }

    public StmtReturn parseStmtReturn() {
        ExpParser expParser = new ExpParser(iterator);
        iterator.read(); // return
        Exp exp = null;
        if (iterator.preRead(1).getLexType() != LexType.SEMICN) {
            exp = expParser.parseExp(); //parseExp
        }
        //iterator.read(); // ;
        judgeErrorI();
        return new StmtReturn(exp);
    }

    public StmtPrintf parseStmtPrintf() {
        ExpParser expParser = new ExpParser(iterator);
        ArrayList<Exp> exps = new ArrayList<>();
        iterator.read(); // printf
        iterator.read(); // (
        Token formatString = iterator.read(); // formatString
        while (iterator.preRead(1).getLexType() == LexType.COMMA) {
            iterator.read(); // ,
            exps.add(expParser.parseExp());//parseExp
        }
        //iterator.read(); // )
        judgeErrorJ();
        //iterator.read(); // ;
        judgeErrorI();
        return new StmtPrintf(formatString, exps);
    }

    public void judgeErrorI() {
        if (iterator.preRead(1).getLexType() == LexType.SEMICN) {
            iterator.read(); // ;
        }
        else {
            Error error = new Error(iterator.getCurLineNum(), ErrorType.i);
        }
    }

    public void judgeErrorJ() {
        if (iterator.preRead(1).getLexType() == LexType.RPARENT) {
            iterator.read();
        }
        else {
            Error error = new Error(iterator.getCurLineNum(), ErrorType.j);
        }
    }

}
