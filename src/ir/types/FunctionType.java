package ir.types;

import java.util.List;

public class FunctionType extends Type{

    private Type retType;
    private List<Type> paramTypes;

    public FunctionType(Type retType, List<Type> paramTypes) {
        super(TypeID.FunctionTyID);
        this.retType = retType;
        this.paramTypes = paramTypes;
    }

    public Type getRetType() {
        return retType;
    }

    public List<Type> getParamTypes() {
        return paramTypes;
    }

    public Type getParamType(int i) {
        return paramTypes.get(i);
    }

    public int getNumOfParams() {
        return paramTypes.size();
    }
}
