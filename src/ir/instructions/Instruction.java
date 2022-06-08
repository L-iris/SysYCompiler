package ir.instructions;

import ir.BasicBlock;
import ir.User;
import ir.Value;
import ir.types.Type;
import util.ir.IListNode;

public class Instruction extends User implements IListNode<Instruction, BasicBlock> {

    public enum InstType {
        RET,
        BR,

        FNEG,

        ADD,
        FADD,
        SUB,
        FSUB,
        MUL,
        FMUL,
        SDIV,
        FDIV,
        SREM,
        FREM,

        AND,
        OR,
        XOR,

        EXTRACTELEMENT,
        INSERTELEMENT,
        SHUFFLEVECTOR,

        ALLOCA,
        LOAD,
        STORE,

        ZEXT,
        SITOFP,

        ICMPEQ,
        ICMPNE,
        ICMPSGT,
        ICMPSGE,
        ICMPSLT,
        ICMPSLE,

        FCMPEQ,
        FCMPNE,
        FCMPOGT,
        FCMPOGE,
        FCMPOLT,
        FCMPOLE,

        PHI,
        CALL,
        GEP,
    }

    protected IListNode<Instruction, BasicBlock> next;
    protected IListNode<Instruction, BasicBlock> prev;
    protected BasicBlock parent;

    protected InstType instType;

    protected static Instruction create(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands){
        Instruction instruction = new Instruction(resultType, resultName, instType, numUserOperands, userOperands);
        parent.instructionIList.insertBefore(instruction, insertBefore);
        return instruction;
    }

    protected static Instruction create(BasicBlock parent, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands){
        Instruction instruction = new Instruction(resultType, resultName, instType, numUserOperands, userOperands);
        parent.instructionIList.insertAtEnd(instruction);
        return instruction;
    }

    public Instruction(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands) {
        super(resultType, resultName, numUserOperands, userOperands);
        if(insertBefore == null)
            parent.instructionIList.insertAtEnd(this);
        else
            parent.instructionIList.insertBefore(this, insertBefore);
        this.instType = instType;
    }

    public Instruction(BasicBlock parent, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands) {
        super(resultType, resultName, numUserOperands, userOperands);
        parent.instructionIList.insertAtEnd(this);
        this.instType = instType;
    }

    public Instruction(Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands) {
        super(resultType, resultName, numUserOperands, userOperands);
        this.instType = instType;
    }

    public Instruction(Type type, String name, int numUserOperands) {
        super(type, name, numUserOperands);
    }

    public Instruction(Type type, String name) {
        super(type, name);
    }

    @Override
    public IListNode<Instruction, BasicBlock> getNext() {
        return next;
    }

    @Override
    public IListNode<Instruction, BasicBlock> getPrev() {
        return prev;
    }

    @Override
    public Instruction getVal() {
        return this;
    }

    @Override
    public BasicBlock getParent() {
        return parent;
    }

    @Override
    public boolean setNext(IListNode<Instruction, BasicBlock> node) {
        this.next = node;
        return false;
    }

    @Override
    public boolean setPrev(IListNode<Instruction, BasicBlock> node) {
        this.prev = node;
        return false;
    }

    @Override
    public boolean setParent(BasicBlock parent) {
        this.parent = parent;
        return false;
    }
}
