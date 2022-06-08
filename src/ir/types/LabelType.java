package ir.types;

public class LabelType extends Type{

    public static final LabelType labelType = new LabelType();
    private LabelType() {
        super(TypeID.LabelTyID);
    }
}
