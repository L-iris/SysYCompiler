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
        PointerType,
    }

    protected final TypeID typeID;

    public Type(TypeID typeID) {
        this.typeID = typeID;
    }

    public TypeID getTypeID() {
        return typeID;
    }

    public static VoidType voidType() {
        return VoidType.voidType;
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

    public static PointerType pointerType(Type containedType) {
        return new PointerType(containedType);
    }

    public static FunctionType functionType(Type retType, Type... paramTypes) {
        return new FunctionType(retType, List.of(paramTypes));
    }

    public static FunctionType functionType(Type retType) {
        return new FunctionType(retType, new ArrayList<>(0));
    }
}
