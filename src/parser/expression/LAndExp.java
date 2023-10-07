package parser.expression;

import java.util.ArrayList;

public class LAndExp {
    ArrayList<EqExp> eqExps;

    public LAndExp(ArrayList<EqExp> eqExps) {
        this.eqExps = eqExps;
    }
}
