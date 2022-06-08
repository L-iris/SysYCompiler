package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.PointerType;
import ir.types.Type;

public class LoadInst extends Instruction{
    public LoadInst(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, Value src) {
        super(parent, insertBefore, resultType, resultName, InstType.LOAD, 1, src);
    }

    public static LoadInst create(BasicBlock parent, Instruction insertBefore, String resultName, Value src) {
        assert src.getType() instanceof PointerType;
        return new LoadInst(parent, insertBefore, ((PointerType) src.getType()).getContainedType(), resultName, src);
    }

    public static LoadInst create(BasicBlock parent, String resultName, Value src) {
        return create(parent, null, resultName, src);
    }
}
