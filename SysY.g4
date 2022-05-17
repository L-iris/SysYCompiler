grammar SysY;

//some of the programming takes the ANTLR4 official document of C11 for reference
//https://github.com/antlr/grammars-v4/blob/master/c/C.g4

program
    : CompUnit
    ;

CompUnit
    : (FuncDef|Decl)+
    ;

Decl
    : ConstDecl
    | VarDecl
    ;

ConstDecl
    : CONST BType ConstDef (COMMA ConstDef)* SEMICOLON
    ;

BType
    :INT | FLOAT
    ;

ConstDef
    :Identifier (LB ConstExpr RB)* ASSIGN ConstInitVal
    ;

ConstInitVal
    :ConstExpr
    |(LC (ConstInitVal (COMMA ConstInitVal)*)? RC)
    ;

VarDecl
    :BType VarDef (COMMA VarDef)* SEMICOLON
    ;

VarDef
    :Identifier (LB ConstExpr RB)* (ASSIGN InitVal)?
    ;

InitVal
    :Expr
    |(LC (InitVal (COMMA InitVal)*)? RC)
    ;

FuncDef
    :FuncType Identifier LP (FuncFParams)? RP Block
    ;

FuncType
    :INT|VOID|FLOAT
    ;

FuncFParams
    :FuncFParam (COMMA FuncFParam)*
    ;

FuncFParam
    :BType Identifier (LB RB (LB Expr RB)*)?
    ;

Block
    :LC BlockItem* RC
    ;

BlockItem
    :Decl|Stmt
    ;

Stmt
    :(LVal ASSIGN Expr SEMICOLON)
    |((Expr)? SEMICOLON)
    |Block
    |(IF LP Cond RP Stmt (ELSE Stmt)?)
    |(WHILE LP Cond RP Stmt)
    |(BREAK SEMICOLON)
    |(CONTINUE SEMICOLON)
    |(RETURN Expr? SEMICOLON)
    ;

Expr
    :AddExpr
    ;

Cond
    :LOrExpr
    ;

LVal
    :Identifier (LB Expr RB)*
    ;

PrimaryExpr
    :(LP Expr RP)
    |LVal
    |Number
    ;

Number
    :IntConst
    |FloatConst
    ;

UnaryExpr
    :PrimaryExpr
    |(Identifier LP (FuncRParams)? RP)
    |(UnaryOp UnaryExpr)
    ;

UnaryOp
    :ADD|MINUS|NOT
    ;

FuncRParams
    :Expr (COMMA Expr)*
    ;

MulExpr
    :UnaryExpr ((MUL|DIV|MOD) UnaryExpr)*
    ;

AddExpr
    :MulExpr ((ADD|MINUS) MulExpr)*
    ;

RelExpr
    :AddExpr ((LT|GT|LE|GE) AddExpr)*
    ;

EqExpr
    :RelExpr ((EQ|NE) RelExpr)*
    ;

LAndExpr
    :EqExpr (AND LAndExpr)*
    ;

LOrExpr
    :LAndExpr (OR LAndExpr)*
    ;

ConstExpr
    :AddExpr
    ;

CONST
    : 'const'
    ;

INT
    : 'int'
    ;

FLOAT
    : 'float'
    ;

VOID
    : 'void'
    ;

IF
    : 'if'
    ;

ELSE
    : 'else'
    ;

WHILE
    : 'while'
    ;

BREAK
    : 'break'
    ;

CONTINUE
    : 'continue'
    ;

RETURN
    : 'return'
    ;

Identifier
    : [_a-zA-Z]
    |[_a-zA-Z][_a-zA-Z0-9]+
    ;

IntConst
    :DECIMAL_CONST
    |OCTAL_CONST
    |HEXADECIMAL_CONST
    ;

DECIMAL_CONST
    : [1-9]
    |[1-9][0-9]+
    ;

OCTAL_CONST
    : '0'
    |('0'[0-7]+)
    ;

HEXADECIMAL_CONST
    : ('0x'|'0X') [a-fA-F0-9]+
    ;

FloatConst
    :FractionalConst ExponentPart? FloatSuffix
    |DigitSequence ExponentPart FloatSuffix
    ;
/* this fractional const may like 01.04 for example, so start with digitsequence */
fragment FractionalConst
    :DigitSequence? '.' DigitSequence
    |DigitSequence '.'
    ;

fragment ExponentPart
    :[eE] Sign? DigitSequence
    ;

fragment Sign
    :[+-]
    ;

DigitSequence
    :[0-9]+
    ;

FloatSuffix
    :[fF]
    ;

STRING
    : DOUBLE_QUOTATION (ESC|.)*? DOUBLE_QUOTATION
    ;

fragment ESC
    : '\\"' | '\\\\'
    ;

DOUBLE_QUOTATION
    : '"'
    ;

ADD
    :'+'
    ;

MINUS
    :'-'
    ;

MUL
    :'*'
    ;

DIV
    :'/'
    ;

MOD
    :'%'
    ;

NOT
    :'!'
    ;

ASSIGN
    :'='
    ;

LT
    :'<'
    ;

GT
    :'>'
    ;

LE
    :'<='
    ;

GE
    :'>='
    ;

EQ
    :'=='
    ;

NE
    :'!='
    ;

AND
    :'&&'
    ;

OR
    :'||'
    ;

LP
    :'('
    ;

RP
    :')'
    ;

LB
    :'['
    ;

RB
    :']'
    ;

LC
    :'{'
    ;

RC
    :'}'
    ;

COMMA
    :','
    ;

SEMICOLON
    :';'
    ;

WS
    :[ \r\t\n]+ ->skip
    ;

LineComment
    :'//' ~ [\r\n]* ->skip
    ;

BlockComment
    :'/*' .*? '/*' ->skip
    ;