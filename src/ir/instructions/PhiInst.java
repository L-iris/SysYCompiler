package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.Type;

public class PhiInst extends Instruction{
    public PhiInst(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, int numUserOperands, Value... userOperands) {
        super(parent, insertBefore, resultType, resultName, InstType.PHI, numUserOperands, userOperands);
    }

    public static  PhiInst create(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, int numUserOperands, Value... userOperands) {
        return new PhiInst(parent, insertBefore, resultType, resultName, numUserOperands, userOperands);
    }

    public Value getValue(int i) {
        return this.userOperands.get(2*i);
    }

    public Value getLabel(int i) {
        return this.userOperands.get(2*i + 1);
    }
}
