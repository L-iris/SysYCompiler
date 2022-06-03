package ir;

import ir.types.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BinaryInst extends Instruction{


    public BinaryInst(Type resultType, String resultName, InstType instType, Value userOperand1, Value userOperand2) {
        super(resultType, resultName, instType, 2, userOperand1, userOperand2);
    }

    public BinaryInst create(BasicBlock parent, Type resultType, String resultName, InstType instType, Value userOperand1, Value userOperand2) {
        BinaryInst binaryInst = new BinaryInst(resultType, resultName, instType, userOperand1, userOperand2);

        return null;
    }

    public BinaryInst create(BasicBlock parent, Type resultType, InstType instType, Value userOperand1, Value userOperand2) {
        return null;
    }
}
