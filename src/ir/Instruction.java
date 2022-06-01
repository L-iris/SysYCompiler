package ir;

import ir.types.Type;
import util.ir.IList;
import util.ir.IListNode;

import java.util.List;

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

    private IListNode<Instruction, BasicBlock> next;
    private IListNode<Instruction, BasicBlock> prev;
    private BasicBlock parent;

    private InstType instType;

    public static Instruction create(BasicBlock parent, Instruction insertBefore, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands){
        Instruction instruction = new Instruction(resultType, resultName, instType, numUserOperands, userOperands);
        parent.instructionIList.insertBefore(instruction, insertBefore);
        return instruction;
    }

    public static Instruction create(BasicBlock parent, Type resultType, String resultName, InstType instType, int numUserOperands, Value... userOperands){
        Instruction instruction = new Instruction(resultType, resultName, instType, numUserOperands, userOperands);
        parent.instructionIList.insertAtEnd(instruction);
        return instruction;
    }

    public Instruction(Type resultType, String resultName, InstType instType, int numUserOperands, Value[] userOperands) {
        super(resultType, resultName, numUserOperands, List.of(userOperands));
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
}
