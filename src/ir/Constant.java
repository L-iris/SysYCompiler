package ir;

import ir.types.Type;

import java.util.List;

/**
 * A constant is a value that is immutable at runtime.
 * Functions are constants because their address is immutable.
 * Same with global variables.
 */
public class Constant extends User{

//    public Constant(Type type, String name, int numUserOperands, List<Value> userOperands, List<Use> useList) {
//        super(type, name, numUserOperands, userOperands, useList);
//    }

    public Constant(Type type, String name, int numUserOperands, Value... userOperands) {
        super(type, name, numUserOperands, userOperands);
    }

    public Constant(Type type, String name, int numUserOperands) {
        super(type, name, numUserOperands);
    }

    public Constant(Type type, String name) {
        super(type, name);
    }

    public Constant(Type type) {
        super(type);
    }
}
