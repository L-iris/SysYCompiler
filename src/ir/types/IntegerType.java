package ir.types;

public class IntegerType extends Type{
    private int numBits;

    public static final IntegerType i32 = new IntegerType(32);

    public IntegerType(int numBits) {
        super(TypeID.IntegerTyID);
        this.numBits = numBits;
    }

    public IntegerType() {
        super(TypeID.IntegerTyID);
        this.numBits = 32;
    }

    @Override
    public String toString() {
        return "i32";
    }
}
