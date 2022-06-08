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


    public GlobalVariable(Type type, String name, boolean isConst, Value userOperand) {
        super(type, name, 1, userOperand);
        this.isConst = isConst;
    }

    public static GlobalVariable create(Module parent, GlobalVariable insertBefore, Type resultType, String resultName, boolean isConst, Value userOperand){
        GlobalVariable globalVariable = new GlobalVariable(resultType, resultName, isConst, userOperand);
        parent.globalVariables.insertBefore(globalVariable, insertBefore);
        return globalVariable;
    }

    public static GlobalVariable create(Module parent, Type resultType, String resultName, boolean isConst, Value userOperand){
        GlobalVariable globalVariable = new GlobalVariable(resultType, resultName, isConst, userOperand);
        parent.globalVariables.insertAtEnd(globalVariable);
        return globalVariable;
    }

    public static GlobalVariable create() {
        return new GlobalVariable(null, null, true, null);
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
