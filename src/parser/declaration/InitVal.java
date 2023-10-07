package parser.declaration;

import parser.expression.Exp;

import java.util.ArrayList;

public class InitVal {
    Exp exp;
    ArrayList<InitVal> initVals;
    public InitVal() {

    }

    public InitVal(Exp exp) {
        this.exp = exp;
    }

    public InitVal(ArrayList<InitVal> initVals) {
        this.initVals = initVals;
    }
}
