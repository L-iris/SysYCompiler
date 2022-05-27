package ir;

import ir.types.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Value is the base class of all values computed by a program that may be used as operands to other values.
 * Every value has a "use list" that keeps track of which other Values are using this Value.
 */
public class Value {

    private Type type;
    private List<Use> useList;

    public Value(Type type, List<Use> useList) {
        this.type = type;
        this.useList = useList;
    }

    public Value(Type type) {
        this.type = type;
        this.useList = new ArrayList<Use>();
    }

    public Type getType() {
        return type;
    }

    public List<Use> getUseList() {
        return useList;
    }

}
