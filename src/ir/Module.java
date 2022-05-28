package ir;

import util.ir.IList;

public class Module {
    public IList<GlobalVariable, Module> globalVariables;
    public IList<Function, Module> functions;

    public Module() {
        globalVariables = new IList<>();
        functions = new IList<>();
    }

}
