package ir;

import ir.types.FunctionType;
import util.ir.IList;
import util.ir.IListNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Function extends Constant implements IListNode<Function,Module>, Iterable<BasicBlock> {

    class FunctionIterator implements Iterator<BasicBlock> {
        private int cursor = 0;
        @Override
        public boolean hasNext() {
            return cursor < Function.this.basicBlockIlist.getNumNode();
        }

        @Override
        public BasicBlock next() {
            return Function.this.basicBlockIlist.get(cursor++).getVal();
        }
    }

    private Function prev;
    private Function next;
    private Module parent;

    private List<Arg> argList;

    private boolean isBuiltin;
    IList<BasicBlock, Function> basicBlockIlist;

    public Function(FunctionType functionType, String name, List<Arg> argList, boolean isBuiltin, Module parent, Function insertBefore) {
        super(functionType, name);
        this.argList = argList;
        this.parent = parent;
        this.isBuiltin = isBuiltin;
        if(insertBefore == null) {

        } else {

        }
    }
    public Function(FunctionType functionType, String name) {
        super(functionType, name);
        this.argList = new ArrayList<>();
    }


    @Override
    public Iterator<BasicBlock> iterator() {
        return new FunctionIterator();
    }

    @Override
    public IListNode<Function, Module> getNext() {
        return next;
    }

    @Override
    public IListNode<Function, Module> getPrev() {
        return prev;
    }

    @Override
    public Function getVal() {
        return this;
    }

    @Override
    public Module getParent() {
        return parent;
    }

    @Override
    public boolean setNext(IListNode<Function, Module> node) {
        return false;
    }

    @Override
    public boolean setPrev(IListNode<Function, Module> node) {
        return false;
    }

    public FunctionType getFunctionType() {
        assert this.type instanceof FunctionType;
        return (FunctionType) this.type;
    }

    public int getNumBasicBlock() {
        return this.basicBlockIlist.getNumNode();
    }
}
