package ir;

import ir.types.Type;

public class Arg extends Value implements Comparable<Arg>{
    private int pos;

    public Arg(int pos, Type type, String name) {
        super(type, name);
        this.pos = pos;
    }

    public static Arg create(int pos, Type type, String name) {
        return new Arg(pos, type, name);
    }

    public int getPos() {
        return pos;
    }

    @Override
    public int compareTo(Arg o) {
        return this.pos - o.pos;
    }

    public boolean setPos(int i) {
        this.pos = i;
        return true;
    }
}
