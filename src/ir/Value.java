package ir;

import ir.types.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Value is the base class of all values computed by a program that may be used as operands to other values.
 * Every value has a "use list" that keeps track of which other Values are using this Value.
 */
public class Value {

    protected Type type;
    protected List<Use> useList;
    protected String name;

    public Value(Type type, List<Use> useList, String name) {
        this.type = type;
        this.useList = useList;
        this.name = name;
    }

    public Value(Type type, String name) {
        this.type = type;
        this.useList = new ArrayList<Use>();
        this.name = name;
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

    public String getName() {
        return name;
    }
}
