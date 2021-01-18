package calculatorwithmemory
import java.util.ArrayList
import java.util.HashMap

class CalculatorWithMemoryParser (private val lexer: CalculatorWithMemoryLexer) {

    private val map = HashMap<String,Int>()
    
    init {
        lexer.nextToken()
    }
    
    class Node(val text: String, val token: CalculatorWithMemoryToken) {
        var value = 0
        val children: MutableList<Node> = ArrayList()
    }

    class ParseException(message: String?) : RuntimeException(message)
    
    fun b (): Node {
        val res = Node("b", CalculatorWithMemoryToken.b)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.NAME -> {
                val start = start()
                res.children.add(start)
                
            }
            CalculatorWithMemoryToken.EOF -> {}
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun start (): Node {
        val res = Node("start", CalculatorWithMemoryToken.start)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.NAME -> {
                val s = s()
                res.children.add(s)
				val SEMICOLON = Node(lexer.curToken.text, CalculatorWithMemoryToken.SEMICOLON)
                res.children.add(SEMICOLON)
                next(CalculatorWithMemoryToken.SEMICOLON)
				val b = b()
                res.children.add(b)
                
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun l (): Node {
        val res = Node("l", CalculatorWithMemoryToken.l)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.NUMBER -> {
                val t = t()
                res.children.add(t)
				val x = x(t.value)
                res.children.add(x)
                res.value = x.value
            }
			CalculatorWithMemoryToken.MINUS -> {
                val t = t()
                res.children.add(t)
				val x = x(t.value)
                res.children.add(x)
                res.value = x.value
            }
			CalculatorWithMemoryToken.NAME -> {
                val t = t()
                res.children.add(t)
				val x = x(t.value)
                res.children.add(x)
                res.value = x.value
            }
			CalculatorWithMemoryToken.OPEN -> {
                val t = t()
                res.children.add(t)
				val x = x(t.value)
                res.children.add(x)
                res.value = x.value
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun m (): Node {
        val res = Node("m", CalculatorWithMemoryToken.m)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.NUMBER -> {
                val v = v()
                res.children.add(v)
                res.value = v.value
            }
			CalculatorWithMemoryToken.MINUS -> {
                val MINUS = Node(lexer.curToken.text, CalculatorWithMemoryToken.MINUS)
                res.children.add(MINUS)
                next(CalculatorWithMemoryToken.MINUS)
				val v = v()
                res.children.add(v)
                res.value = -v.value
            }
			CalculatorWithMemoryToken.NAME -> {
                val v = v()
                res.children.add(v)
                res.value = v.value
            }
			CalculatorWithMemoryToken.OPEN -> {
                val v = v()
                res.children.add(v)
                res.value = v.value
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun p (): Node {
        val res = Node("p", CalculatorWithMemoryToken.p)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.NUMBER -> {
                val m = m()
                res.children.add(m)
                res.value = m.value
            }
			CalculatorWithMemoryToken.MINUS -> {
                val m = m()
                res.children.add(m)
                res.value = m.value
            }
			CalculatorWithMemoryToken.NAME -> {
                val m = m()
                res.children.add(m)
                res.value = m.value
            }
			CalculatorWithMemoryToken.OPEN -> {
                val m = m()
                res.children.add(m)
                res.value = m.value
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun s (): Node {
        val res = Node("s", CalculatorWithMemoryToken.s)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.NAME -> {
                val NAME = Node(lexer.curToken.text, CalculatorWithMemoryToken.NAME)
                res.children.add(NAME)
                next(CalculatorWithMemoryToken.NAME)
				val EQUALS = Node(lexer.curToken.text, CalculatorWithMemoryToken.EQUALS)
                res.children.add(EQUALS)
                next(CalculatorWithMemoryToken.EQUALS)
				val expr = expr()
                res.children.add(expr)
                map[NAME.text] = expr.value; println(NAME.text + " = " + expr.value + ";")
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun t (): Node {
        val res = Node("t", CalculatorWithMemoryToken.t)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.NUMBER -> {
                val p = p()
                res.children.add(p)
				val y = y(p.value)
                res.children.add(y)
                res.value = y.value
            }
			CalculatorWithMemoryToken.MINUS -> {
                val p = p()
                res.children.add(p)
				val y = y(p.value)
                res.children.add(y)
                res.value = y.value
            }
			CalculatorWithMemoryToken.NAME -> {
                val p = p()
                res.children.add(p)
				val y = y(p.value)
                res.children.add(y)
                res.value = y.value
            }
			CalculatorWithMemoryToken.OPEN -> {
                val p = p()
                res.children.add(p)
				val y = y(p.value)
                res.children.add(y)
                res.value = y.value
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun v (): Node {
        val res = Node("v", CalculatorWithMemoryToken.v)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.NUMBER -> {
                val NUMBER = Node(lexer.curToken.text, CalculatorWithMemoryToken.NUMBER)
                res.children.add(NUMBER)
                next(CalculatorWithMemoryToken.NUMBER)
                res.value = Integer.parseInt(NUMBER.text)
            }
			CalculatorWithMemoryToken.NAME -> {
                val NAME = Node(lexer.curToken.text, CalculatorWithMemoryToken.NAME)
                res.children.add(NAME)
                next(CalculatorWithMemoryToken.NAME)
                res.value = map[NAME.text]!!
            }
			CalculatorWithMemoryToken.OPEN -> {
                val OPEN = Node(lexer.curToken.text, CalculatorWithMemoryToken.OPEN)
                res.children.add(OPEN)
                next(CalculatorWithMemoryToken.OPEN)
				val expr = expr()
                res.children.add(expr)
				val CLOSE = Node(lexer.curToken.text, CalculatorWithMemoryToken.CLOSE)
                res.children.add(CLOSE)
                next(CalculatorWithMemoryToken.CLOSE)
                res.value = expr.value
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun x (acc : Int): Node {
        val res = Node("x", CalculatorWithMemoryToken.x)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.PLUS -> {
                val PLUS = Node(lexer.curToken.text, CalculatorWithMemoryToken.PLUS)
                res.children.add(PLUS)
                next(CalculatorWithMemoryToken.PLUS)
				val t = t()
                res.children.add(t)
				val x = x(acc + t.value)
                res.children.add(x)
                res.value = x.value
            }
			CalculatorWithMemoryToken.MINUS -> {
                val MINUS = Node(lexer.curToken.text, CalculatorWithMemoryToken.MINUS)
                res.children.add(MINUS)
                next(CalculatorWithMemoryToken.MINUS)
				val t = t()
                res.children.add(t)
				val x = x(acc - t.value)
                res.children.add(x)
                res.value = x.value
            }
            CalculatorWithMemoryToken.SEMICOLON -> {res.value = acc}
			CalculatorWithMemoryToken.CLOSE -> {res.value = acc}
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun y (acc : Int): Node {
        val res = Node("y", CalculatorWithMemoryToken.y)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.MULTIPLY -> {
                val MULTIPLY = Node(lexer.curToken.text, CalculatorWithMemoryToken.MULTIPLY)
                res.children.add(MULTIPLY)
                next(CalculatorWithMemoryToken.MULTIPLY)
				val p = p()
                res.children.add(p)
				val y = y(acc * p.value)
                res.children.add(y)
                res.value = y.value
            }
			CalculatorWithMemoryToken.DIVIDE -> {
                val DIVIDE = Node(lexer.curToken.text, CalculatorWithMemoryToken.DIVIDE)
                res.children.add(DIVIDE)
                next(CalculatorWithMemoryToken.DIVIDE)
				val p = p()
                res.children.add(p)
				val y = y(acc / p.value)
                res.children.add(y)
                res.value = y.value
            }
            CalculatorWithMemoryToken.PLUS -> {res.value = acc}
			CalculatorWithMemoryToken.MINUS -> {res.value = acc}
			CalculatorWithMemoryToken.SEMICOLON -> {res.value = acc}
			CalculatorWithMemoryToken.CLOSE -> {res.value = acc}
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }

	fun expr (): Node {
        val res = Node("expr", CalculatorWithMemoryToken.expr)
        when (val currentToken = lexer.curToken.token) {
            CalculatorWithMemoryToken.NUMBER -> {
                val l = l()
                res.children.add(l)
                res.value = l.value
            }
			CalculatorWithMemoryToken.MINUS -> {
                val l = l()
                res.children.add(l)
                res.value = l.value
            }
			CalculatorWithMemoryToken.NAME -> {
                val l = l()
                res.children.add(l)
                res.value = l.value
            }
			CalculatorWithMemoryToken.OPEN -> {
                val l = l()
                res.children.add(l)
                res.value = l.value
            }
            
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }
    
    private fun next(expected: CalculatorWithMemoryToken) {
        val cur = lexer.curToken.token
        if (cur != expected) {
            throw ParseException("Unknown token : " + cur.name + ", expected token : " + expected.name)
        }
        lexer.nextToken()
    }
}