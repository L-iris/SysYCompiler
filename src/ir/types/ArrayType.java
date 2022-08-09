package ir.types;

public class ArrayType extends Type{
    // Number of elements in the array.
    private final int numElements;
    private Type containedType;

    protected ArrayType(Type containedType, int numElements) {
        super(TypeID.ArrayTyID);
        this.numElements = numElements;
    }

    public int getNumElements() {
        return numElements;
    }

    public Type getContainedType() {
        return this.containedType;
    }

    public void setContainedType(Type containedType) {
        this.containedType = containedType;
    }

    @Override
    public String toString() {
        return "[ "+this.numElements + " * " + this.containedType+" ]";
    }
}
