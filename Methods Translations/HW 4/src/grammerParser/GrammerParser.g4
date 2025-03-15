grammar GrammerParser;

grammarParser:
    parserName header globals? fields? parserRules ;

parserName:
    GRAMMAR NAME ;

header:
    HEADER CODE ;

globals:
    GLOBALS SQUARE_OPEN global+ SQUARE_CLOSE ;
global:
    NAME EQUALS TYPE SEMICOLON ;

fields:
    FIELDS SQUARE_OPEN field+ SQUARE_CLOSE ;
field:
    NAME EQUALS NAME SEMICOLON ;

parserRules:
    principle+ ;
principle:
    token | grammerRule ;

token:
    TOKEN COLON REGEX CODE? SKIP_RULE? SEMICOLON ;

grammerRule:
    NAME constructor? COLON contentRule SEMICOLON ;
constructor:
    SQUARE_OPEN NAME COLON NAME (SEMICOLON NAME COLON NAME)? SQUARE_CLOSE ;
contentRule:
    name+ CODE? |
    contentRule OR contentRule ;
name:
    TOKEN |
    NAME FUNC_ARGUMENT* ;


GRAMMAR: 'grammar' ;
IMPORT: 'import' ;
HEADER: 'header' ;
FIELDS: 'fields' ;
GLOBALS: 'globals' ;
PACKAGE: 'package' ;
SKIP_RULE: '-> skip' ;

POINT: '.' ;
COMMA: ',' ;
OR: '|' ;
COLON: ':' ;
SEMICOLON: ';' ;
STAR: '*' ;
OPEN_BRACKET: '(' ;
CLOSE_BRACKET: ')' ;
SQUARE_OPEN: '[' ;
SQUARE_CLOSE: ']' ;
EQUALS: '=' ;
FIGURE_OPEN: '{' ;
FIGURE_CLOSE: '}' ;
MORE_RULE: '>' ;
LESS: '<' ;

TOKEN: [A-Z_]+ ;
NAME: [a-zA-Z0-9]+ ;
TYPE: NAME (LESS NAME COMMA NAME MORE_RULE)? (OPEN_BRACKET CLOSE_BRACKET)? ;
REGEX: '"'.*?'"' ;
CODE: FIGURE_OPEN .+? FIGURE_CLOSE ;
FUNC_ARGUMENT: OPEN_BRACKET .+? CLOSE_BRACKET ;
WHITESPACE: [ \t\r\n] -> skip ;