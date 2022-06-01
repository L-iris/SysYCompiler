package ir;

import ir.types.Type;
import util.ir.IListNode;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable extends Constant implements IListNode<GlobalVariable, Module> {

    private IListNode<GlobalVariable, Module> next;
    private IListNode<GlobalVariable, Module> prev;
    private Module parent;

    private boolean isConst;

    private int initInt;
    private float initFloat;

    public GlobalVariable(Type type, String name, int numUserOperands, List<Value> userOperands, List<Use> useList, boolean isConst, Module parent) {
        super(type, name, numUserOperands, userOperands, useList);
        this.isConst = isConst;
        if(parent != null)
            parent.globalVariables.insertAtEnd(this);
    }

    public static GlobalVariable create()

    public static GlobalVariable create() {
        return new GlobalVariable(null, null, 0, new ArrayList<>(), new ArrayList<>(), false, null);
    }

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean aConst) {
        isConst = aConst;
    }

    @Override
    public IListNode<GlobalVariable, Module> getNext() {
        return next;
    }

    @Override
    public IListNode<GlobalVariable, Module> getPrev() {
        return prev;
    }

    @Override
    public GlobalVariable getVal() {
        return this;
    }

    @Override
    public Module getParent() {
        return parent;
    }

    @Override
    public boolean setNext(IListNode<GlobalVariable, Module> node) {
        this.next = node;
        return false;
    }

    @Override
    public boolean setPrev(IListNode<GlobalVariable, Module> node) {
        this.prev = node;
        return false;
    }

    @Override
    public boolean setParent(Module parent) {
        this.parent = parent;
        return false;
    }
}
