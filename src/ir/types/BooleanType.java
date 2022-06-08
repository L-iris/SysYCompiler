package ir.types;

public class BooleanType extends Type{
    protected static BooleanType i1 = new BooleanType();
    private BooleanType() {
        super(TypeID.BooleanTyID);
    }
}
