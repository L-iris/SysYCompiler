package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.Type;

public class ConvertInst extends Instruction{

    public Type targetType;

    public ConvertInst(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, Value value, Type targetType) {
        super(parent, insertBefore, resultType, resultName, instType, 1, value);
        this.targetType = targetType;
    }

    public static ConvertInst create(BasicBlock parent, Instruction insertBefore,String resultName, InstType instType, Value value, Type targetType) {
        return new ConvertInst(parent, insertBefore, targetType, resultName, instType, value, targetType);
    }

    public Value getValue() {
        return this.userOperands.get(0);
    }

    public Type getTargetType() {
        return this.targetType;
    }
}
