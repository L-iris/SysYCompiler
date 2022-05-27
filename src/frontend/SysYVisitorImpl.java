package frontend;

import util.frontend.SysYBaseVisitor;
import util.frontend.SysYParser;

public class SysYVisitorImpl extends SysYBaseVisitor<Void> {
    /**
     * program
     *     : compUnit
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitProgram(SysYParser.ProgramContext ctx) {

        return super.visitProgram(ctx);
    }

    /**
     * compUnit
     *     : (funcDef|decl)+
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitCompUnit(SysYParser.CompUnitContext ctx) {
        return super.visitCompUnit(ctx);
    }

    /**
     * decl
     *     : constDecl
     *     | varDecl
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitDecl(SysYParser.DeclContext ctx) {
        return super.visitDecl(ctx);
    }

    /**
     * constDecl
     *     : CONST bType constDef (COMMA constDef)* SEMICOLON
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitConstDecl(SysYParser.ConstDeclContext ctx) {
        return super.visitConstDecl(ctx);
    }

    /**
     * bType
     *     :INT | FLOAT
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitBType(SysYParser.BTypeContext ctx) {
        return super.visitBType(ctx);
    }

    /**
     * constDef
     *     :Identifier (LB constExpr RB)* ASSIGN constInitVal
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitConstDef(SysYParser.ConstDefContext ctx) {
        return super.visitConstDef(ctx);
    }

    /**
     * constInitVal
     *     :constExpr
     *     |(LC (constInitVal (COMMA constInitVal)*)? RC)
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitConstInitVal(SysYParser.ConstInitValContext ctx) {
        return super.visitConstInitVal(ctx);
    }

    /**
     * varDecl
     *     :bType varDef (COMMA varDef)* SEMICOLON
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitVarDecl(SysYParser.VarDeclContext ctx) {
        return super.visitVarDecl(ctx);
    }

    /**
     * varDef
     *     :Identifier (LB constExpr RB)* (ASSIGN initVal)?
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitVarDef(SysYParser.VarDefContext ctx) {
        return super.visitVarDef(ctx);
    }

    /**
     * initVal
     *     :expr
     *     |(LC (initVal (COMMA initVal)*)? RC)
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitInitVal(SysYParser.InitValContext ctx) {
        return super.visitInitVal(ctx);
    }

    /**
     * funcDef
     *     :funcType Identifier LP (funcFParams)? RP block
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitFuncDef(SysYParser.FuncDefContext ctx) {
        return super.visitFuncDef(ctx);
    }

    /**
     * funcType
     *     :INT|VOID|FLOAT
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitFuncType(SysYParser.FuncTypeContext ctx) {
        return super.visitFuncType(ctx);
    }

    /**
     * funcFParams
     *     :funcFParam (COMMA funcFParam)*
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        return super.visitFuncFParams(ctx);
    }

    /**
     * funcFParam
     *     :bType Identifier (LB RB (LB expr RB)*)?
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitFuncFParam(SysYParser.FuncFParamContext ctx) {
        return super.visitFuncFParam(ctx);
    }

    /**
     * block
     *     :LC blockItem* RC
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitBlock(SysYParser.BlockContext ctx) {
        return super.visitBlock(ctx);
    }

    /**
     * blockItem
     *     :decl|stmt
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitBlockItem(SysYParser.BlockItemContext ctx) {
        return super.visitBlockItem(ctx);
    }

    /**
     * stmt
     *     :(lVal ASSIGN expr SEMICOLON)
     *     |((expr)? SEMICOLON)
     *     |block
     *     |(IF LP cond RP stmt (ELSE stmt)?)
     *     |(WHILE LP cond RP stmt)
     *     |(BREAK SEMICOLON)
     *     |(CONTINUE SEMICOLON)
     *     |(RETURN expr? SEMICOLON)
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitStmt(SysYParser.StmtContext ctx) {
        return super.visitStmt(ctx);
    }

    /**
     * expr
     *     :addExpr
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitExpr(SysYParser.ExprContext ctx) {
        return super.visitExpr(ctx);
    }

    /**
     * cond
     *     :lOrExpr
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitCond(SysYParser.CondContext ctx) {
        return super.visitCond(ctx);
    }

    /**
     * lVal
     *     :Identifier (LB expr RB)*
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitLVal(SysYParser.LValContext ctx) {
        return super.visitLVal(ctx);
    }

    /**
     * primaryExpr
     *     :(LP expr RP)
     *     |lVal
     *     |number
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitPrimaryExpr(SysYParser.PrimaryExprContext ctx) {
        return super.visitPrimaryExpr(ctx);
    }

    /**
     * number
     *     :IntConst
     *     |FloatConst
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitNumber(SysYParser.NumberContext ctx) {
        return super.visitNumber(ctx);
    }

    /**
     * unaryExpr
     *     :primaryExpr
     *     |(Identifier LP (funcRParams)? RP)
     *     |(unaryOp unaryExpr)
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitUnaryExpr(SysYParser.UnaryExprContext ctx) {
        return super.visitUnaryExpr(ctx);
    }

    /**
     * unaryOp
     *     :ADD|MINUS|NOT
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitUnaryOp(SysYParser.UnaryOpContext ctx) {
        return super.visitUnaryOp(ctx);
    }

    /**
     * funcRParams
     *     :expr (COMMA expr)*
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitFuncRParams(SysYParser.FuncRParamsContext ctx) {
        return super.visitFuncRParams(ctx);
    }

    /**
     * mulExpr
     *     :unaryExpr ((MUL|DIV|MOD) unaryExpr)*
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitMulExpr(SysYParser.MulExprContext ctx) {
        return super.visitMulExpr(ctx);
    }

    /**
     * addExpr
     *     :mulExpr ((ADD|MINUS) mulExpr)*
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitAddExpr(SysYParser.AddExprContext ctx) {
        return super.visitAddExpr(ctx);
    }

    /**
     * relExpr
     *     :addExpr ((LT|GT|LE|GE) addExpr)*
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitRelExpr(SysYParser.RelExprContext ctx) {
        return super.visitRelExpr(ctx);
    }

    /**
     * eqExpr
     *     :relExpr ((EQ|NE) relExpr)*
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitEqExpr(SysYParser.EqExprContext ctx) {
        return super.visitEqExpr(ctx);
    }

    /**
     * lAndExpr
     *     :eqExpr (AND lAndExpr)*
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitLAndExpr(SysYParser.LAndExprContext ctx) {
        return super.visitLAndExpr(ctx);
    }

    /**
     * lOrExpr
     *     :lAndExpr (OR lAndExpr)*
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitLOrExpr(SysYParser.LOrExprContext ctx) {
        return super.visitLOrExpr(ctx);
    }

    /**
     * constExpr
     *     :addExpr
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Void visitConstExpr(SysYParser.ConstExprContext ctx) {
        return super.visitConstExpr(ctx);
    }
}