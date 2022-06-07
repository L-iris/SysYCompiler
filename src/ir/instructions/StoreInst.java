package ir.instructions;

import ir.BasicBlock;
import ir.Value;
import ir.types.PointerType;
import ir.types.Type;

public class StoreInst extends Instruction{
    public StoreInst(BasicBlock parent, Instruction insertBefore,Value value, Value dest) {
        super(parent, insertBefore, null, null, InstType.STORE, 2, value, dest);
    }

    public static StoreInst create(BasicBlock parent, Instruction insertBefore, Value value, Value dest) {
        assert dest.getType() instanceof PointerType;
        assert ((PointerType) dest.getType()).getContainedType().equals(value.getType());
        return new StoreInst(parent, insertBefore, value, dest);
    }

    public static StoreInst create(BasicBlock parent, Value value, Value dest) {
        return create(parent, null, value, dest);
    }
}
