package util;

import ir.Value;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    private List<Value> valueList;

    public SymbolTable() {
        this.valueList = new ArrayList<>();
    }

    public Value find(String name) {
        for(var v : valueList)
            if(name.equals(v.getName()))
                return v;
        return null;
    }

    public boolean addValue(Value value) {
        if(find(value.getName()) != null)
            return false;
        return valueList.add(value);
    }
}
