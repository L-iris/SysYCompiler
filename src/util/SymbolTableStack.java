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

    public Value findValueCurrentScope(String name) {
        SymbolTable symbolTable = findSymbolTable(name);
        if(symbolTable == this.currentSymbolTable()) {
            return findValue(name);
        }
        return null;
    }

    public boolean addValue(String name, Value value) {
        return currentSymbolTable().addValue(name, value);
    }

    public boolean isGlobal() {
        return this.symbolTableList.size() == 1;
    }
}
