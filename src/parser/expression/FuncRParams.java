package parser.expression;

import java.util.ArrayList;

public class FuncRParams {
    private ArrayList<Exp> exps;
    public FuncRParams(ArrayList<Exp> exps) {
        this.exps = exps;
    }

    public int getParamsNum() {
        return exps.size();
    }

    public ArrayList<Exp> getExps() {
        return exps;
    }
}
