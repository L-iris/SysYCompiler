package ir.types;

public class ArrayType extends Type{
    // The element type of the array.
    private Type ContainedType;
    // Number of elements in the array.
    private int numElements;

    public ArrayType(TypeID typeID, Type containedType, int numElements) {
        super(TypeID.ArrayTyID);
        ContainedType = containedType;
        this.numElements = numElements;
    }

    public Type getContainedType() {
        return ContainedType;
    }

    public int getNumElements() {
        return numElements;
    }
}
