package ir;

import ir.types.Type;

public class Arg extends Value{
    private int pos;

    public Arg(int pos, Type type, String name) {
        super(type, name);
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
