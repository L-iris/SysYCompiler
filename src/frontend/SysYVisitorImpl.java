package frontend;

import ir.Module;
import ir.*;
import ir.constval.ConstArray;
import ir.constval.ConstFloat;
import ir.constval.ConstInt;
import ir.instructions.*;
import ir.types.Type;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import util.SymbolTableStack;
import util.frontend.SysYBaseVisitor;
import util.frontend.SysYLexer;
import util.frontend.SysYParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SysYVisitorImpl extends SysYBaseVisitor<Value> {

    private static class VisitCtx {
        public Type retType;
        public Function function;
        public BasicBlock basicBlock;

        enum Phase {
            GlobalVariable,
            GlobalArray,
            Function,
        }

        class Loop {
            BasicBlock condBr;
            BasicBlock trueBr;
            BasicBlock falseBr;
        }

        Loop loop;
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
                    initVal = visit(ctx.constInitVal());
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
                    initVal = visit(ctx.constInitVal());
                else{
                    if(this.visitCtx.bType.equals(Type.i32()))
                        initVal = ConstArray.create(ConstInt.create(0), dims);
                    else
                        initVal = ConstArray.create(ConstFloat.create(0), dims);
                }
            }
            retVal = GlobalVariable.create(module, bType, ctx.Identifier().getText() + ".addr", false, initVal);
            this.symbolTableStack.addValue(ctx.Identifier().getText(), retVal);
            this.module.globalVariables.insertAtEnd((GlobalVariable)retVal);
        } else { //局部
            if(ctx.LB().size() == 0) { //局部变量
                if(ctx.ASSIGN() != null)
                    initVal = visit(ctx.constInitVal());
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
                    initVal = visit(ctx.constInitVal());
                else
                    initVal = null;
            }
            retVal = AllocInst.create(this.visitCtx.basicBlock, ctx.Identifier().getText() + ".addr", bType);
            if(initVal != null)
                StoreInst.create(this.visitCtx.basicBlock, initVal, retVal);
            symbolTableStack.addValue(ctx.Identifier().getText(), retVal);
        }
        return retVal;
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
            this.module.globalVariables.insertAtEnd(((GlobalVariable) retVal));
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
            retVal = AllocInst.create(this.visitCtx.basicBlock, ctx.Identifier().getText() + ".addr", bType);
            if(initVal != null)
                StoreInst.create(this.visitCtx.basicBlock, initVal, retVal);
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
        this.visitCtx.function = null;
        this.visitCtx.isGlobal = true;
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
        Type type = bType;
        if(ctx.LB().size() != 0) {
            List<SysYParser.ExprContext> exprs = ctx.expr();
            for(int i=exprs.size()-1; i>=0; i--) {
                ConstInt value = (ConstInt) visit(exprs.get(i));
                type = Type.arrayType(type, value.value);
            }
        }
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
        BasicBlock basicBlock;
        if(this.visitCtx.args != null) {
            basicBlock = BasicBlock.create(this.visitCtx.function, null);
            this.visitCtx.basicBlock = basicBlock;
            this.visitCtx.function.basicBlockIlist.insertAtEnd(basicBlock);
            for(var arg : this.visitCtx.args){
                basicBlock.instructionIList.insertAtEnd(AllocInst.create(basicBlock, null, arg.getType()));
            }
            this.visitCtx.args = null;
        } else {
            basicBlock = BasicBlock.create(this.visitCtx.function, null);
            this.visitCtx.basicBlock.setNext(basicBlock);
            basicBlock.setPrev(this.visitCtx.basicBlock);
            this.visitCtx.basicBlock = basicBlock;
            symbolTableStack.enterScope();
            for(var bi:ctx.blockItem()){
                visit(bi);
            }
            symbolTableStack.exitScope();
        }
        return basicBlock;
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
            visitAssign(ctx);
        } else if(ctx.block() != null) {
            visit(ctx.block());
        } else if(ctx.IF() != null) {
            visitIf(ctx);
        } else if(ctx.WHILE() != null) {
            visitWhile(ctx);
        } else if(ctx.BREAK() != null) {
            visitBreak(ctx);
        } else if(ctx.CONTINUE() != null) {
            visitContinue(ctx);
        } else if(ctx.RETURN() != null) {
            visitReturn(ctx);
        } else {
            visit(ctx.expr());
        }
        return null;
    }

    private Value visitAssign(SysYParser.StmtContext ctx) {
        SysYParser.LValContext lVal = ctx.lVal();
        SysYParser.ExprContext expr = ctx.expr();
        Value var = visit(lVal);
        Value val = visit(expr);
        return StoreInst.create(visitCtx.basicBlock, val, var);
    }

    private Value visitIf(SysYParser.StmtContext ctx) {
        Value cond = visit(ctx.cond());
        BasicBlock trueBr = BasicBlock.create(visitCtx.function, null);
        BasicBlock falseBr = BasicBlock.create(visitCtx.function, null);
        BrInst brInst = BrInst.create(visitCtx.basicBlock, cond, trueBr, falseBr);
        visitCtx.basicBlock = trueBr;
        visit(ctx.stmt(0));
        visitCtx.basicBlock = falseBr;
        if(ctx.stmt().size() == 2)
            visit(ctx.stmt(1));
        return brInst;
    }

    private Value visitWhile(SysYParser.StmtContext ctx) {
        BasicBlock condBr = BasicBlock.create(visitCtx.function, null);
        visitCtx.basicBlock = condBr;
        Value cond = visit(ctx.cond());
        BasicBlock trueBr = BasicBlock.create(visitCtx.function, null);
        BasicBlock falseBr = BasicBlock.create(visitCtx.function, null);
        BrInst brInst = BrInst.create(visitCtx.basicBlock, cond, trueBr, falseBr);
        visitCtx.loop.condBr = condBr;
        visitCtx.loop.trueBr = trueBr;
        visitCtx.loop.falseBr = falseBr;
        visitCtx.basicBlock = trueBr;
        visit(ctx.stmt(0));
        BrInst.create(visitCtx.basicBlock, condBr);
        visitCtx.basicBlock = falseBr;
        return brInst;
    }

    private Value visitBreak(SysYParser.StmtContext ctx) {
        return BrInst.create(visitCtx.basicBlock, visitCtx.loop.falseBr);
    }

    private Value visitContinue(SysYParser.StmtContext ctx) {
        return BrInst.create(visitCtx.basicBlock, visitCtx.loop.condBr);
    }

    private Value visitReturn(SysYParser.StmtContext ctx) {
        Value v = null;
        if(ctx.expr() != null)
            v = visit(ctx.expr());
        return RetInst.create(visitCtx.basicBlock, v);
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
    //FIXME
    @Override
    public Value visitLVal(SysYParser.LValContext ctx) {
        String var = ctx.Identifier().getText();
        Value var_ = symbolTableStack.findValue(var);
        if(ctx.expr().size() == 0){
            if(var_.getType().getTypeID() == Type.TypeID.ArrayTyID){
                return var_;
            }else{
                return LoadInst.create(visitCtx.basicBlock, null, var_);
            }
        }else{
            int arr_dim = ctx.expr().size();
            for (int i = 0; i < arr_dim; i++) {
                Value tmp = visitExpr(ctx.expr(i));
                //TODO
            }
        }
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
        if(ctx.expr() != null){
            return visitExpr(ctx.expr());
        }else if(ctx.lVal() != null){
            return visitLVal(ctx.lVal());
        }else if(ctx.number() != null){
            return visitNumber(ctx.number());
        }
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
        if(ctx.IntConst() != null){
            //FIXME
            int type_ = 13;//ctx.IntConst().getSymbol().getType();
            if(type_ == SysYLexer.DECIMAL_CONST){
                return ConstInt.create(new BigInteger(ctx.IntConst().getText(),10).intValue());
            }else if(type_ == SysYLexer.HEXADECIMAL_CONST){
                return ConstInt.create(new BigInteger(ctx.IntConst().getText().substring(2),16).intValue());
            }else if(type_ == SysYLexer.OCTAL_CONST){
                return ConstInt.create(new BigInteger(ctx.IntConst().getText(),8).intValue());
            }
        }else if (ctx.FloatConst() != null){
                return ConstFloat.create(new BigDecimal(ctx.FloatConst().getText()).floatValue());
        }
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
                    return ConstInt.create(value);
                }
                if(ctx.unaryOp().NOT() != null){
                    int value = ((ConstInt) operand).value;
                    return ConstInt.create(value == 0 ? 1 :0);
                }
            }else if(operand instanceof ConstFloat){
                if(ctx.unaryOp().ADD() != null){
                    return operand;
                }
                if(ctx.unaryOp().MINUS() != null){
                    float value = ((ConstFloat) operand).value;
                    return ConstFloat.create(value);
                }
                if(ctx.unaryOp().NOT() != null){
                    float value = ((ConstFloat) operand).value;
                    return ConstFloat.create(value == 0 ? 1 : 0) ;
                }
            }else{
                if(ctx.unaryOp().ADD() != null){
                    return operand;
                }
                if(ctx.unaryOp().MINUS() != null){
                    if(operand.getType().getTypeID() == Type.TypeID.IntegerTyID){
                        return BinaryInst.create(this.visitCtx.basicBlock, Type.i32(), null, Instruction.InstType.SUB, ConstInt.create(0), operand);
                    }else{
                        return BinaryInst.create(this.visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FSUB, ConstFloat.create(0), operand);
                    }
                }
                if(ctx.unaryOp().NOT() != null){
                    if(operand.getType().getTypeID() == Type.TypeID.IntegerTyID){
                        return CmpInst.create(this.visitCtx.basicBlock, null, Instruction.InstType.ICMPEQ, ConstInt.create(0), operand);
                    }else{
                        return CmpInst.create(this.visitCtx.basicBlock, null, Instruction.InstType.FCMPEQ, ConstFloat.create(0), operand);
                    }
                }
            }
        }else if(ctx.Identifier() != null){
               String type_name = ctx.Identifier().getText();
               Value type_ = symbolTableStack.findValueCurrentScope(type_name);
               assert type_ instanceof Function;
               if(ctx.funcRParams() != null){
                   Value[] params = new Value[ctx.funcRParams().expr().size()];
                   //循环visit expr
                   for(int i = 0;i<ctx.funcRParams().expr().size();i++){
                       params[i] = visitExpr(ctx.funcRParams().expr(i));
                   }
                   CallInst.create(visitCtx.basicBlock, null, (Function) type_, params);
               }
               CallInst.create(visitCtx.basicBlock, null, (Function) type_);
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
                    operand1 = BinaryInst.create(visitCtx.basicBlock, Type.i32(), null, Instruction.InstType.MUL, operand1, operand2);
                else if(ctx.getChild(2*i-1).getText().equals("/"))
                    operand1 = BinaryInst.create(visitCtx.basicBlock, Type.i32(), null, Instruction.InstType.SDIV, operand1, operand2);
                else if(ctx.getChild(2*i-1).getText().equals("%"))
                    operand1 = BinaryInst.create(visitCtx.basicBlock, Type.i32(), null, Instruction.InstType.SREM, operand1, operand2);
            } else {
                if(operand1.getType().getTypeID() == Type.TypeID.IntegerTyID) {
                    operand1 = ConvertInst.create(visitCtx.basicBlock, null, null, Instruction.InstType.SITOFP, operand1, Type.f32());
                    if (ctx.getChild(2 * i - 1).getText().equals("*"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FMUL, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("/"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FDIV, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("%"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.SREM, operand1, operand2);
                }else if(operand2.getType().getTypeID() == Type.TypeID.IntegerTyID){
                    operand2 = ConvertInst.create(visitCtx.basicBlock, null, null, Instruction.InstType.SITOFP, operand2, Type.f32());
                    if (ctx.getChild(2 * i - 1).getText().equals("*"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FMUL, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("/"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FDIV, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("%"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.SREM, operand1, operand2);
                }else{
                    if (ctx.getChild(2 * i - 1).getText().equals("*"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FMUL, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("/"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FDIV, operand1, operand2);
                    else if(ctx.getChild(2*i-1).getText().equals("%"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.SREM, operand1, operand2);
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
                    operand1 = BinaryInst.create(visitCtx.basicBlock, Type.i32(), null, Instruction.InstType.ADD, operand1, operand2);
                else
                    operand1 = BinaryInst.create(visitCtx.basicBlock, Type.i32(), null, Instruction.InstType.SUB, operand1, operand2);
            } else {
                if(operand1.getType().getTypeID() == Type.TypeID.IntegerTyID) {
                    operand1 = ConvertInst.create(visitCtx.basicBlock, null, null, Instruction.InstType.SITOFP, operand1, Type.f32());
                    if (ctx.getChild(2 * i - 1).getText().equals("+"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FADD, operand1, operand2);
                    else
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FSUB, operand1, operand2);
                }else if(operand2.getType().getTypeID() == Type.TypeID.IntegerTyID){
                    operand2 = ConvertInst.create(visitCtx.basicBlock, null, null, Instruction.InstType.SITOFP, operand2, Type.f32());
                    if (ctx.getChild(2 * i - 1).getText().equals("+"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FADD, operand1, operand2);
                    else
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FSUB, operand1, operand2);
                }else{
                    if (ctx.getChild(2 * i - 1).getText().equals("+"))
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FADD, operand1, operand2);
                    else
                        operand1 = BinaryInst.create(visitCtx.basicBlock, Type.f32(), null, Instruction.InstType.FSUB, operand1, operand2);
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
        Value operand1 = visitAddExpr(ctx.addExpr(0));
        for(int i = 1; i<ctx.addExpr().size(); i++){
            Value operand2 = visitAddExpr(ctx.addExpr(i));
            if(operand1.getType().getTypeID() == Type.TypeID.IntegerTyID && operand2.getType().getTypeID() == Type.TypeID.IntegerTyID){
                if(ctx.getChild(2 * i - 1).getText() == "<"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.ICMPSLT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.ICMPSGT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == "<="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.ICMPSLE, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.ICMPSGE, operand1, operand2);
                }
            }else if(operand1.getType().getTypeID() == Type.TypeID.IntegerTyID && operand2.getType().getTypeID() == Type.TypeID.FloatTyID){
                operand1 = ConvertInst.create(visitCtx.basicBlock, null, null, Instruction.InstType.SITOFP, operand1, Type.f32());
                if(ctx.getChild(2 * i - 1).getText() == "<"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOLT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOGT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == "<="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOLE, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOGE, operand1, operand2);
                }
            }else if(operand1.getType().getTypeID() == Type.TypeID.FloatTyID && operand2.getType().getTypeID() == Type.TypeID.IntegerTyID){
                operand2 = ConvertInst.create(visitCtx.basicBlock, null, null, Instruction.InstType.SITOFP, operand2,Type.f32());
                if(ctx.getChild(2 * i - 1).getText() == "<"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOLT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOGT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == "<="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOLE, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOGE, operand1, operand2);
                }
            }else if(operand1.getType().getTypeID() == Type.TypeID.FloatTyID && operand2.getType().getTypeID() == Type.TypeID.FloatTyID){
                if(ctx.getChild(2 * i - 1).getText() == "<"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOLT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOGT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == "<="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOLE, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOGE, operand1, operand2);
                }
            }else if(operand1.getType().getTypeID() == Type.TypeID.BooleanTyID && operand2.getType().getTypeID() == Type.TypeID.IntegerTyID){
                operand1 = ConvertInst.create(visitCtx.basicBlock, null, null, Instruction.InstType.ZEXT, operand1, Type.i32());
                if(ctx.getChild(2 * i - 1).getText() == "<"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.ICMPSLT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.ICMPSGT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == "<="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.ICMPSLE, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.ICMPSGE, operand1, operand2);
                }
            }else if(operand1.getType().getTypeID() == Type.TypeID.BooleanTyID && operand2.getType().getTypeID() == Type.TypeID.FloatTyID){
                operand1 = ConvertInst.create(visitCtx.basicBlock, null, null, Instruction.InstType.ZEXT, operand1, Type.f32());
                if(ctx.getChild(2 * i - 1).getText() == "<"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOLT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">"){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOGT, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == "<="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOLE, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == ">="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPOGE, operand1, operand2);
                }
            }
        }
        return operand1;
    }

    /**
     * eqExpr
     *     :relExpr ((EQ|NE) relExpr)*
     *     ;
     */
    @Override
    public Value visitEqExpr(SysYParser.EqExprContext ctx) {
        Value operand1 = visitRelExpr(ctx.relExpr(0));
        for(int i = 1; i < ctx.relExpr().size(); i++){
            Value operand2 = visitRelExpr(ctx.relExpr(i));
            if(operand2.getType().getTypeID() == Type.TypeID.IntegerTyID){
                operand1 = ConvertInst.create(visitCtx.basicBlock, null, null, Instruction.InstType.ZEXT, operand1, Type.i32());
                if(ctx.getChild(2 * i - 1).getText() == "=="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.ICMPEQ, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == "!="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.ICMPNE, operand1, operand2);
                }
            }else if(operand2.getType().getTypeID() == Type.TypeID.FloatTyID){
                operand1 = ConvertInst.create(visitCtx.basicBlock, null, null, Instruction.InstType.ZEXT, operand1, Type.f32());
                if(ctx.getChild(2 * i - 1).getText() == "=="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPEQ, operand1, operand2);
                }
                if(ctx.getChild(2 * i - 1).getText() == "!="){
                    operand1 = CmpInst.create(visitCtx.basicBlock, null, Instruction.InstType.FCMPNE, operand1, operand2);
                }
            }
        }
        return operand1;
    }

    /**
     * lAndExpr
     *     :eqExpr (AND eqExpr)*
     *     ;
     */
    @Override
    public Value visitLAndExpr(SysYParser.LAndExprContext ctx) {
        Value operand1 = visitEqExpr(ctx.eqExpr(0));
        for(int i = 1; i < ctx.eqExpr().size(); i++){
            Value operand2 = visitEqExpr(ctx.eqExpr(i));
            operand1 = BinaryInst.create(visitCtx.basicBlock, null, Type.i1(), null, Instruction.InstType.AND, operand1, operand2);
        }
        return operand1;
    }

    /**
     * lOrExpr
     *     :lAndExpr (OR lAndExpr)*
     *     ;
     */
    @Override
    public Value visitLOrExpr(SysYParser.LOrExprContext ctx) {
        Value operand1 = visitLAndExpr(ctx.lAndExpr(0));
        for(int i = 1;i < ctx.lAndExpr().size(); i++){
            Value operand2 = visitLAndExpr(ctx.lAndExpr(i));
            operand1 = BinaryInst.create(visitCtx.basicBlock, null, Type.i1(), null, Instruction.InstType.OR, operand1, operand2);
        }
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