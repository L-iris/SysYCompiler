package ir.types;

public class VoidType extends Type{

    public static VoidType voidType = new VoidType();

    private VoidType() {
        super(TypeID.VoidTyID);
    }
}
