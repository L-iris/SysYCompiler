package ir.constval;

import ir.Value;
import ir.types.Type;

public class ConstInt extends Value {
    public int value;

    public ConstInt(int value) {
        super(Type.i32());
        this.value = value;
    }
}
