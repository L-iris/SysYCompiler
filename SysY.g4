grammar SysY;

program
    : compUnit
    ;

compUnit
    : (funcDef|decl)+
    ;

decl
    : constDecl
    | varDecl
    ;

constDecl
    : CONST bType constDef (COMMA constDef)* SEMICOLON
    ;

bType
    :INT | FLOAT
    ;

constDef
    :Identifier (LB constExpr RB)* ASSIGN constInitVal
    ;

constInitVal
    :constExpr
    |(LC (constInitVal (COMMA constInitVal)*)? RC)
    ;

varDecl
    :bType varDef (COMMA varDef)* SEMICOLON
    ;

varDef
    :Identifier (LB constExpr RB)* (ASSIGN initVal)?
    ;

initVal
    :expr
    |(LC (initVal (COMMA initVal)*)? RC)
    ;

funcDef
    :funcType Identifier LP (funcFParams)? RP block
    ;

funcType
    :INT|VOID|FLOAT
    ;

funcFParams
    :funcFParam (COMMA funcFParam)*
    ;

funcFParam
    :bType Identifier (LB RB (LB expr RB)*)?
    ;

block
    :LC blockItem* RC
    ;

blockItem
    :decl|stmt
    ;

stmt
    :(lVal ASSIGN expr SEMICOLON)
    |((expr)? SEMICOLON)
    |block
    |(IF LP cond RP stmt (ELSE stmt)?)
    |(WHILE LP cond RP stmt)
    |(BREAK SEMICOLON)
    |(CONTINUE SEMICOLON)
    |(RETURN expr? SEMICOLON)
    ;

expr
    :addExpr
    ;

cond
    :lOrExpr
    ;

lVal
    :Identifier (LB expr RB)*
    ;

primaryExpr
    :(LP expr RP)
    |lVal
    |number
    ;

number
    :IntConst
    |FloatConst
    ;

unaryExpr
    :primaryExpr
    |(Identifier LP (funcRParams)? RP)
    |(unaryOp unaryExpr)
    ;

unaryOp
    :ADD|MINUS|NOT
    ;

funcRParams
    :expr (COMMA expr)*
    ;

mulExpr
    :unaryExpr ((MUL|DIV|MOD) unaryExpr)*
    ;

addExpr
    :mulExpr ((ADD|MINUS) mulExpr)*
    ;

relExpr
    :addExpr ((LT|GT|LE|GE) addExpr)*
    ;

eqExpr
    :relExpr ((EQ|NE) relExpr)*
    ;

lAndExpr
    :eqExpr (AND lAndExpr)*
    ;

lOrExpr
    :lAndExpr (OR lAndExpr)*
    ;

constExpr
    :addExpr
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
    :   [_a-zA-Z]
    |   [_a-zA-Z][_a-zA-Z0-9]+
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