package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.Type;

public class AllocaInst extends Instruction{

    public Type allocType;

    public AllocaInst(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands) {
        super(parent, insertBefore, resultType, resultName, instType, numUserOperands, userOperands);
    }

    public static AllocaInst create(BasicBlock parent, Instruction insertBefore, String resultName, Type allocType) {
        AllocaInst allocaInst = new AllocaInst(parent, insertBefore, Type.pointerType(allocType), resultName, InstType.ALLOCA, 0);
        allocaInst.allocType = allocType;
        return allocaInst;
    }

    public static AllocaInst create(BasicBlock parent, String resultName, Type allocType) {
        AllocaInst allocaInst = new AllocaInst(parent, null, Type.pointerType(allocType), resultName, InstType.ALLOCA, 0);
        allocaInst.allocType = allocType;
        return allocaInst;
    }

}
