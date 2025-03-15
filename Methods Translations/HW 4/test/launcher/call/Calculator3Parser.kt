package launcher.call

import java.util.ArrayList
import java.util.HashMap

class Calculator3Parser(private val lexer: Calculator3Lexer) {

    private val map = HashMap<String, Int>();

    init {
        lexer.nextToken()
    }

    class Node(val text: String, val rule: Rule) {
        var value = 0;
        val children: MutableList<Node> = ArrayList()
    }

    class ParseException(message: String?) : RuntimeException(message)

    fun all(): Node {
        val res = Node("all", Rule.all)
        when (val currentRule = lexer.curToken.rule) {
            Rule.NAME -> {
                val begin = begin()
                res.children.add(begin)
                val SEMICOLON = Node(lexer.curToken.text, Rule.SEMICOLON)
                res.children.add(SEMICOLON)
                consume(Rule.SEMICOLON)
                val b = b()
                res.children.add(b)

            }

            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun b(): Node {
        val res = Node("b", Rule.b)
        when (val currentRule = lexer.curToken.rule) {
            Rule.NAME -> {
                val all = all()
                res.children.add(all)

            }
            Rule.EOF -> {
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun f(): Node {
        val res = Node("f", Rule.f)
        when (val currentRule = lexer.curToken.rule) {
            Rule.NUMBER -> {
                val j = j()
                res.children.add(j)
                res.value = j.value;
            }
            Rule.MINUS -> {
                val MINUS = Node(lexer.curToken.text, Rule.MINUS)
                res.children.add(MINUS)
                consume(Rule.MINUS)
                val j = j()
                res.children.add(j)
                res.value = -j.value;
            }
            Rule.NAME -> {
                val j = j()
                res.children.add(j)
                res.value = j.value;
            }
            Rule.OPEN -> {
                val j = j()
                res.children.add(j)
                res.value = j.value;
            }

            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun g(): Node {
        val res = Node("g", Rule.g)
        when (val currentRule = lexer.curToken.rule) {
            Rule.NUMBER -> {
                val f = f()
                res.children.add(f)
                res.value = f.value;
            }
            Rule.MINUS -> {
                val f = f()
                res.children.add(f)
                res.value = f.value;
            }
            Rule.NAME -> {
                val f = f()
                res.children.add(f)
                res.value = f.value;
            }
            Rule.OPEN -> {
                val f = f()
                res.children.add(f)
                res.value = f.value;
            }

            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun start(): Node {
        val res = Node("start", Rule.start)
        when (val currentRule = lexer.curToken.rule) {
            Rule.NUMBER -> {
                val s = s()
                res.children.add(s)
                res.value = s.value;
            }
            Rule.MINUS -> {
                val s = s()
                res.children.add(s)
                res.value = s.value;
            }
            Rule.NAME -> {
                val s = s()
                res.children.add(s)
                res.value = s.value;
            }
            Rule.OPEN -> {
                val s = s()
                res.children.add(s)
                res.value = s.value;
            }

            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun j(): Node {
        val res = Node("j", Rule.j)
        when (val currentRule = lexer.curToken.rule) {
            Rule.NUMBER -> {
                val NUMBER = Node(lexer.curToken.text, Rule.NUMBER)
                res.children.add(NUMBER)
                consume(Rule.NUMBER)
                res.value = Integer.parseInt(NUMBER.text);
            }
            Rule.NAME -> {
                val NAME = Node(lexer.curToken.text, Rule.NAME)
                res.children.add(NAME)
                consume(Rule.NAME)
                res.value = map[NAME.text]!!;
            }
            Rule.OPEN -> {
                val OPEN = Node(lexer.curToken.text, Rule.OPEN)
                res.children.add(OPEN)
                consume(Rule.OPEN)
                val start = start()
                res.children.add(start)
                val CLOSE = Node(lexer.curToken.text, Rule.CLOSE)
                res.children.add(CLOSE)
                consume(Rule.CLOSE)
                res.value = start.value;
            }

            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun s(): Node {
        val res = Node("s", Rule.s)
        when (val currentRule = lexer.curToken.rule) {
            Rule.NUMBER -> {
                val t = t()
                res.children.add(t)
                val x = x(t.value)
                res.children.add(x)
                res.value = x.value;
            }
            Rule.MINUS -> {
                val t = t()
                res.children.add(t)
                val x = x(t.value)
                res.children.add(x)
                res.value = x.value;
            }
            Rule.NAME -> {
                val t = t()
                res.children.add(t)
                val x = x(t.value)
                res.children.add(x)
                res.value = x.value;
            }
            Rule.OPEN -> {
                val t = t()
                res.children.add(t)
                val x = x(t.value)
                res.children.add(x)
                res.value = x.value;
            }

            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun t(): Node {
        val res = Node("t", Rule.t)
        when (val currentRule = lexer.curToken.rule) {
            Rule.NUMBER -> {
                val g = g()
                res.children.add(g)
                val y = y(g.value)
                res.children.add(y)
                res.value = y.value;
            }
            Rule.MINUS -> {
                val g = g()
                res.children.add(g)
                val y = y(g.value)
                res.children.add(y)
                res.value = y.value;
            }
            Rule.NAME -> {
                val g = g()
                res.children.add(g)
                val y = y(g.value)
                res.children.add(y)
                res.value = y.value;
            }
            Rule.OPEN -> {
                val g = g()
                res.children.add(g)
                val y = y(g.value)
                res.children.add(y)
                res.value = y.value;
            }

            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun x(acc: Int): Node {
        val res = Node("x", Rule.x)
        when (val currentRule = lexer.curToken.rule) {
            Rule.PLUS -> {
                val PLUS = Node(lexer.curToken.text, Rule.PLUS)
                res.children.add(PLUS)
                consume(Rule.PLUS)
                val t = t()
                res.children.add(t)
                val x = x(acc + t.value)
                res.children.add(x)
                res.value = x.value;
            }
            Rule.MINUS -> {
                val MINUS = Node(lexer.curToken.text, Rule.MINUS)
                res.children.add(MINUS)
                consume(Rule.MINUS)
                val t = t()
                res.children.add(t)
                val x = x(acc - t.value)
                res.children.add(x)
                res.value = x.value;
            }
            Rule.SEMICOLON -> {
                res.value = acc;
            }
            Rule.CLOSE -> {
                res.value = acc;
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun y(acc: Int): Node {
        val res = Node("y", Rule.y)
        when (val currentRule = lexer.curToken.rule) {
            Rule.MULTIPLY -> {
                val MULTIPLY = Node(lexer.curToken.text, Rule.MULTIPLY)
                res.children.add(MULTIPLY)
                consume(Rule.MULTIPLY)
                val g = g()
                res.children.add(g)
                val y = y(acc * g.value)
                res.children.add(y)
                res.value = y.value;
            }
            Rule.DIVIDE -> {
                val DIVIDE = Node(lexer.curToken.text, Rule.DIVIDE)
                res.children.add(DIVIDE)
                consume(Rule.DIVIDE)
                val g = g()
                res.children.add(g)
                val y = y(acc / g.value)
                res.children.add(y)
                res.value = y.value;
            }
            Rule.PLUS -> {
                res.value = acc;
            }
            Rule.MINUS -> {
                res.value = acc;
            }
            Rule.SEMICOLON -> {
                res.value = acc;
            }
            Rule.CLOSE -> {
                res.value = acc;
            }
            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    fun begin(): Node {
        val res = Node("begin", Rule.begin)
        when (val currentRule = lexer.curToken.rule) {
            Rule.NAME -> {
                val NAME = Node(lexer.curToken.text, Rule.NAME)
                res.children.add(NAME)
                consume(Rule.NAME)
                val EQUALS = Node(lexer.curToken.text, Rule.EQUALS)
                res.children.add(EQUALS)
                consume(Rule.EQUALS)
                val start = start()
                res.children.add(start)
                map.put(NAME.text, start.value); System.out.println(NAME.text + " = " + start.value + ";");
            }

            else -> throw ParseException("Illegal token " + currentRule.name)
        }
        return res
    }

    private fun consume(expected: Rule) {
        val actual = lexer.curToken.rule
        if (expected != actual) {
            throw ParseException("Illegal token " + actual.name + ", expected " + expected.name)
        }
        lexer.nextToken()
    }
}