package ir.constval;

import ir.Value;
import ir.types.Type;

public class ConstFloat extends Value {
    public float value;

    public ConstFloat(float value) {
        super(Type.f32());
        this.value = value;
    }
}