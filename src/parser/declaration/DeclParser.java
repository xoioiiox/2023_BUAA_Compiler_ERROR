package parser.declaration;

import error.*;
import error.Error;
import lexer.LexType;
import lexer.LexerIterator;
import lexer.Token;
import parser.expression.ConstExp;
import parser.expression.Exp;
import parser.expression.ExpParser;

import java.util.ArrayList;

public class DeclParser {
    LexerIterator iterator;
    private SymbolTable curSymbolTable;

    public DeclParser (LexerIterator iterator, SymbolTable curSymbolTable) {
        this.iterator = iterator;
        this.curSymbolTable = curSymbolTable;
    }

    public Decl parseDecl () {
        Decl decl;
        if (iterator.preRead(1).getLexType() == LexType.CONSTTK) {
            decl = parseConstDecl();
        }
        else {
            decl = parseVarDecl();
        }
        return decl;
    }

    public ConstDecl parseConstDecl() {
        ArrayList<ConstDef> constDefs = new ArrayList<>();
        iterator.read(); // const
        iterator.read(); // int
        Btype btype = new Btype("int");
        constDefs.add(parseConstDef());
        while (iterator.preRead(1).getLexType() == LexType.COMMA) {
            iterator.read(); // ,
            constDefs.add(parseConstDef());
        }
        //iterator.read(); // ;
        checkErrorI();
        System.out.println("<ConstDecl>");
        return new ConstDecl(btype, constDefs);
    }

    public ConstDef parseConstDef() {
        ExpParser expParser = new ExpParser(iterator, curSymbolTable);
        Token Ident = iterator.read(); // ident
        ArrayList<ConstExp> constExps = new ArrayList<>();
        int cnt = 0;
        while (iterator.preRead(1).getLexType() == LexType.LBRACK) {
            cnt++;
            iterator.read(); // [
            expParser.parseConstExp();
            //iterator.read(); // ]
            checkErrorK();
        }
        if (curSymbolTable.checkReName(Ident.getVal())) {
            Error error = new Error(Ident.getLineNum(), ErrorType.b);
            ErrorTable.addError(error);
        }
        SymbolCon symbolCon = new SymbolCon(Ident.getVal(), Ident.getLineNum(), cnt);
        curSymbolTable.addSymbol(symbolCon);
        iterator.read(); // =
        ConstInitVal constInitVal = parseConstInitVal();
        System.out.println("<ConstDef>");
        return new ConstDef(Ident, constExps, constInitVal);
    }

    public ConstInitVal parseConstInitVal() {
        ExpParser expParser = new ExpParser(iterator, curSymbolTable);
        ConstInitVal constInitVal;
        if (iterator.preRead(1).getLexType() == LexType.LBRACE) {
            iterator.read(); // {
            if (iterator.preRead(1).getLexType() == LexType.RBRACE) {
                iterator.read(); // }
                constInitVal = new ConstInitVal();
            }
            else {
                ArrayList<ConstInitVal> constInitVals = new ArrayList<>();
                constInitVals.add(parseConstInitVal());
                while (iterator.preRead(1).getLexType() == LexType.COMMA) {
                    iterator.read(); // ,
                    constInitVals.add(parseConstInitVal());
                }
                iterator.read(); // }
                constInitVal = new ConstInitVal(constInitVals);
            }
        }
        else {
            ConstExp constExp = expParser.parseConstExp();
            constInitVal = new ConstInitVal(constExp);
        }
        System.out.println("<ConstInitVal>");
        return constInitVal;
    }

    public VarDecl parseVarDecl() {
        ArrayList<VarDef> varDefs = new ArrayList<>();
        iterator.read(); //btype
        Btype btype = new Btype("int");
        varDefs.add(parseVarDef());
        while (iterator.preRead(1).getLexType() == LexType.COMMA) {
            iterator.read(); // ,
            varDefs.add(parseVarDef());
        }
        //iterator.read(); // ;
        checkErrorI();
        System.out.println("<VarDecl>");
        return new VarDecl(btype, varDefs);
    }

    public VarDef parseVarDef() {
        ExpParser expParser = new ExpParser(iterator, curSymbolTable);
        ArrayList<ConstExp> constExps = new ArrayList<>();
        InitVal initVal = null;
        Token Ident = iterator.read(); // ident
        int cnt = 0;
        while (iterator.preRead(1).getLexType() == LexType.LBRACK) {
            cnt++;
            iterator.read(); // [
            constExps.add(expParser.parseConstExp());
            //iterator.read(); // ]
            checkErrorK();
        }
        if (curSymbolTable.checkReName(Ident.getVal())) {
            Error error = new Error(Ident.getLineNum(), ErrorType.b);
            ErrorTable.addError(error);
        }
        SymbolVar symbolVar = new SymbolVar(Ident.getVal(), Ident.getLineNum(), cnt);
        curSymbolTable.addSymbol(symbolVar);
        if (iterator.preRead(1).getLexType() == LexType.ASSIGN) {
            iterator.read(); // =
            initVal = parseInitVal();
        }
        System.out.println("<VarDef>");
        return new VarDef(Ident, constExps, initVal);
    }

    public InitVal parseInitVal() {
        ExpParser expParser = new ExpParser(iterator, curSymbolTable);
        InitVal initVal;
        if (iterator.preRead(1).getLexType() == LexType.LBRACE) {
            iterator.read(); // {
            if (iterator.preRead(1).getLexType() == LexType.RBRACE) {
                iterator.read(); // }
                initVal = new InitVal();
            }
            else {
                ArrayList<InitVal> initVals = new ArrayList<>();
                initVals.add(parseInitVal());
                while (iterator.preRead(1).getLexType() == LexType.COMMA) {
                    iterator.read(); // ,
                    initVals.add(parseInitVal());
                }
                iterator.read(); // }
                initVal = new InitVal(initVals);
            }
        }
        else {
            Exp exp = expParser.parseExp();
            initVal = new InitVal(exp);
        }
        System.out.println("<InitVal>");
        return initVal;
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

    public void checkErrorK() {
        if (iterator.preRead(1).getLexType() == LexType.RBRACK) {
            iterator.read();
        }
        else {
            Error error = new Error(iterator.readLast().getLineNum(), ErrorType.k);
            ErrorTable.addError(error);
        }
    }

}
