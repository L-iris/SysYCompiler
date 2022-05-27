package ir;

import ir.types.Type;

/**
 * A constant is a value that is immutable at runtime.
 * Functions are constants because their address is immutable.
 * Same with global variables.
 */
public class Constant extends User{

    public Constant(Type type) {
        super(type);
    }
}
