package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.Type;

public class AllocInst extends Instruction{

    public Type allocType;

    public AllocInst(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands) {
        super(parent, insertBefore, resultType, resultName, instType, numUserOperands, userOperands);
    }

    public static AllocInst create(BasicBlock parent, Instruction insertBefore, String resultName, Type allocType) {
        AllocInst allocInst = new AllocInst(parent, insertBefore, Type.pointerType(allocType), resultName, InstType.ALLOCA, 0);
        allocInst.allocType = allocType;
        return allocInst;
    }

    public static AllocInst create(BasicBlock parent, String resultName, Type allocType) {
        AllocInst allocInst = new AllocInst(parent, null, Type.pointerType(allocType), resultName, InstType.ALLOCA, 0);
        allocInst.allocType = allocType;
        return allocInst;
    }

    @Override
    public String toString() {
        return this.name + " = alloc "+allocType;
    }
}
