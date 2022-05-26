package frontend;

import java.util.List;

public class MyVisitor extends SysYBaseVisitor<Integer>{
    @Override
    public Integer visitProgram(SysYParser.ProgramContext ctx) {
        System.out.println("visitProgram");
        return super.visitProgram(ctx);
    }

    @Override
    public Integer visitCompUnit(SysYParser.CompUnitContext ctx) {
        System.out.println("visitCompUnit");
        List<SysYParser.DeclContext> decl = ctx.decl();
        List<SysYParser.FuncDefContext> funcDefContexts = ctx.funcDef();
        for(var d : decl){
            System.out.println(d);
        }
        for(var f : funcDefContexts){
            System.out.println(f);
        }
        return super.visitCompUnit(ctx);
    }
}
