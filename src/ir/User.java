package ir;

import ir.types.Type;

import java.util.List;

/**
 * This class defines the interface that one who uses a Value must implement.
 * Each instance of the Value class keeps track of what User's have handles to it.
 * * Instructions are the largest class of Users.
 * * Constants may be users of other constants (think arrays and stuff)
 */
public class User extends Value{

    protected List<Value> userOperands;
    protected int numUserOperands;

    public User(Type type, String name, List<Use> useList, int numUserOperands) {
        super(type, useList, name);
        this.numUserOperands = numUserOperands;
    }

    public User(Type type, String name, int numUserOperands) {
        super(type, name);
        this.numUserOperands = numUserOperands;
    }

    public User(Type type) {
        super(type);
        this.numUserOperands = 0;
    }
}
