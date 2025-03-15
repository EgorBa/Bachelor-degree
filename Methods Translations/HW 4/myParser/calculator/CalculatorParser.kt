package calculator
import java.util.ArrayList
import java.util.HashMap

class CalculatorParser (private val lexer: CalculatorLexer) {

    
    
    init {
        lexer.nextToken()
    }
    
    class Node(val text: String, val token: CalculatorToken) {
        var value = 0
		var comp = 0
        val children: MutableList<Node> = ArrayList()
    }

    class ParseException(message: String?) : RuntimeException(message)
    
    fun start (): Node {
        val res = Node("start", CalculatorToken.start)
        when (val currentToken = lexer.curToken.token) {
            CalculatorToken.COMP -> {
                val expr = expr()
                res.children.add(expr)
                println(expr.value.toString()  +" "+expr.comp.toString() +"i")
            }
			CalculatorToken.NUMBER -> {
                val expr = expr()
                res.children.add(expr)
                println(expr.value.toString()  +" "+expr.comp.toString() +"i")
            }
			CalculatorToken.MINUS -> {
                val expr = expr()
                res.children.add(expr)
                println(expr.value.toString()  +" "+expr.comp.toString() +"i")
            }
			CalculatorToken.OPEN -> {
                val expr = expr()
                res.children.add(expr)
                println(expr.value.toString()  +" "+expr.comp.toString() +"i")
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun l (): Node {
        val res = Node("l", CalculatorToken.l)
        when (val currentToken = lexer.curToken.token) {
            CalculatorToken.COMP -> {
                val t = t()
                res.children.add(t)
				val x = x(t.value,t.comp)
                res.children.add(x)
                res.value = x.value; res.comp = x.comp
            }
			CalculatorToken.NUMBER -> {
                val t = t()
                res.children.add(t)
				val x = x(t.value,t.comp)
                res.children.add(x)
                res.value = x.value; res.comp = x.comp
            }
			CalculatorToken.MINUS -> {
                val t = t()
                res.children.add(t)
				val x = x(t.value,t.comp)
                res.children.add(x)
                res.value = x.value; res.comp = x.comp
            }
			CalculatorToken.OPEN -> {
                val t = t()
                res.children.add(t)
				val x = x(t.value,t.comp)
                res.children.add(x)
                res.value = x.value; res.comp = x.comp
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun m (): Node {
        val res = Node("m", CalculatorToken.m)
        when (val currentToken = lexer.curToken.token) {
            CalculatorToken.COMP -> {
                val v = v()
                res.children.add(v)
                res.value = v.value; res.comp = v.comp
            }
			CalculatorToken.NUMBER -> {
                val v = v()
                res.children.add(v)
                res.value = v.value; res.comp = v.comp
            }
			CalculatorToken.MINUS -> {
                val MINUS = Node(lexer.curToken.text, CalculatorToken.MINUS)
                res.children.add(MINUS)
                next(CalculatorToken.MINUS)
				val v = v()
                res.children.add(v)
                res.value = -v.value; res.comp = -v.comp
            }
			CalculatorToken.OPEN -> {
                val v = v()
                res.children.add(v)
                res.value = v.value; res.comp = v.comp
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun p (): Node {
        val res = Node("p", CalculatorToken.p)
        when (val currentToken = lexer.curToken.token) {
            CalculatorToken.COMP -> {
                val m = m()
                res.children.add(m)
                res.value = m.value; res.comp = m.comp
            }
			CalculatorToken.NUMBER -> {
                val m = m()
                res.children.add(m)
                res.value = m.value; res.comp = m.comp
            }
			CalculatorToken.MINUS -> {
                val m = m()
                res.children.add(m)
                res.value = m.value; res.comp = m.comp
            }
			CalculatorToken.OPEN -> {
                val m = m()
                res.children.add(m)
                res.value = m.value; res.comp = m.comp
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun t (): Node {
        val res = Node("t", CalculatorToken.t)
        when (val currentToken = lexer.curToken.token) {
            CalculatorToken.COMP -> {
                val p = p()
                res.children.add(p)
				val y = y(p.value,p.comp)
                res.children.add(y)
                res.value = y.value; res.comp = y.comp
            }
			CalculatorToken.NUMBER -> {
                val p = p()
                res.children.add(p)
				val y = y(p.value,p.comp)
                res.children.add(y)
                res.value = y.value; res.comp = y.comp
            }
			CalculatorToken.MINUS -> {
                val p = p()
                res.children.add(p)
				val y = y(p.value,p.comp)
                res.children.add(y)
                res.value = y.value; res.comp = y.comp
            }
			CalculatorToken.OPEN -> {
                val p = p()
                res.children.add(p)
				val y = y(p.value,p.comp)
                res.children.add(y)
                res.value = y.value; res.comp = y.comp
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun v (): Node {
        val res = Node("v", CalculatorToken.v)
        when (val currentToken = lexer.curToken.token) {
            CalculatorToken.COMP -> {
                val COMP = Node(lexer.curToken.text, CalculatorToken.COMP)
                res.children.add(COMP)
                next(CalculatorToken.COMP)
                res.comp = Integer.parseInt(COMP.text.substring(0,COMP.text.length - 1))
            }
			CalculatorToken.NUMBER -> {
                val NUMBER = Node(lexer.curToken.text, CalculatorToken.NUMBER)
                res.children.add(NUMBER)
                next(CalculatorToken.NUMBER)
                res.value = Integer.parseInt(NUMBER.text)
            }
			CalculatorToken.OPEN -> {
                val OPEN = Node(lexer.curToken.text, CalculatorToken.OPEN)
                res.children.add(OPEN)
                next(CalculatorToken.OPEN)
				val expr = expr()
                res.children.add(expr)
				val CLOSE = Node(lexer.curToken.text, CalculatorToken.CLOSE)
                res.children.add(CLOSE)
                next(CalculatorToken.CLOSE)
                res.value = expr.value; res.comp = expr.comp
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun x (acc : Int, accComp : Int): Node {
        val res = Node("x", CalculatorToken.x)
        when (val currentToken = lexer.curToken.token) {
            CalculatorToken.PLUS -> {
                val PLUS = Node(lexer.curToken.text, CalculatorToken.PLUS)
                res.children.add(PLUS)
                next(CalculatorToken.PLUS)
				val t = t()
                res.children.add(t)
				val x = x(acc + t.value,accComp + t.comp)
                res.children.add(x)
                res.value = x.value; res.comp = x.comp
            }
			CalculatorToken.MINUS -> {
                val MINUS = Node(lexer.curToken.text, CalculatorToken.MINUS)
                res.children.add(MINUS)
                next(CalculatorToken.MINUS)
				val t = t()
                res.children.add(t)
				val x = x(acc - t.value,accComp - t.comp)
                res.children.add(x)
                res.value = x.value; res.comp = x.comp
            }
            CalculatorToken.EOF -> {res.value = acc; res.comp = accComp}
			CalculatorToken.CLOSE -> {res.value = acc; res.comp = accComp}
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun y (acc : Int, accComp : Int): Node {
        val res = Node("y", CalculatorToken.y)
        when (val currentToken = lexer.curToken.token) {
            CalculatorToken.MULTIPLY -> {
                val MULTIPLY = Node(lexer.curToken.text, CalculatorToken.MULTIPLY)
                res.children.add(MULTIPLY)
                next(CalculatorToken.MULTIPLY)
				val p = p()
                res.children.add(p)
				val y = y(acc * p.value - accComp * p.comp,acc * p.comp - p.value * accComp)
                res.children.add(y)
                res.value = y.value; res.comp = y.comp
            }
			CalculatorToken.DIVIDE -> {
                val DIVIDE = Node(lexer.curToken.text, CalculatorToken.DIVIDE)
                res.children.add(DIVIDE)
                next(CalculatorToken.DIVIDE)
				val p = p()
                res.children.add(p)
				val y = y((acc * p.value + accComp * p.comp)/(p.value*p.value + p.comp*p.comp),(accComp * p.value - acc * p.comp)/(p.value*p.value + p.comp*p.comp))
                res.children.add(y)
                res.value = y.value; res.comp = y.comp
            }
            CalculatorToken.PLUS -> {res.value = acc; res.comp = accComp}
			CalculatorToken.MINUS -> {res.value = acc; res.comp = accComp}
			CalculatorToken.EOF -> {res.value = acc; res.comp = accComp}
			CalculatorToken.CLOSE -> {res.value = acc; res.comp = accComp}
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun expr (): Node {
        val res = Node("expr", CalculatorToken.expr)
        when (val currentToken = lexer.curToken.token) {
            CalculatorToken.COMP -> {
                val l = l()
                res.children.add(l)
                res.value = l.value; res.comp = l.comp
            }
			CalculatorToken.NUMBER -> {
                val l = l()
                res.children.add(l)
                res.value = l.value; res.comp = l.comp
            }
			CalculatorToken.MINUS -> {
                val l = l()
                res.children.add(l)
                res.value = l.value; res.comp = l.comp
            }
			CalculatorToken.OPEN -> {
                val l = l()
                res.children.add(l)
                res.value = l.value; res.comp = l.comp
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }
    
    private fun next(expected: CalculatorToken) {
        val cur = lexer.curToken.token
        if (cur != expected) {
            throw ParseException("Unknown token : " + cur.name + ", expected token : " + expected.name)
        }
        lexer.nextToken()
    }
}