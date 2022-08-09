package ir;

import ir.types.FunctionType;
import ir.types.Type;
import util.ir.IList;
import util.ir.IListNode;

import javax.lang.model.element.TypeElement;
import java.util.*;

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

    private IListNode<Function, Module> prev;
    private IListNode<Function, Module> next;
    private Module parent;

    private List<Arg> argList;

    boolean isBuiltin;
    public IList<BasicBlock, Function> basicBlockIlist;

    public Function(FunctionType functionType, String name, List<Arg> argList, boolean isBuiltin, Module parent) {
        super(functionType, name);
        this.basicBlockIlist = new IList<>(this);
        this.argList = argList;
        this.parent = parent;
        this.isBuiltin = isBuiltin;
    }

    public static Function create(FunctionType functionType, String name, List<Arg> argList, boolean isBuiltin, Module parent, Function insertBefore) {
        Function function = new Function(functionType, name, argList, isBuiltin, parent);
        function.parent.functions.insertBefore(function, insertBefore);
        return function;
    }

    public static Function create(FunctionType functionType, String name, List<Arg> argList, boolean isBuiltin, Module parent) {
        Function function = new Function(functionType, name, argList, isBuiltin, parent);
        function.parent.functions.insertAtEnd(function);
        return function;
    }

    public static Function create(FunctionType functionType, String name, boolean isBuiltin, Module parent, Function insertBefore) {
        Function function = new Function(functionType, name, new ArrayList<>(), isBuiltin, parent);
        function.parent.functions.insertBefore(function, insertBefore);
        return function;
    }

    public static Function create(FunctionType functionType, String name, boolean isBuiltin, Module parent) {
        Function function = new Function(functionType, name, new ArrayList<>(), isBuiltin, parent);
        function.parent.functions.insertAtEnd(function);
        return function;
    }

    public static Function create(boolean isBuiltin, Module parent, Function insertBefore, Type retType, String name, List<Arg> args) {
        FunctionType functionType;
        if(args.size() != 0) {
            Type[] paramsType = new Type[args.size()];
            //args.sort(Comparator.comparingInt(Arg::getPos));
            for (int i = 0; i < args.size(); i++) {
                paramsType[i] = args.get(i).getType();
            }
            functionType = Type.functionType(retType, paramsType);
        } else {
            functionType = Type.functionType(retType);
        }
        Function function = new Function(functionType, name, args, isBuiltin, parent);
        if(insertBefore == null)
            function.parent.functions.insertAtEnd(function);
        else
            function.parent.functions.insertBefore(function, insertBefore);
        return function;
    }

    public static Function create(boolean isBuiltin, Module parent, Type retType, String name, List<Arg> args) {
        return create(isBuiltin, parent, null, retType, name, args);
    }

    public static Function create(boolean isBuiltin, Module parent, Function insertBefore, Type retType, String name, Arg... args) {
        return create(isBuiltin, parent, insertBefore, retType, name, List.of(args));
    }

    public static Function create(boolean isBuiltin, Module parent, Type retType, String name, Arg... args) {
        return create(isBuiltin, parent, null, retType, name, args);
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
        this.next = node;
        return true;
    }

    @Override
    public boolean setPrev(IListNode<Function, Module> node) {
        this.prev = node;
        return true;
    }

    @Override
    public boolean setParent(Module parent) {
        this.parent = parent;
        return true;
    }

    public FunctionType getFunctionType() {
        assert this.type instanceof FunctionType;
        return (FunctionType) this.type;
    }

    public int getNumBasicBlock() {
        return this.basicBlockIlist.getNumNode();
    }

    @Override
    public String toString() {
        StringBuilder str= new StringBuilder("define " + ((FunctionType) this.type).getRetType() + " " + this.name + this.argList);
        str.append("{\n");
        for(var b:this) {
            str.append(b).append("\n");
        }
        str.append("}");
        return str.toString();
    }
}
