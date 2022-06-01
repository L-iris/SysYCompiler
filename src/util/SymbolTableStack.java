package util;

import ir.Value;

import java.util.ArrayList;
import java.util.List;

public class SymbolTableStack {
    private List<SymbolTable> symbolTableList;

    public SymbolTableStack() {
        this.symbolTableList = new ArrayList<>();
    }

    public SymbolTable currentSymbolTable() {
        return symbolTableList.get(symbolTableList.size() - 1);
    }

    public SymbolTable enterScope() {
        var tmp = new SymbolTable();
        symbolTableList.add(tmp);
        return tmp;
    }

    public SymbolTable exitScope() {
        return symbolTableList.remove(symbolTableList.size() - 1);
    }

    public SymbolTable findSymbolTable(String name) {
        for(int i = symbolTableList.size() - 1; i >= 0; i--)
            if(symbolTableList.get(i).find(name) != null)
                return symbolTableList.get(i);
        return null;
    }

    public Value findValue(String name) {
        return this.findSymbolTable(name) == null? null : findSymbolTable(name).find(name);
    }

    public boolean addValue(Value value) {
        return currentSymbolTable().addValue(value);
    }

    public boolean isGlobal() {
        return this.symbolTableList.size() == 1;
    }
}