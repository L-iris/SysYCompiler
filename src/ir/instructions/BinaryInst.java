package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.Type;

import java.util.List;

public class BinaryInst extends Instruction{


    public BinaryInst(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands) {
        super(parent, insertBefore, resultType, resultName, instType, numUserOperands, userOperands);
    }

    public static BinaryInst create(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, Value userOperand1, Value userOperand2) {
        assert userOperand1.getTypeID() == userOperand2.getTypeID();
        assert userOperand1.getTypeID() == resultType.getTypeID();
        Value[] operands = new Value[2];
        operands[0] = userOperand1;
        operands[1] = userOperand2;
        return new BinaryInst(parent, insertBefore, resultType, resultName, instType, operands.length, operands);
    }

    public static BinaryInst create(BasicBlock parent, Type resultType, String resultName, InstType instType, Value userOperand1, Value userOperand2) {
        return create(parent, null, resultType, resultName, instType, userOperand1, userOperand2);
    }

    public static BinaryInst create(BasicBlock parent, String resultName, InstType instType, Value userOperand1, Value userOperand2) {
        return create(parent, userOperand1.getType(), resultName, instType, userOperand1, userOperand2);
    }

    public Value getOperand1() {
        return this.userOperands.get(0);
    }

    public Value getOperand2() {
        return this.userOperands.get(1);
    }
}
