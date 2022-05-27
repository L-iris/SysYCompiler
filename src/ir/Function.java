package ir;

import ir.types.FunctionType;
import ir.types.Type;
import util.ir.Ilist;
import util.ir.Inode;

import java.util.Iterator;
import java.util.List;

public class Function extends Constant implements Inode<Function,Module>, Iterable<BasicBlock> {

    private Function prev;
    private Function next;
    private Module parent;

    private List<Arg> argList;

    private boolean isBuiltin;
    Ilist<BasicBlock, Function> basicBlockIlist;

    public Function(FunctionType functionType, List<Arg> argList) {
        super(functionType);
        this.argList = argList;
    }

    @Override
    public Iterator<BasicBlock> iterator() {
        return null;
    }

    @Override
    public Inode<Function, Module> getNext() {
        return next;
    }

    @Override
    public Inode<Function, Module> getPrev() {
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

    public FunctionType getFunctionType() {
        assert this.type instanceof FunctionType;
        return (FunctionType) this.type;
    }
}
