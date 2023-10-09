package error;

import java.util.ArrayList;

public class SymbolFunc extends Symbol{
    private int reType; // -1:void, 0:int
    private ArrayList<SymbolVar> params = new ArrayList<>();

    public SymbolFunc(String name, int lineNum, int reType) {
        super(name, lineNum);
        this.reType = reType;
    }

    public int getReType() {
        return reType;
    }

    public void addParams(SymbolVar symbolVar) {
        this.params.add(symbolVar);
    }

    public int getParamsNum() {
        return params.size();
    }

    public ArrayList<SymbolVar> getParams() {
        return params;
    }
}
