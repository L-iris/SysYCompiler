package ir.types;

public class PointerType extends Type{

    private Type containedType;

    public PointerType(Type containedType) {
        super(TypeID.PointerType);
        this.containedType = containedType;
    }

    protected  PointerType(TypeID typeID) {
        super(typeID);
    }

    public Type getContainedType() {
        return this.containedType;
    }

    public void setContainedType(Type containedType) {
        this.containedType = containedType;
    }
}
