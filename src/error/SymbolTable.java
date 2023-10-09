package error;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Symbol> symbolMap = new HashMap<>();
    SymbolTable prevSymbolTable;

    public SymbolTable(SymbolTable prevSymbolTable) {
        this.prevSymbolTable = prevSymbolTable;
    }

    public void addSymbol(Symbol symbol) {
        this.symbolMap.put(symbol.getName(), symbol);
    }

    public boolean checkReName(String name) {
        return symbolMap.containsKey(name);
    }

    public SymbolTable getPrevSymbolTable() {
        return prevSymbolTable;
    }

    public Symbol getSymbol(String name) {
        if (this.symbolMap.containsKey(name)) {
            return symbolMap.get(name);
        }
        if (this.prevSymbolTable != null) {
            return this.prevSymbolTable.getSymbol(name);
        }
        return null;
    }
}
