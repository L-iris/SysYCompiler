package util;

import ir.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private Map<String, Value> stringValueMap;

    public SymbolTable() {
        this.stringValueMap = new HashMap<>();
    }

    public Value find(String name) {
        return this.stringValueMap.get(name);
    }

    public boolean addValue(String name, Value value) {
        if(this.stringValueMap.containsKey(name))
            return false;
        return this.stringValueMap.put(name, value) == null;
    }
}
