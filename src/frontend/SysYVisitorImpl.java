package frontend;

import ir.*;
import ir.Module;
import ir.constval.ConstArray;
import ir.constval.ConstFloat;
import ir.constval.ConstInt;
import ir.instructions.*;
import ir.types.Type;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import util.SymbolTableStack;
import util.frontend.SysYBaseVisitor;
import util.frontend.SysYParser;

import javax.security.auth.callback.Callback;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SysYVisitorImpl extends SysYBaseVisitor<Value> {

    private static class VisitCtx {
        public Type retType;
        public Function function;

        enum Phase {
            GlobalVariable,
            GlobalArray,
            Function,
        }

        Phase phase;
        Type bType;
        Type type;
        List<Arg> args;
        boolean isConst;
        boolean isGlobal;
        boolean isConstExpr;
    }

    public SymbolTableStack symbolTableStack;
    public Module module;
    private Function function;
    private BasicBlock basicBlock;
    private final VisitCtx visitCtx;

    public SysYVisitorImpl(Module module) {
        this.symbolTableStack = new SymbolTableStack();
        this.module = module;
        this.visitCtx = new VisitCtx();
    }

    private Value visitChildrenCallBack(RuleNode node, Callable<Void> beforeVisit, Callable<Void> afterVisit) {
        Value result = this.defaultResult();
        int n = node.getChildCount();

        for(int i = 0; i < n && this.shouldVisitNextChild(node, result); ++i) {
            ParseTree c = node.getChild(i);
            Value childResult = null;
            try {
                if (beforeVisit != null)
                    beforeVisit.call();
                childResult = c.accept(this);
                if (afterVisit != null)
                    afterVisit.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = this.aggregateResult(result, childResult);
        }

        return result;
    }

    /**
     * program
     *     : compUnit
     *     ;
     */
    @Override
    public Value visitProgram(SysYParser.ProgramContext ctx) {
        return super.visitProgram(ctx);
    }

    /**
     * compUnit
     *     : (funcDef|decl)+
     *     ;
     */
    @Override
    public Value visitCompUnit(SysYParser.CompUnitContext ctx) {
        symbolTableStack.enterScope();
        this.visitCtx.isGlobal = true;
        symbolTableStack.addValue("getint", Function.create(true, module, Type.i32(), "getint"));
        symbolTableStack.addValue("getch", Function.create(true, module, Type.i32(), "getch"));
        symbolTableStack.addValue("getarray", Function.create(true, module, Type.i32(), "getarray", Arg.create(0, Type.arrayType(Type.i32()), "a")));
        symbolTableStack.addValue("getfloat", Function.create(true, module, Type.f32(), "getfloat"));
        symbolTableStack.addValue("getfarray", Function.create(true, module, Type.i32(), "getfarray", Arg.create(0, Type.arrayType(Type.f32()), "a")));
        symbolTableStack.addValue("putint", Function.create(true, module, Type.voidType(), "putint", Arg.create(0, Type.i32(), "a")));
        symbolTableStack.addValue("putch", Function.create(true, module, Type.voidType(), "getch", Arg.create(0, Type.i32(), "a")));
        symbolTableStack.addValue("putarray", Function.create(true, module, Type.voidType(), "putarray", Arg.create(0, Type.i32(), "n"), Arg.create(1, Type.arrayType(Type.i32()), "a")));
        symbolTableStack.addValue("putfloat", Function.create(true, module, Type.voidType(), "putfloat", Arg.create(0, Type.f32(), "a")));
        symbolTableStack.addValue("putfarray", Function.create(true, module, Type.voidType(), "putfarray", Arg.create(0, Type.i32(), "n"), Arg.create(1, Type.arrayType(Type.f32()), "a")));
        symbolTableStack.addValue("putf", Function.create(true, module, Type.voidType(), "putf" /*TODO varargs*/));
        symbolTableStack.addValue("_sysy_starttime", Function.create(true, module, Type.voidType(), "_sysy_starttime", Arg.create(0, Type.i32(), "lineno")));
        symbolTableStack.addValue("_sysy_stoptime", Function.create(true, module, Type.voidType(), "_sysy_stoptime", Arg.create(0, Type.i32(), "lineno")));
        Value result = visitChildrenCallBack(ctx, () -> {
            this.visitCtx.isGlobal = true;
            return null;
        }, null);
        symbolTableStack.exitScope();
        return result;
    }

    /**
     * decl
     *     : constDecl
     *     | varDecl
     *     ;
     */
    @Override
    public Value visitDecl(SysYParser.DeclContext ctx) {
        return super.visitDecl(ctx);
    }

    /**
     * constDecl
     *     : CONST bType constDef (COMMA constDef)* SEMICOLON
     *     ;
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
     */
    @Override
    public Value visitConstInitVal(SysYParser.ConstInitValContext ctx) {
        return super.visitConstInitVal(ctx);
    }

    /**
     * varDecl
     *     :bType varDef (COMMA varDef)* SEMICOLON
     *     ;
     */
    @Override
    public Value visitVarDecl(SysYParser.VarDeclContext ctx) {
        this.visitCtx.isConst = false;
        return super.visitVarDecl(ctx);
    }

    /**
     * varDef
     *     :Identifier (LB constExpr RB)* (ASSIGN initVal)?
     *     ;
     */
    @Override
    public Value visitVarDef(SysYParser.VarDefContext ctx) {
        if(this.symbolTableStack.findValueCurrentScope(ctx.Identifier().getText()) != null){
            //重定义error
            return null;
        }
        Value retVal = null;
        Type bType = this.visitCtx.bType;
        Value initVal = null;
        if(this.visitCtx.isGlobal) { //全局
            if (ctx.LB().size() == 0) { //全局变量
                if(ctx.ASSIGN() != null)
                    initVal = visit(ctx.initVal());
                else
                    initVal = ConstInt.create(0);
            } else { //全局数组
                List<Integer> dims = new ArrayList<>(ctx.constExpr().size());
                for (SysYParser.ConstExprContext constExprContext : ctx.constExpr()) {
                    Value v = visit(constExprContext);
                    if(v == null){
                        //数组维度非常数error
                        return null;
                    } else if(!(v instanceof ConstInt)){
                        //数组维度非整数error
                        return null;
                    } else{
                        dims.add(((ConstInt) v).value);
                    }
                }
                for(int i = dims.size() - 1; i >= 0; i--){
                    bType = Type.arrayType(bType, dims.get(i));
                }
                if(ctx.ASSIGN() != null)
                    initVal = visit(ctx.initVal());
                else{
                    if(this.visitCtx.bType.equals(Type.i32()))
                        initVal = ConstArray.create(ConstInt.create(0), dims);
                    else
                        initVal = ConstArray.create(ConstFloat.create(0), dims);
                }
            }
            retVal = GlobalVariable.create(module, bType, ctx.Identifier().getText() + ".addr", false, initVal);
            this.symbolTableStack.addValue(ctx.Identifier().getText(), retVal);
        } else { //局部
            if(ctx.LB().size() == 0) { //局部变量
                if(ctx.ASSIGN() != null)
                    initVal = visit(ctx.initVal());
                else
                    initVal = null;
            } else { //局部数组
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
                if(ctx.ASSIGN() != null)
                    initVal = visit(ctx.initVal());
                else
                    initVal = null;
            }
            retVal = AllocaInst.create(basicBlock, ctx.Identifier().getText() + ".addr", bType);
            if(initVal != null)
                StoreInst.create(basicBlock, initVal, retVal);
            symbolTableStack.addValue(ctx.Identifier().getText(), retVal);
        }
        return retVal;
    }

    /**
     * initVal
     *     :expr
     *     |(LC (initVal (COMMA initVal)*)? RC)
     *     ;
     */
    @Override
    public Value visitInitVal(SysYParser.InitValContext ctx) {
        return super.visitInitVal(ctx);
    }

    /**
     * funcDef
     *     :funcType Identifier LP (funcFParams)? RP block
     *     ;
     */
    @Override
    public Value visitFuncDef(SysYParser.FuncDefContext ctx) {
        this.visitCtx.phase = VisitCtx.Phase.Function;
        if(symbolTableStack.findValueCurrentScope(ctx.Identifier().getText()) != null){
            //重定义error
            return null;
        }
        visit(ctx.funcType());
        Type retType = this.visitCtx.retType;
        String funcName = ctx.Identifier().getText();
        visit(ctx.funcFParams());
        List<Arg> args = this.visitCtx.args;
        Function function = Function.create(false, module, retType, funcName, args);
        symbolTableStack.addValue(funcName, function);
        this.visitCtx.isGlobal = false;
        this.visitCtx.function = function;
        symbolTableStack.enterScope();
        visit(ctx.block());
        symbolTableStack.exitScope();
        return function;
    }

    /**
     * funcType
     *     :INT|VOID|FLOAT
     *     ;
     */
    @Override
    public Value visitFuncType(SysYParser.FuncTypeContext ctx) {
        if(ctx.INT() != null)
            this.visitCtx.retType = Type.i32();
        else if(ctx.VOID() != null)
            this.visitCtx.retType = Type.voidType();
        else if(ctx.FLOAT() != null)
            this.visitCtx.retType = Type.f32();
        return super.visitFuncType(ctx);
    }

    /**
     * funcFParams
     *     :funcFParam (COMMA funcFParam)*
     *     ;
     */
    @Override
    public Value visitFuncFParams(SysYParser.FuncFParamsContext ctx) {
        List<Arg> args = new ArrayList<>(ctx.funcFParam().size());
        for(int i = 0; i < ctx.funcFParam().size(); i++){
            Arg funcFParam = (Arg) visit(ctx.funcFParam(i));
            funcFParam.setPos(i);
            args.add(funcFParam);
        }
        this.visitCtx.args = args;
        return null;
    }

    /**
     * funcFParam
     *     :bType Identifier (LB RB (LB expr RB)*)?
     *     ;
     */
    @Override
    public Value visitFuncFParam(SysYParser.FuncFParamContext ctx) {
        Arg funcFParam = null;
        visit(ctx.bType());
        Type bType = this.visitCtx.bType;
        String name = ctx.Identifier().getText();
        Type type = null;
        //TODO from (LB RB (LB expr RB)*) to type
        funcFParam = Arg.create(-1, type, ctx.Identifier().getText());
        return funcFParam;
    }

    /**
     * block
     *     :LC blockItem* RC
     *     ;
     */
    @Override
    public Value visitBlock(SysYParser.BlockContext ctx) {
        return visitChildrenCallBack(ctx, null, null);
    }

    /**
     * blockItem
     *     :decl|stmt
     *     ;
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
     */
    @Override
    public Value visitStmt(SysYParser.StmtContext ctx) {
        if(ctx.ASSIGN() != null) { // 赋值语句

        } else if(ctx.block() != null) {

        } else if(ctx.IF() != null) {

        } else if(ctx.WHILE() != null) {

        } else if(ctx.BREAK() != null) {

        } else if(ctx.CONTINUE() != null) {

        } else if(ctx.RETURN() != null) {

        } else {

        }
        return null;
    }
    /**
     * expr
     *     :addExpr
     *     ;
     */
    @Override
    public Value visitExpr(SysYParser.ExprContext ctx) {

        return super.visitExpr(ctx);
    }

    /**
     * cond
     *     :lOrExpr
     *     ;
     */
    @Override
    public Value visitCond(SysYParser.CondContext ctx) {
        return super.visitCond(ctx);
    }

    /**
     * lVal
     *     :Identifier (LB expr RB)*
     *     ;
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
     */
    @Override
    public Value visitUnaryExpr(SysYParser.UnaryExprContext ctx) {
        if(ctx.primaryExpr() != null){
            return visitPrimaryExpr(ctx.primaryExpr());
        }else if(ctx.unaryExpr() != null){
            Value operand = visitUnaryExpr(ctx.unaryExpr());
            if(operand instanceof ConstInt){
                if(ctx.unaryOp().ADD() != null){
                    return operand;
                }
                if(ctx.unaryOp().MINUS() != null){
                    int value = ((ConstInt) operand).value;
                    return new ConstInt(-value);
                }
                if(ctx.unaryOp().NOT() != null){
                    int value = ((ConstInt) operand).value;
                    return new ConstInt(value == 0 ? 1 : 0);
                }
            }else if(operand instanceof ConstFloat){
                if(ctx.unaryOp().ADD() != null){
                    return operand;
                }
                if(ctx.unaryOp().MINUS() != null){
                    float value = ((ConstFloat) operand).value;
                    return new ConstFloat(-value);
                }
                if(ctx.unaryOp().NOT() != null){
                    float value = ((ConstFloat) operand).value;
                    return new ConstFloat(value == 0 ? 1 : 0);
                }
            }else{
                if(ctx.unaryOp().ADD() != null){
                    return operand;
                }
                if(ctx.unaryOp().MINUS() != null){
                    if(operand.getType().getTypeID() == Type.TypeID.IntegerTyID){
                        return BinaryInst.create(basicBlock, Type.i32(), null, Instruction.InstType.SUB, ConstInt.create(0), operand);
                    }else{
                        return BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FSUB, ConstFloat.create(0), operand);
                    }
                }
                if(ctx.unaryOp().NOT() != null){
                    if(operand.getType().getTypeID() == Type.TypeID.IntegerTyID){
                        return CmpInst.create(basicBlock, null, Instruction.InstType.ICMPEQ, ConstInt.create(0), operand);
                    }else{
                        return CmpInst.create(basicBlock, null, Instruction.InstType.FCMPEQ, ConstFloat.create(0), operand);
                    }
                }
            }
        }else if(ctx.Identifier() != null){
               String type_name = ctx.Identifier().getText();
               Value type_ = symbolTableStack.findValue(type_name);
               if(ctx.funcRParams() != null){
                   List<SysYParser.ExprContext> paramsContext;
                   paramsContext = ctx.funcRParams().expr();
                   //循环visit expr
               }
        }
        return super.visitUnaryExpr(ctx);
    }

    /**
     * unaryOp
     *     :ADD|MINUS|NOT
     *     ;
     */
    @Override
    public Value visitUnaryOp(SysYParser.UnaryOpContext ctx) {
        return super.visitUnaryOp(ctx);
    }

    /**
     * funcRParams
     *     :expr (COMMA expr)*
     *     ;
     */
    @Override
    public Value visitFuncRParams(SysYParser.FuncRParamsContext ctx) {
        return super.visitFuncRParams(ctx);
    }

    /**
     * mulExpr
     *     :unaryExpr ((MUL|DIV|MOD) unaryExpr)*
     *     ;
     */
    @Override
    public Value visitMulExpr(SysYParser.MulExprContext ctx) {
        Value operand1 = visitUnaryExpr(ctx.unaryExpr(0));
        for(int i = 1; i < ctx.unaryExpr().size(); i++) {
            Value operand2 = visitUnaryExpr(ctx.unaryExpr(i));
            if(isConst(operand1) && isConst(operand2)) {
                if(ctx.getChild(2 * i - 1).getText().equals("*"))
                    operand1 = constMUL(operand1, operand2);
                else if(ctx.getChild(2*i-1).getText().equals("/"))
                    operand1 = constDIV(operand1, operand2);
                else if (ctx.getChild(2*i-1).getText().equals("%"))
                    operand1=constMOD(operand1,operand2);
            } else if(operand1.getType().getTypeID() == Type.TypeID.IntegerTyID & operand2.getType().getTypeID() == Type.TypeID.IntegerTyID) {
                if(ctx.getChild(2 * i - 1).getText().equals("*"))
                    operand1 = BinaryInst.create(basicBlock, Type.i32(), null, Instruction.InstType.MUL, operand1, operand2);
                else if(ctx.getChild(2*i-1).getText().equals("/"))
                    operand1 = BinaryInst.create(basicBlock, Type.i32(), null, Instruction.InstType.SDIV, operand1, operand2);
                else if(ctx.getChild(2*i-1).getText().equals("%"))
                    operand1 = BinaryInst.create(basicBlock, Type.i32(), null, Instruction.InstType.SREM, operand1, operand2);
            } else {
                //TODO
                if(operand1.getType().getTypeID() == Type.TypeID.IntegerTyID) {
                    operand1 = ConvertInst.create(basicBlock, null, null, Instruction.InstType.SITOFP, operand1, Type.f32());
                    if (ctx.getChild(2 * i - 1).getText().equals("*"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FMUL, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("/"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FDIV, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("%"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.SREM, operand1, operand2);
                }else if(operand2.getType().getTypeID() == Type.TypeID.IntegerTyID){
                    operand2 = ConvertInst.create(basicBlock, null, null, Instruction.InstType.SITOFP, operand2, Type.f32());
                    if (ctx.getChild(2 * i - 1).getText().equals("*"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FMUL, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("/"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FDIV, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("%"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.SREM, operand1, operand2);
                }else{
                    if (ctx.getChild(2 * i - 1).getText().equals("*"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FMUL, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("/"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FDIV, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("%"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.SREM, operand1, operand2);
                }
            }
        }
        return operand1;
    }

    private Value constMUL(Value v1, Value v2){
        if(v1 instanceof ConstInt) {
            if(v2 instanceof ConstInt) {
                return ConstInt.create(((ConstInt) v1).value * ((ConstInt) v2).value);
            } else if(v2 instanceof ConstFloat) {
                return ConstFloat.create(((ConstInt) v1).value * ((ConstFloat) v2).value);
            }
        } else if(v1 instanceof ConstFloat) {
            if(v2 instanceof ConstInt) {
                return ConstFloat.create(((ConstFloat) v1).value * ((ConstInt) v2).value);
            } else if(v2 instanceof ConstFloat) {
                return ConstFloat.create(((ConstFloat) v1).value * ((ConstFloat) v2).value);
            }
        }
        return null;
    }

    private Value constDIV(Value v1, Value v2){
        if(v1 instanceof ConstInt) {
            if(v2 instanceof ConstInt) {
                return ConstInt.create(((ConstInt) v1).value / ((ConstInt) v2).value);
            } else if(v2 instanceof ConstFloat) {
                return ConstFloat.create(((ConstInt) v1).value / ((ConstFloat) v2).value);
            }
        } else if(v1 instanceof ConstFloat) {
            if(v2 instanceof ConstInt) {
                return ConstFloat.create(((ConstFloat) v1).value / ((ConstInt) v2).value);
            } else if(v2 instanceof ConstFloat) {
                return ConstFloat.create(((ConstFloat) v1).value / ((ConstFloat) v2).value);
            }
        }
        return null;
    }

    private Value constMOD(Value v1, Value v2){
        if(v1 instanceof ConstInt) {
            if(v2 instanceof ConstInt) {
                return ConstInt.create(((ConstInt) v1).value % ((ConstInt) v2).value);
            } else if(v2 instanceof ConstFloat) {
                return ConstFloat.create(((ConstInt) v1).value % ((ConstFloat) v2).value);
            }
        } else if(v1 instanceof ConstFloat) {
            if(v2 instanceof ConstInt) {
                return ConstFloat.create(((ConstFloat) v1).value % ((ConstInt) v2).value);
            } else if(v2 instanceof ConstFloat) {
                return ConstFloat.create(((ConstFloat) v1).value % ((ConstFloat) v2).value);
            }
        }
        return null;
    }
    /**
     * addExpr
     *     :mulExpr ((ADD|MINUS) mulExpr)*
     *     ;
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
                if(operand1.getType().getTypeID() == Type.TypeID.IntegerTyID) {
                    operand1 = ConvertInst.create(basicBlock, null, null, Instruction.InstType.SITOFP, operand1, Type.f32());
                    if (ctx.getChild(2 * i - 1).getText().equals("+"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FADD, operand1, operand2);
                    else
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FSUB, operand1, operand2);
                }else if(operand2.getType().getTypeID() == Type.TypeID.IntegerTyID){
                    operand2 = ConvertInst.create(basicBlock, null, null, Instruction.InstType.SITOFP, operand2, Type.f32());
                    if (ctx.getChild(2 * i - 1).getText().equals("+"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FADD, operand1, operand2);
                    else
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FSUB, operand1, operand2);
                }else{
                    if (ctx.getChild(2 * i - 1).getText().equals("+"))
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FADD, operand1, operand2);
                    else
                        operand1 = BinaryInst.create(basicBlock, Type.f32(), null, Instruction.InstType.FSUB, operand1, operand2);
                }
            }
        }
        return operand1;
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
     */
    @Override
    public Value visitRelExpr(SysYParser.RelExprContext ctx) {
        return super.visitRelExpr(ctx);
    }

    /**
     * eqExpr
     *     :relExpr ((EQ|NE) relExpr)*
     *     ;
     */
    @Override
    public Value visitEqExpr(SysYParser.EqExprContext ctx) {
        return super.visitEqExpr(ctx);
    }

    /**
     * lAndExpr
     *     :eqExpr (AND lAndExpr)*
     *     ;
     */
    @Override
    public Value visitLAndExpr(SysYParser.LAndExprContext ctx) {
        return super.visitLAndExpr(ctx);
    }

    /**
     * lOrExpr
     *     :lAndExpr (OR lAndExpr)*
     *     ;
     */
    @Override
    public Value visitLOrExpr(SysYParser.LOrExprContext ctx) {
        return super.visitLOrExpr(ctx);
    }

    /**
     * constExpr
     *     :addExpr
     *     ;
     */
    @Override
    public Value visitConstExpr(SysYParser.ConstExprContext ctx) {
        this.visitCtx.isConstExpr = true;
        return super.visitConstExpr(ctx);
    }
}