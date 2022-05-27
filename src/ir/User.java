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

    private int numUserOperands;

    public User(Type type, List<Use> useList, int numUserOperands) {
        super(type, useList);
        this.numUserOperands = numUserOperands;
    }

    public User(Type type, int numUserOperands) {
        super(type);
        this.numUserOperands = numUserOperands;
    }

    public User(Type type) {
        super(type);
        this.numUserOperands = 0;
    }
}
