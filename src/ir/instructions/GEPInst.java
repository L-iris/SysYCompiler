package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.ArrayType;
import ir.types.PointerType;
import ir.types.Type;

import java.util.Arrays;

public class GEPInst extends Instruction{
    public GEPInst(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands) {
        super(parent, insertBefore, resultType, resultName, instType, numUserOperands, userOperands);
    }

    public static GEPInst create(BasicBlock basicBlock, Instruction insertBefore, String resultName, Value pointerToArray, Value... index) {
        assert pointerToArray.getTypeID() == Type.TypeID.PointerType;
        assert ((PointerType) pointerToArray.getType()).getContainedType().getTypeID() == Type.TypeID.ArrayTyID;

        Type resultType = ((PointerType) pointerToArray.getType()).getContainedType();
        for(int i=1;i<index.length;i++){
            resultType = ((ArrayType) resultType).getContainedType();
        }
        resultType = Type.pointerType(resultType);
        Value[] op = new Value[index.length + 1];
        op[0] = pointerToArray;
        for(int i=1;i<op.length;i++){
            op[i] = index[i-1];
        }
        return new GEPInst(basicBlock, insertBefore, resultType, resultName, InstType.GEP, op.length, op);
    }

    public static GEPInst create(BasicBlock basicBlock, String resultName, Value pointerToArray, Value... index) {
        return create(basicBlock, null, resultName, pointerToArray, index);
    }
}
