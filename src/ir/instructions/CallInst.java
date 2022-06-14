package ir.instructions;

import ir.BasicBlock;
import ir.Function;
import ir.Value;
import ir.types.FunctionType;
import ir.types.Type;

public class CallInst extends Instruction{
    public CallInst(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands) {
        super(parent, insertBefore, resultType, resultName, instType, numUserOperands, userOperands);
    }

    public static CallInst create(BasicBlock basicBlock, Instruction insertBefore, String resultName, Function func, Value... params){
        FunctionType functionType = func.getFunctionType();
        assert functionType.getNumOfParams() == params.length;
        for(int i=0;i< params.length;i++){
            assert functionType.getParamType(i).equals(params[i]);
        }

        Value[] operands = new Value[params.length+1];
        operands[0] = func;
        for(int i=0;i<params.length;i++){
            operands[i+1] = params[i];
        }

        return new CallInst(basicBlock, insertBefore, functionType.getRetType(), resultName, InstType.CALL, operands.length, operands);
    }

    public static CallInst create(BasicBlock basicBlock, String resultName, Function func, Value... params) {
        return create(basicBlock, null, resultName, func, params);
    }

    public Function getFunction(){
        return ((Function) userOperands.get(0));
    }

    public Value getParam(int i){
        return userOperands.get(i+1);
    }
}
