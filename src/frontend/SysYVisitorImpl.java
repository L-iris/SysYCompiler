package frontend;

import java.util.List;

public class SysYVisitorImpl extends SysYBaseVisitor<Void> {
    @Override
    public Void visitProgram(SysYParser.ProgramContext ctx) {
        System.out.println("visitProgram");
        return super.visitProgram(ctx);
    }

    @Override
    public Void visitCompUnit(SysYParser.CompUnitContext ctx) {
        System.out.println("visitCompUnit");
        List<SysYParser.DeclContext> decl = ctx.decl();
        List<SysYParser.FuncDefContext> funcDefContexts = ctx.funcDef();
        return super.visitCompUnit(ctx);
    }

    @Override
    public Void visitDecl(SysYParser.DeclContext ctx) {
        System.out.println("visitDecl");
        return super.visitDecl(ctx);
    }

    @Override
    public Void visitConstDecl(SysYParser.ConstDeclContext ctx) {
        System.out.println("visitConstDecl");
        return super.visitConstDecl(ctx);
    }

    @Override
    public Void visitBType(SysYParser.BTypeContext ctx) {
        System.out.println("visitBType");
        return super.visitBType(ctx);
    }

    @Override
    public Void visitConstDef(SysYParser.ConstDefContext ctx) {
        System.out.println("visitConstDef");
        return super.visitConstDef(ctx);
    }

    @Override
    public Void visitConstInitVal(SysYParser.ConstInitValContext ctx) {
        System.out.println("visitConstInitVal");
        return super.visitConstInitVal(ctx);
    }

    @Override
    public Void visitVarDecl(SysYParser.VarDeclContext ctx) {
        System.out.println("visitVarDecl");
        return super.visitVarDecl(ctx);
    }

    @Override
    public Void visitVarDef(SysYParser.VarDefContext ctx) {
        System.out.println("visitVarDef");
        return super.visitVarDef(ctx);
    }

    @Override
    public Void visitInitVal(SysYParser.InitValContext ctx) {
        System.out.println("visitInitVal");
        return super.visitInitVal(ctx);
    }

    @Override
    public Void visitFuncDef(SysYParser.FuncDefContext ctx) {
        System.out.println("visitFuncDef");
        return super.visitFuncDef(ctx);
    }

    @Override
    public Void visitFuncType(SysYParser.FuncTypeContext ctx) {
        System.out.println("visitFuncType");
        return super.visitFuncType(ctx);
    }

    @Override
    public Void visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        System.out.println("visitFuncFParams");
        return super.visitFuncFParams(ctx);
    }

    @Override
    public Void visitFuncFParam(SysYParser.FuncFParamContext ctx) {
        System.out.println("visitFuncFParam");
        return super.visitFuncFParam(ctx);
    }

    @Override
    public Void visitBlock(SysYParser.BlockContext ctx) {
        System.out.println("visitBlock");
        return super.visitBlock(ctx);
    }

    @Override
    public Void visitBlockItem(SysYParser.BlockItemContext ctx) {
        System.out.println("visitBlockItem");
        return super.visitBlockItem(ctx);
    }

    @Override
    public Void visitStmt(SysYParser.StmtContext ctx) {
        System.out.println("visitStmt");
        return super.visitStmt(ctx);
    }

    @Override
    public Void visitAssignStmt(SysYParser.AssignStmtContext ctx) {
        System.out.println("visitAssignStmt");
        return super.visitAssignStmt(ctx);
    }

    @Override
    public Void visitExpStmt(SysYParser.ExpStmtContext ctx) {
        System.out.println("visitExpStmt");
        return super.visitExpStmt(ctx);
    }

    @Override
    public Void visitConditionStmt(SysYParser.ConditionStmtContext ctx) {
        System.out.println("visitConditionStmt");
        return super.visitConditionStmt(ctx);
    }

    @Override
    public Void visitWhileStmt(SysYParser.WhileStmtContext ctx) {
        System.out.println("visitWhileStmt");
        return super.visitWhileStmt(ctx);
    }

    @Override
    public Void visitBreakStmt(SysYParser.BreakStmtContext ctx) {
        System.out.println("visitBreakStmt");
        return super.visitBreakStmt(ctx);
    }

    @Override
    public Void visitContinueStmt(SysYParser.ContinueStmtContext ctx) {
        System.out.println("visitContinueStmt");
        return super.visitContinueStmt(ctx);
    }

    @Override
    public Void visitReturnStmt(SysYParser.ReturnStmtContext ctx) {
        System.out.println("visitReturnStmt");
        return super.visitReturnStmt(ctx);
    }

    @Override
    public Void visitExp(SysYParser.ExpContext ctx) {
        System.out.println("visitExp");
        return super.visitExp(ctx);
    }

    @Override
    public Void visitCond(SysYParser.CondContext ctx) {
        System.out.println("visitCond");
        return super.visitCond(ctx);
    }

    @Override
    public Void visitLVal(SysYParser.LValContext ctx) {
        System.out.println("visitLVal");
        return super.visitLVal(ctx);
    }

    @Override
    public Void visitPrimaryExp(SysYParser.PrimaryExpContext ctx) {
        System.out.println("visitPrimaryExp");
        return super.visitPrimaryExp(ctx);
    }

    @Override
    public Void visitNumber(SysYParser.NumberContext ctx) {
        System.out.println("visitNumber");
        return super.visitNumber(ctx);
    }

    @Override
    public Void visitIntConst(SysYParser.IntConstContext ctx) {
        System.out.println("visitIntConst");
        return super.visitIntConst(ctx);
    }

    @Override
    public Void visitUnaryExp(SysYParser.UnaryExpContext ctx) {
        System.out.println("visitUnaryExp");
        return super.visitUnaryExp(ctx);
    }

    @Override
    public Void visitCallee(SysYParser.CalleeContext ctx) {
        System.out.println("visitCallee");
        return super.visitCallee(ctx);
    }

    @Override
    public Void visitUnaryOp(SysYParser.UnaryOpContext ctx) {
        System.out.println("visitUnaryOp");
        return super.visitUnaryOp(ctx);
    }

    @Override
    public Void visitFuncRParams(SysYParser.FuncRParamsContext ctx) {
        System.out.println("visitFuncRParams");
        return super.visitFuncRParams(ctx);
    }

    @Override
    public Void visitParam(SysYParser.ParamContext ctx) {
        System.out.println("visitParam");
        return super.visitParam(ctx);
    }

    @Override
    public Void visitMulExp(SysYParser.MulExpContext ctx) {
        System.out.println("visitMulExp");
        return super.visitMulExp(ctx);
    }

    @Override
    public Void visitMulOp(SysYParser.MulOpContext ctx) {
        System.out.println("visitMulOp");
        return super.visitMulOp(ctx);
    }

    @Override
    public Void visitAddExp(SysYParser.AddExpContext ctx) {
        System.out.println("visitAddExp");
        return super.visitAddExp(ctx);
    }

    @Override
    public Void visitAddOp(SysYParser.AddOpContext ctx) {
        System.out.println("visitAddOp");
        return super.visitAddOp(ctx);
    }

    @Override
    public Void visitRelExp(SysYParser.RelExpContext ctx) {
        System.out.println("visitRelExp");
        return super.visitRelExp(ctx);
    }

    @Override
    public Void visitRelOp(SysYParser.RelOpContext ctx) {
        System.out.println("visitRelOp");
        return super.visitRelOp(ctx);
    }

    @Override
    public Void visitEqExp(SysYParser.EqExpContext ctx) {
        System.out.println("visitEqExp");
        return super.visitEqExp(ctx);
    }

    @Override
    public Void visitEqOp(SysYParser.EqOpContext ctx) {
        System.out.println("visitEqOp");
        return super.visitEqOp(ctx);
    }

    @Override
    public Void visitLAndExp(SysYParser.LAndExpContext ctx) {
        System.out.println("visitLAndExp");
        return super.visitLAndExp(ctx);
    }

    @Override
    public Void visitLOrExp(SysYParser.LOrExpContext ctx) {
        System.out.println("visitLOrExp");
        return super.visitLOrExp(ctx);
    }

    @Override
    public Void visitConstExp(SysYParser.ConstExpContext ctx) {
        System.out.println("visitConstExp");
        return super.visitConstExp(ctx);
    }
}
