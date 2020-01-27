grammar Orwell;
expr: PAREN_L expr PAREN_R | expr BINOP expr | NUM | fun_dec;
fun_dec: FUN_KEYWORD ID PAREN_L param_list PAREN_R | FUN_KEYWORD ID;
param_list: ID COLON TYPE COMMA param_list | ID COLON TYPE;
BINOP: '+' | '-' | '*' | '/' | '%' ;
NUM: INT | FLOAT;
INT: DIGIT+;
FLOAT: DIGIT+'.'DIGIT+;
PAREN_L: '(';
PAREN_R: ')';
fragment DIGIT: [0-9]+;
FUN_KEYWORD: 'fun';
TYPE: 'int' | 'float';
ID: [a-zA-Z_]+;
WS: [ \n\t\r]+ -> skip;
COLON: ':';
COMMA: ',';
