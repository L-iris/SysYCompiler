package ir.types;

public class Type {

    public enum TypeID {
        IntegerTyID,        ///< Arbitrary bit width integers
        FloatTyID,
        FunctionTyID,       ///< Functions
        LabelTyID,
        ArrayTyID,          ///< Arrays
    }

    protected final TypeID typeID;

    public Type(TypeID typeID) {
        this.typeID = typeID;
    }

    public TypeID getType() {
        return typeID;
    }

    public static IntegerType i32() {
        return IntegerType.i32;
    }

    public static IntegerType i1() {
        return IntegerType.i1;
    }

    public static FloatType f32(){
        return FloatType.f32;
    }

    public static LabelType labelType() {
        return LabelType.labelType;
    }
}
