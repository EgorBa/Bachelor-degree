package launcher.calcul

import java.util.ArrayList
import java.util.HashMap

class CalculatorParserKT(private val lexer: CalculatorLexerKT) {
    private val map = HashMap<String, Int>()

    class Node(val text: String, val rule: RuleKT) {
        var `val` = 0
        val children: MutableList<Node> = ArrayList()
    }

    class ParseException(message: String?) : RuntimeException(message)

    fun all(): Node {
        val res = Node("all", RuleKT.all)
        val currentRule = lexer.curToken.rule
        when (currentRule) {
            RuleKT.NAME -> {
                val begin = begin()
                res.children.add(begin)
                val SEMICOLON = Node(lexer.curToken.text, RuleKT.SEMICOLON)
                res.children.add(SEMICOLON)
                consume(RuleKT.SEMICOLON)
                val b = b()
                res.children.add(b)
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun b(): Node {
        val res = Node("b", RuleKT.b)
        val currentRule = lexer.curToken.rule
        when (currentRule) {
            RuleKT.NAME -> {
                val all = all()
                res.children.add(all)
            }
            RuleKT.EOF -> {
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun f(): Node {
        val res = Node("f", RuleKT.f)
        when (val currentRule = lexer.curToken.rule) {
            RuleKT.NUMBER -> {
                val h = h()
                res.children.add(h)
                res.`val` = h.`val`
            }
            RuleKT.MINUS -> {
                val MINUS = Node(lexer.curToken.text, RuleKT.MINUS)
                res.children.add(MINUS)
                consume(RuleKT.MINUS)
                val h = h()
                res.children.add(h)
                res.`val` = -h.`val`
            }
            RuleKT.NAME -> {
                val h = h()
                res.children.add(h)
                res.`val` = h.`val`
            }
            RuleKT.OPEN -> {
                val h = h()
                res.children.add(h)
                res.`val` = h.`val`
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun g(): Node {
        val res = Node("g", RuleKT.g)
        val currentRule = lexer.curToken.rule
        when (currentRule) {
            RuleKT.NUMBER -> {
                val f = f()
                res.children.add(f)
                res.`val` = f.`val`
            }
            RuleKT.MINUS -> {
                val f = f()
                res.children.add(f)
                res.`val` = f.`val`
            }
            RuleKT.NAME -> {
                val f = f()
                res.children.add(f)
                res.`val` = f.`val`
            }
            RuleKT.OPEN -> {
                val f = f()
                res.children.add(f)
                res.`val` = f.`val`
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun start(): Node {
        val res = Node("start", RuleKT.start)
        val currentRule = lexer.curToken.rule
        when (currentRule) {
            RuleKT.NUMBER -> {
                val s = s()
                res.children.add(s)
                res.`val` = s.`val`
            }
            RuleKT.MINUS -> {
                val s = s()
                res.children.add(s)
                res.`val` = s.`val`
            }
            RuleKT.NAME -> {
                val s = s()
                res.children.add(s)
                res.`val` = s.`val`
            }
            RuleKT.OPEN -> {
                val s = s()
                res.children.add(s)
                res.`val` = s.`val`
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun h(): Node {
        val res = Node("h", RuleKT.h)
        val currentRule = lexer.curToken.rule
        when (currentRule) {
            RuleKT.NUMBER -> {
                val j = j()
                res.children.add(j)
                val k = k()
                res.children.add(k)
                res.`val` = Math.pow(j.`val`.toDouble(), k.`val`.toDouble()).toInt()
            }
            RuleKT.NAME -> {
                val j = j()
                res.children.add(j)
                val k = k()
                res.children.add(k)
                res.`val` = Math.pow(j.`val`.toDouble(), k.`val`.toDouble()).toInt()
            }
            RuleKT.OPEN -> {
                val j = j()
                res.children.add(j)
                val k = k()
                res.children.add(k)
                res.`val` = Math.pow(j.`val`.toDouble(), k.`val`.toDouble()).toInt()
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun j(): Node {
        val res = Node("j", RuleKT.j)
        val currentRule = lexer.curToken.rule
        when (currentRule) {
            RuleKT.NUMBER -> {
                val NUMBER = Node(lexer.curToken.text, RuleKT.NUMBER)
                res.children.add(NUMBER)
                consume(RuleKT.NUMBER)
                res.`val` = NUMBER.text.toInt()
            }
            RuleKT.NAME -> {
                val NAME = Node(lexer.curToken.text, RuleKT.NAME)
                res.children.add(NAME)
                consume(RuleKT.NAME)
                res.`val` = map[NAME.text]!!
            }
            RuleKT.OPEN -> {
                val OPEN = Node(lexer.curToken.text, RuleKT.OPEN)
                res.children.add(OPEN)
                consume(RuleKT.OPEN)
                val start = start()
                res.children.add(start)
                val CLOSE = Node(lexer.curToken.text, RuleKT.CLOSE)
                res.children.add(CLOSE)
                consume(RuleKT.CLOSE)
                res.`val` = start.`val`
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun k(): Node {
        val res = Node("k", RuleKT.k)
        val currentRule = lexer.curToken.rule
        when (currentRule) {
            RuleKT.POW -> {
                val POW = Node(lexer.curToken.text, RuleKT.POW)
                res.children.add(POW)
                consume(RuleKT.POW)
                val h = h()
                res.children.add(h)
                res.`val` = h.`val`
            }
            RuleKT.MULTIPLY -> res.`val` = 1
            RuleKT.DIVIDE -> res.`val` = 1
            RuleKT.PLUS -> res.`val` = 1
            RuleKT.MINUS -> res.`val` = 1
            RuleKT.SEMICOLON -> res.`val` = 1
            RuleKT.CLOSE -> res.`val` = 1
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun s(): Node {
        val res = Node("s", RuleKT.s)
        val currentRule = lexer.curToken.rule
        when (currentRule) {
            RuleKT.NUMBER -> {
                val t = t()
                res.children.add(t)
                val x = x(t.`val`)
                res.children.add(x)
                res.`val` = x.`val`
            }
            RuleKT.MINUS -> {
                val t = t()
                res.children.add(t)
                val x = x(t.`val`)
                res.children.add(x)
                res.`val` = x.`val`
            }
            RuleKT.NAME -> {
                val t = t()
                res.children.add(t)
                val x = x(t.`val`)
                res.children.add(x)
                res.`val` = x.`val`
            }
            RuleKT.OPEN -> {
                val t = t()
                res.children.add(t)
                val x = x(t.`val`)
                res.children.add(x)
                res.`val` = x.`val`
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun t(): Node {
        val res = Node("t", RuleKT.t)
        val currentRule = lexer.curToken.rule
        when (currentRule) {
            RuleKT.NUMBER -> {
                val g = g()
                res.children.add(g)
                val y = y(g.`val`)
                res.children.add(y)
                res.`val` = y.`val`
            }
            RuleKT.MINUS -> {
                val g = g()
                res.children.add(g)
                val y = y(g.`val`)
                res.children.add(y)
                res.`val` = y.`val`
            }
            RuleKT.NAME -> {
                val g = g()
                res.children.add(g)
                val y = y(g.`val`)
                res.children.add(y)
                res.`val` = y.`val`
            }
            RuleKT.OPEN -> {
                val g = g()
                res.children.add(g)
                val y = y(g.`val`)
                res.children.add(y)
                res.`val` = y.`val`
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun x(acc: Int): Node {
        val res = Node("x", RuleKT.x)
        val currentRule = lexer.curToken.rule
        when (currentRule) {
            RuleKT.PLUS -> {
                val PLUS = Node(lexer.curToken.text, RuleKT.PLUS)
                res.children.add(PLUS)
                consume(RuleKT.PLUS)
                val t = t()
                res.children.add(t)
                val x = x(acc + t.`val`)
                res.children.add(x)
                res.`val` = x.`val`
            }
            RuleKT.MINUS -> {
                val MINUS = Node(lexer.curToken.text, RuleKT.MINUS)
                res.children.add(MINUS)
                consume(RuleKT.MINUS)
                val t = t()
                res.children.add(t)
                val x = x(acc - t.`val`)
                res.children.add(x)
                res.`val` = x.`val`
            }
            RuleKT.SEMICOLON -> res.`val` = acc
            RuleKT.CLOSE -> res.`val` = acc
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun y(acc: Int): Node {
        val res = Node("y", RuleKT.y)
        when (val currentRule = lexer.curToken.rule) {
            RuleKT.MULTIPLY -> {
                val MULTIPLY = Node(lexer.curToken.text, RuleKT.MULTIPLY)
                res.children.add(MULTIPLY)
                consume(RuleKT.MULTIPLY)
                val g = g()
                res.children.add(g)
                val y = y(acc * g.`val`)
                res.children.add(y)
                res.`val` = y.`val`
            }
            RuleKT.DIVIDE -> {
                val DIVIDE = Node(lexer.curToken.text, RuleKT.DIVIDE)
                res.children.add(DIVIDE)
                consume(RuleKT.DIVIDE)
                val g = g()
                res.children.add(g)
                val y = y(acc / g.`val`)
                res.children.add(y)
                res.`val` = y.`val`
            }
            RuleKT.PLUS -> res.`val` = acc
            RuleKT.MINUS -> res.`val` = acc
            RuleKT.SEMICOLON -> res.`val` = acc
            RuleKT.CLOSE -> res.`val` = acc
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun begin(): Node {
        val res = Node("begin", RuleKT.begin)
        when (val currentRule = lexer.curToken.rule) {
            RuleKT.NAME -> {
                val NAME = Node(lexer.curToken.text, RuleKT.NAME)
                res.children.add(NAME)
                consume(RuleKT.NAME)
                val EQUALS = Node(lexer.curToken.text, RuleKT.EQUALS)
                res.children.add(EQUALS)
                consume(RuleKT.EQUALS)
                val start = start()
                res.children.add(start)
                map[NAME.text] = start.`val`
                println(NAME.text + " = " + start.`val` + ";")
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    private fun consume(expected: RuleKT) {
        val actual = lexer.curToken.rule
        if (expected != actual) {
            throw ParseException("Illegal token " + actual.name + ", expected " + expected.name)
        }
        lexer.nextToken()
    }

    init {
        lexer.nextToken()
    }
}