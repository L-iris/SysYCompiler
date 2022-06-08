package ir;

import ir.instructions.Instruction;
import ir.types.Type;
import util.ir.IList;
import util.ir.IListNode;

import java.util.Iterator;

/**
 * This represents a single basic block in LLVM. A basic block is simply a
 * container of instructions that execute sequentially. Basic blocks are Values
 * because they are referenced by instructions such as branches and switch
 * tables. The type of a BasicBlock is "Type::LabelTy" because the basic block
 * represents a label to which a branch can jump.
 *
 * A well formed basic block is formed of a list of non-terminating
 * instructions followed by a single terminator instruction. Terminator
 * instructions may not occur in the middle of basic blocks, and must terminate
 * the blocks. The BasicBlock class allows malformed basic blocks to occur
 * because it may be useful in the intermediate stage of constructing or
 * modifying a program. However, the verifier will ensure that basic blocks are
 * "well formed".
 */
public class BasicBlock extends Value implements IListNode<BasicBlock,Function>, Iterable<Instruction> {

    private BasicBlock next;
    private BasicBlock prev;
    private Function parent;

    public IList<Instruction, BasicBlock> instructionIList;



    public BasicBlock() {
        super(Type.labelType());
    }

    public BasicBlock(Function parent) {
        super(Type.labelType());
        this.parent = parent;
    }

    @Override
    public Iterator<Instruction> iterator() {
        return null;
    }

    @Override
    public IListNode<BasicBlock, Function> getNext() {
        return next;
    }

    @Override
    public IListNode<BasicBlock, Function> getPrev() {
        return prev;
    }

    @Override
    public BasicBlock getVal() {
        return this;
    }

    @Override
    public Function getParent() {
        return parent;
    }

    @Override
    public boolean setNext(IListNode<BasicBlock, Function> node) {
        return false;
    }

    @Override
    public boolean setPrev(IListNode<BasicBlock, Function> node) {
        return false;
    }

    @Override
    public boolean setParent(Function parent) {
        this.parent = parent;
        return false;
    }
}
