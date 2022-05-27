package ir.types;

public class IntegerType extends Type{
    private int numBits;

    protected static final IntegerType i32 = new IntegerType(32);
    protected static final IntegerType i1 = new IntegerType(1);

    public IntegerType(int numBits) {
        super(TypeID.IntegerTyID);
        this.numBits = numBits;
    }

    public IntegerType() {
        super(TypeID.IntegerTyID);
        this.numBits = 32;
    }
}
