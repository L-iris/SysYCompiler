package backend;

import ir.BasicBlock;
import ir.GlobalVariable;
import ir.Module;
import ir.Value;
import ir.constval.ConstArray;
import ir.constval.ConstFloat;
import ir.constval.ConstInt;
import ir.instructions.Instruction;
import ir.instructions.RetInst;

public class IR2ASM {
    public Module module;
    public String asm;
    public String prefix = ".arch armv7ve\n";
    public String suffix = "@ver: final-1\n";

    public IR2ASM(Module module){
        this.module = module;
        asm = "";
    }

    private String addLine(String line) {
        this.asm = this.asm + line + "\n";
        return this.asm;
    }

    public String genAsm() {
        addLine(prefix);

        addLine(".text\n");
        for(var fn:module.functions) {
            var f = fn.getVal();
            if(f.isBuiltin)
                continue;
            var fname = f.getName();
            addLine(".global " + fname);
            addLine(fname + ":");
            for (int i = 0; i < f.basicBlockIlist.getNumNode(); i++) {
                var bb = f.basicBlockIlist.get(i).getVal();
                if(bb.instructionIList.getNumNode() == 0)
                    continue;
                if(i==0) {
                    if(bb.instructionIList.getNumNode()<=5) {
                        continue;
                    } else {

                    }
                }

                addLine(".LBB" + bb.getName().substring(1) + ":");
                for(var in:bb) {
                    Instruction inst = in.getVal();

                    if(inst instanceof RetInst){
                        RetInst retInst = (RetInst) inst;
                        if(retInst.getRetValue() == null) {
                            addLine("bx lr");
                        } else{
                            if(retInst.getRetValue() instanceof ConstInt) {
                                addLine("mov r0,#"+((ConstInt) retInst.getRetValue()).value);
                                addLine("bx lr");
                            } else if(retInst.getRetValue() instanceof ConstFloat) {
                                addLine("mov r0,#"+((ConstFloat) retInst.getRetValue()).value);
                                addLine("bx lr");
                            } else if(retInst.getRetValue() instanceof ConstArray) {
                                addLine("mov r0,#"+((ConstFloat) retInst.getRetValue()).value);
                                addLine("bx lr");
                            }
                        }
                    }
                }
            }
        }
        if(module.globalVariables.getNumNode() !=0) {
            addLine(".data\n");
            addLine(".align 4");
        }
        for(var gn:module.globalVariables) {
            GlobalVariable gv = gn.getVal();
            Value init = gv.getOperand(0);
            if(init instanceof ConstInt){
                String name = gv.getName().substring(0, gv.getName().length() - 5);
                addLine(".global "+ name);
                addLine(name+":");
                addLine("\t.word "+((ConstInt) init).value);
            } else if(init instanceof ConstFloat){
                String name = gv.getName().substring(0, gv.getName().length() - 5);
                addLine(".global "+ name);
                addLine(name+":");
                addLine("\t.float "+((ConstFloat) init).value);
            } else if(init instanceof ConstArray){
                String name = gv.getName().substring(0, gv.getName().length() - 5);
                addLine(".global "+ name);
                addLine(name+":");
                for(var v:((ConstArray) init).containedValue){

                }
            }
        }

        addLine(suffix);
        return asm;
    }
}
