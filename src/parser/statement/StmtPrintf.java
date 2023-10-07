package parser.statement;

import lexer.Token;
import parser.expression.Exp;

import java.util.ArrayList;

public class StmtPrintf extends Stmt {
    Token formatString;
    ArrayList<Exp> exps;

    public StmtPrintf(Token formatString, ArrayList<Exp> exps) {
        this.formatString = formatString;
        this.exps = exps;
    }
}
