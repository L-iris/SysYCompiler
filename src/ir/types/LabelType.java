package ir.types;

public class LabelType extends Type{

    protected static final LabelType labelType = new LabelType();
    public LabelType() {
        super(TypeID.LabelTyID);
    }
}
