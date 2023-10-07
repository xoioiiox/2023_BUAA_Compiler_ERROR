package parser.expression;

import java.util.ArrayList;

public class LOrExp {
    ArrayList<LAndExp> lAndExps;

    public LOrExp(ArrayList<LAndExp> lAndExps) {
        this.lAndExps = lAndExps;
    }
}
