package frontend;

import ir.*;
import ir.Module;
import ir.instructions.AllocaInst;
import ir.instructions.BinaryInst;
import ir.instructions.Instruction;
import ir.types.Type;
import util.SymbolTableStack;
import util.frontend.SysYBaseVisitor;
import util.frontend.SysYParser;

import java.util.ArrayList;
import java.util.List;

public class SysYVisitorImpl extends SysYBaseVisitor<Value> {

    private static class VisitCtx {
        enum Phase {
            GlobalVariable,
            GlobalArray,
            Function,
        }

        Phase phase;
        Type bType;
        Type type;
        boolean isConst;
        boolean isGlobal;
    }

    public SymbolTableStack symbolTableStack;
    public Module module;
    private Function function;
    private BasicBlock basicBlock;
    private VisitCtx visitCtx = new VisitCtx();

    public SysYVisitorImpl(Module module) {
        this.symbolTableStack = new SymbolTableStack();
        this.module = module;
    }

    /**
     * program
     *     : compUnit
     *     ;
     * @return
     */
    @Override
    public Value visitProgram(SysYParser.ProgramContext ctx) {
        return super.visitProgram(ctx);
    }

    /**
     * compUnit
     *     : (funcDef|decl)+
     *     ;
     * @return
     */
    @Override
    public Value visitCompUnit(SysYParser.CompUnitContext ctx) {
        symbolTableStack.enterScope();
        this.visitCtx.isGlobal = true;
        symbolTableStack.addValue("getint", Function.create(true, module, Type.i32(), "getint"));
        symbolTableStack.addValue("getch", Function.create(true, module, Type.i32(), "getch"));
        symbolTableStack.addValue("getarray", Function.create(true, module, Type.i32(), "getarray", Arg.create(0, Type.arrayType(Type.i32()), "a")));
        return super.visitCompUnit(ctx);
    }

    /**
     * decl
     *     : constDecl
     *     | varDecl
     *     ;
     * @return
     */
    @Override
    public Value visitDecl(SysYParser.DeclContext ctx) {
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
    public Value visitConstDecl(SysYParser.ConstDeclContext ctx) {
        this.visitCtx.isConst = true;
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
    public Value visitBType(SysYParser.BTypeContext ctx) {
        if(ctx.INT() != null)
            this.visitCtx.bType = Type.i32();
        else
            this.visitCtx.bType = Type.f32();
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
    public Value visitConstDef(SysYParser.ConstDefContext ctx) {
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
    public Value visitConstInitVal(SysYParser.ConstInitValContext ctx) {
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
    public Value visitVarDecl(SysYParser.VarDeclContext ctx) {
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
    public Value visitVarDef(SysYParser.VarDefContext ctx) {
        if(this.symbolTableStack.findValueCurrentScope(ctx.Identifier().getText()) != null){
            //重定义error
        }
        Type bType = this.visitCtx.bType;
        Value initVal = null;
        if(this.visitCtx.isGlobal) { //全局
            if (ctx.LB().size() == 0) { //全局变量
                initVal = visit(ctx.initVal());
            } else { //全局数组
                List<Integer> dims = new ArrayList<>(ctx.constExpr().size());
                for (SysYParser.ConstExprContext constExprContext : ctx.constExpr()) {
                    Value v = visit(constExprContext);
                    if(v == null){
                        //数组维度非常数error
                    } else if(!(v instanceof ConstInt)){
                        //数组维度非整数error
                    } else{
                        dims.add(((ConstInt) v).value);
                    }
                }
                for(int i = dims.size() - 1; i >= 0; i--){
                    bType = Type.arrayType(bType, dims.get(i));
                }
                initVal = visit(ctx.initVal());

            }
            GlobalVariable globalVariable = GlobalVariable.create(module, bType, ctx.Identifier().getText() + ".addr", false, initVal);
            this.symbolTableStack.addValue(ctx.Identifier().getText(), globalVariable);
        } else { //局部
            if(ctx.LB().size() == 0) { //局部变量
                initVal = visit(ctx.initVal());
            } else { //局部数组

            }
            AllocaInst.create(basicBlock, null, bType);
        }
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
    public Value visitInitVal(SysYParser.InitValContext ctx) {
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
    public Value visitFuncDef(SysYParser.FuncDefContext ctx) {

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
    public Value visitFuncType(SysYParser.FuncTypeContext ctx) {
        if(ctx.INT() != null) {
            this.visitCtx.bType = Type.i32();
        } else if(ctx.FLOAT() != null){
            this.visitCtx.bType = Type.f32();
        } else if(ctx.VOID() != null){
            this.visitCtx.bType = Type.voidType();
        }
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
    public Value visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
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
    public Value visitFuncFParam(SysYParser.FuncFParamContext ctx) {
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
    public Value visitBlock(SysYParser.BlockContext ctx) {
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
    public Value visitBlockItem(SysYParser.BlockItemContext ctx) {
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
    public Value visitStmt(SysYParser.StmtContext ctx) {
        return super.visitStmt(ctx);
    }
    Value exprValue;
    /**
     * expr
     *     :addExpr
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Value visitExpr(SysYParser.ExprContext ctx) {

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
    public Value visitCond(SysYParser.CondContext ctx) {
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
    public Value visitLVal(SysYParser.LValContext ctx) {
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
    public Value visitPrimaryExpr(SysYParser.PrimaryExprContext ctx) {
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
    public Value visitNumber(SysYParser.NumberContext ctx) {
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
    public Value visitUnaryExpr(SysYParser.UnaryExprContext ctx) {
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
    public Value visitUnaryOp(SysYParser.UnaryOpContext ctx) {
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
    public Value visitFuncRParams(SysYParser.FuncRParamsContext ctx) {
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
    public Value visitMulExpr(SysYParser.MulExprContext ctx) {
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
    public Value visitAddExpr(SysYParser.AddExprContext ctx) {
        Value operand1 = visitMulExpr(ctx.mulExpr(0));
        for(int i = 1; i < ctx.mulExpr().size(); i++) {
            Value operand2 = visitMulExpr(ctx.mulExpr(i));
            if(isConst(operand1) & isConst(operand2)) {
                if(ctx.getChild(2 * i - 1).getText().equals("+"))
                    operand1 = constAdd(operand1, operand2);
                else
                    operand1 = constMinus(operand1, operand2);
            } else if(operand1.getType().getTypeID() == Type.TypeID.IntegerTyID & operand2.getType().getTypeID() == Type.TypeID.IntegerTyID) {
                if(ctx.getChild(2 * i - 1).getText().equals("+"))
                    operand1 = BinaryInst.create(basicBlock, Type.i32(), null, Instruction.InstType.ADD, operand1, operand2);
                else
                    operand1 = BinaryInst.create(basicBlock, Type.i32(), null, Instruction.InstType.SUB, operand1, operand2);
            } else {
                //TODO
            }
        }
        return super.visitAddExpr(ctx);
    }

    private boolean isConst(Value v) {
        return v instanceof ConstInt | v instanceof ConstFloat;
    }

    private Value constAdd(Value v1, Value v2) {
        if(v1 instanceof ConstInt) {
            if(v2 instanceof ConstInt) {
                return new ConstInt(((ConstInt) v1).value + ((ConstInt) v2).value);
            } else if(v2 instanceof ConstFloat) {
                return new ConstFloat(((ConstInt) v1).value + ((ConstFloat) v2).value);
            }
        } else if(v1 instanceof ConstFloat) {
            if(v2 instanceof ConstInt) {
                return new ConstFloat(((ConstFloat) v1).value + ((ConstInt) v2).value);
            } else if(v2 instanceof ConstFloat) {
                return new ConstFloat(((ConstFloat) v1).value + ((ConstFloat) v2).value);
            }
        }
        return null;
    }

    private Value constMinus(Value v1, Value v2) {
        if(v1 instanceof ConstInt) {
            if(v2 instanceof ConstInt) {
                return new ConstInt(((ConstInt) v1).value - ((ConstInt) v2).value);
            } else if(v2 instanceof ConstFloat) {
                return new ConstFloat(((ConstInt) v1).value - ((ConstFloat) v2).value);
            }
        } else if(v1 instanceof ConstFloat) {
            if(v2 instanceof ConstInt) {
                return new ConstFloat(((ConstFloat) v1).value - ((ConstInt) v2).value);
            } else if(v2 instanceof ConstFloat) {
                return new ConstFloat(((ConstFloat) v1).value - ((ConstFloat) v2).value);
            }
        }
        return null;
    }

    /**
     * relExpr
     *     :addExpr ((LT|GT|LE|GE) addExpr)*
     *     ;
     * @param ctx
     * @return
     */
    @Override
    public Value visitRelExpr(SysYParser.RelExprContext ctx) {
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
    public Value visitEqExpr(SysYParser.EqExprContext ctx) {
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
    public Value visitLAndExpr(SysYParser.LAndExprContext ctx) {
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
    public Value visitLOrExpr(SysYParser.LOrExprContext ctx) {
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
    public Value visitConstExpr(SysYParser.ConstExprContext ctx) {
        return super.visitConstExpr(ctx);
    }
}