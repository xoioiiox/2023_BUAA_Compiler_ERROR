package parser.declaration;

import parser.expression.ConstExp;

import java.util.ArrayList;

public class ConstInitVal {
    ConstExp constExp;
    ArrayList<ConstInitVal> constInitVals;

    public ConstInitVal() {

    }

    public ConstInitVal(ConstExp constExp) {
        this.constExp = constExp;
    }

    public ConstInitVal(ArrayList<ConstInitVal> constInitVals) {
        this.constInitVals = constInitVals;
    }
}
