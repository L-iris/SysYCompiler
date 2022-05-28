package ir.types;

import java.util.ArrayList;
import java.util.List;

public class Type {

    public enum TypeID {
        IntegerTyID,        ///< Arbitrary bit width integers
        FloatTyID,
        FunctionTyID,       ///< Functions
        LabelTyID,
        ArrayTyID,          ///< Arrays
        VoidTyID,
        BooleanTyID,
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

    public static BooleanType i1() {
        return BooleanType.i1;
    }

    public static FloatType f32(){
        return FloatType.f32;
    }

    public static LabelType labelType() {
        return LabelType.labelType;
    }

    public static ArrayType arrayType(Type containedType, int numElements) {
        return new ArrayType(containedType, numElements);
    }

    public static ArrayType arrayType(Type containedType) {
        return new ArrayType(containedType, 0);
    }

    public static FunctionType functionType(Type retType, Type... paramTypes) {
        return new FunctionType(retType, List.of(paramTypes));
    }

    public static FunctionType functionType(Type retType) {
        return new FunctionType(retType, new ArrayList<>(0));
    }
}
