package ir;

import ir.types.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BinaryInst extends Instruction{


    public BinaryInst(Type resultType, String resultName, InstType instType, int numUserOperands, Value[] userOperands) {
        super(resultType, resultName, instType, numUserOperands, userOperands);
    }
}
