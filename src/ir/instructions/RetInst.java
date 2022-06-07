package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.Type;

public class RetInst extends Instruction{

    public RetInst(BasicBlock parent, Instruction insertBefore, Value userOperand) {
        super(parent, insertBefore, null, null, InstType.RET, 1, userOperand);
    }

    public RetInst(BasicBlock parent, Instruction insertBefore) {
        super(parent, insertBefore, null, null, InstType.RET, 1);
    }

    public static RetInst create(BasicBlock parent, Instruction insertBefore, Value userOperand) {
        return new RetInst(parent, insertBefore, userOperand);
    }

    public static RetInst create(BasicBlock parent, Value userOperand) {
        return new RetInst(parent, null, userOperand);
    }

    public static RetInst create(BasicBlock parent, Instruction insertBefore) {
        return new RetInst(parent, insertBefore);
    }

    public static RetInst create(BasicBlock parent) {
        return new RetInst(parent, null);
    }

    public Type getRetType() {
        if(userOperands.isEmpty())
            return Type.voidType();
        return this.userOperands.get(0).getType();
    }

    public Value getRetValue() {
        if(userOperands.isEmpty())
            return null;
        return this.userOperands.get(0);
    }
}
