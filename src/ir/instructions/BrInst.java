package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.Type;

public class BrInst extends Instruction{
    public BrInst(BasicBlock parent, Instruction insertBefore , int numUserOperands, Value... userOperands) {
        super(parent, insertBefore, null, null, InstType.BR, numUserOperands, userOperands);
    }

    public static BrInst create(BasicBlock parent, Instruction insertBefore, Value label) {
        assert label.getType().equals(Type.labelType());
        return new BrInst(parent, insertBefore, 1, label);
    }

    public static BrInst create(BasicBlock parent, Value label) {
        return create(parent, null, label);
    }

    public static BrInst create(BasicBlock parent, Instruction insertBefore, Value cond, Value trueBr, Value falseBr) {
        assert cond.getType().equals(Type.i1());
        assert trueBr.getType().equals(Type.labelType());
        assert falseBr.getType().equals(Type.labelType());
        return new BrInst(parent, insertBefore, 3, cond, trueBr, falseBr);
    }

    public static BrInst create(BasicBlock parent, Value cond, Value trueBr, Value falseBr) {
        return create(parent, null, cond, trueBr, falseBr);
    }

    public boolean isUnconditional() {
        return this.userOperands.size() == 1;
    }

    public Value getConditional() {
        if(isUnconditional())
            return null;
        return userOperands.get(0);
    }

    public Value getTrueBranch() {
        if(isUnconditional())
            return null;
        return userOperands.get(1);
    }

    public Value getFalseBranch() {
        if(isUnconditional())
            return null;
        return userOperands.get(2);
    }

    public Value getBranch() {
        if(isUnconditional())
            return userOperands.get(0);
        return null;
    }
}
