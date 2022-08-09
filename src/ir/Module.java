package ir;

import util.ir.IList;
import util.ir.IListNode;

public class Module {
    public IList<GlobalVariable, Module> globalVariables;
    public IList<Function, Module> functions;

    public Module() {
        globalVariables = new IList<>(this);
        functions = new IList<>(this);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (IListNode<GlobalVariable, Module> gv :
                globalVariables) {
            str.append(gv.getVal().toString()).append("\n");
        }

        for (var f :
                functions) {
            if(!f.getVal().isBuiltin)
                str.append(f.getVal().toString()).append("\n");
        }

        return str.toString();
    }
}
