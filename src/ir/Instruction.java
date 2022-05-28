package ir;

import ir.types.Type;
import util.ir.IListNode;

import java.util.List;

public class Instruction extends User implements IListNode<Instruction, BasicBlock> {

    enum InstType {
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

    private Instruction next;
    private Instruction prev;
    private BasicBlock parent;

    private InstType instType;

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
        return false;
    }

    @Override
    public boolean setPrev(IListNode<Instruction, BasicBlock> node) {
        return false;
    }
}
