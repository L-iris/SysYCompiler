package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.Type;

public class CmpInst extends Instruction{
    public CmpInst(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands) {
        super(parent, insertBefore, resultType, resultName, instType, numUserOperands, userOperands);
    }

    public static CmpInst create(BasicBlock basicBlock, Instruction insertBefore, String resultName, InstType instType, Value userOperand1, Value userOperand2){
        assert userOperand1.getType().equals(userOperand2.getType());
        return new CmpInst(basicBlock, insertBefore, Type.i1(), resultName, instType, 2, userOperand1, userOperand2);
    }

    public static CmpInst create(BasicBlock basicBlock, String resultName, InstType instType, Value userOperand1, Value userOperand2){
        return create(basicBlock, null, resultName, instType, userOperand1, userOperand2);
    }
}
