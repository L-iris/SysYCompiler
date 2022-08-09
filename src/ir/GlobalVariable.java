package ir;

import ir.constval.ConstArray;
import ir.constval.ConstFloat;
import ir.constval.ConstInt;
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
        super(Type.pointerType(type), name, 1, userOperand);
        this.isConst = isConst;
    }

    public static GlobalVariable create(Module parent, GlobalVariable insertBefore, Type resultType, String resultName, boolean isConst, Value userOperand){
        if(resultType.getTypeID() == Type.TypeID.IntegerTyID){
            assert userOperand instanceof ConstInt;
        } else if(resultType.getTypeID() == Type.TypeID.FloatTyID){
            assert userOperand instanceof ConstFloat;
        } else if(resultType.getTypeID() == Type.TypeID.ArrayTyID){
            assert userOperand instanceof ConstArray;
        }
        GlobalVariable globalVariable = new GlobalVariable(resultType, resultName, isConst, userOperand);
        parent.globalVariables.insertBefore(globalVariable, insertBefore);
        return globalVariable;
    }

    public static GlobalVariable create(Module parent, Type resultType, String resultName, boolean isConst, Value userOperand){
        return create(parent, null, resultType, resultName, isConst, userOperand);
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        return str.append(this.name).append(" = ").append("global ").append(this.type).append(", ").append(this.userOperands.get(0)).toString();
    }
}
