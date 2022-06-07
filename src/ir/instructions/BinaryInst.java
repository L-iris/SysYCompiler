package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.Type;

public class BinaryInst extends Instruction{


    public BinaryInst(Type resultType, String resultName, InstType instType, Value userOperand1, Value userOperand2) {
        super(resultType, resultName, instType, 2, userOperand1, userOperand2);
        assert userOperand1.getType().equals(userOperand2.getType());
        assert resultType.equals(userOperand1.getType());
    }

    public static BinaryInst create(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, Value userOperand1, Value userOperand2) {
        BinaryInst binaryInst = new BinaryInst(resultType, resultName, instType, userOperand1, userOperand2);
        parent.instructionIList.insertBefore(binaryInst, insertBefore);
        return binaryInst;
    }

    public static BinaryInst create(BasicBlock parent, Type resultType, String resultName, InstType instType, Value userOperand1, Value userOperand2) {
        BinaryInst binaryInst = new BinaryInst(resultType, resultName, instType, userOperand1, userOperand2);
        parent.instructionIList.insertAtEnd(binaryInst);
        return binaryInst;
    }

    public Value getOperand1() {
        return this.userOperands.get(0);
    }

    public Value getOperand2() {
        return this.userOperands.get(1);
    }
}
