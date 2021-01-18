grammar Calc;

@header {
import java.util.HashMap;
}

@members{
HashMap<String, Integer> map = new HashMap<>();
}

calc returns [String value]
	: statement+ {$value=$statement.value;}
	;

statement returns [String value]
	: ID '=' expr ';'{
	map.put($ID.text,$expr.value);
	System.out.println($ID.text+" = "+$expr.value);
	}
	;

expr returns [Integer value]
	: me1=multExpression {$value = $me1.value;}
	('+' me2=multExpression {$value += $me2.value;}
	|'-' me2=multExpression {$value -= $me2.value;})*
	;

multExpression returns [Integer value]
	: a1=logExpression  {$value = $a1.value;}
    ('*' a2=logExpression  {$value *= $a2.value;}
    |'/' a2=logExpression  {$value /= $a2.value;})*
    ;

logExpression returns [Integer value]
	: a1=atom {$value = $a1.value;}
    ('//' a2=logExpression {$value = (int) (Math.log($value)/Math.log($a2.value));})*
    ;

atom returns [Integer value]
	: ('+')? ID {$value = map.get($ID.text);}
	| '-' ID {$value = -map.get($ID.text);}
	| ('+')? INT {$value = Integer.parseInt($INT.text);}
	| '-' INT {$value = -Integer.parseInt($INT.text);}
	| '(' expr ')' {$value = $expr.value;}
	;

ID : [a-z]+ ;
INT : [0-9]+ ;
WS : [ \t\r\n]+ -> skip ;