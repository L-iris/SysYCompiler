package ir.types;

public class FloatType extends Type{
    private int numBits;

    protected static final FloatType f32 = new FloatType(32);

    public FloatType(int numBits) {
        super(TypeID.FloatTyID);
        this.numBits = numBits;
    }

    public FloatType() {
        super(TypeID.FloatTyID);
        this.numBits = 32;
    }

    @Override
    public String toString() {
        return "f32";
    }
}
