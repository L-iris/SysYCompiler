package ir.types;

public class ArrayType extends PointerType{
    // Number of elements in the array.
    private int numElements;

    protected ArrayType(Type containedType, int numElements) {
        super(TypeID.ArrayTyID);
        this.numElements = numElements;
    }

    public int getNumElements() {
        return numElements;
    }
}
