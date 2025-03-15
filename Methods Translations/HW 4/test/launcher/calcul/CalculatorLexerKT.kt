package launcher.calcul

import java.util.regex.Pattern

class CalculatorLexerKT(private val input: String) {
    private val length: Int
    var curToken: Token
    private var word: StringBuilder
    private var index: Int

    class Token(val rule: RuleKT, val text: String)
    private class TokenRule constructor(val rule: RuleKT, val pattern: Pattern)
    class LexicalException(message: String?) : RuntimeException(message)

    private val allPatterns =
        Pattern.compile("(\\d+(\\.\\d+)?)|([\\n\\r\\t ]+)|(\\+)|(-)|(\\*\\*)|(\\*)|(/)|(\\()|(\\))|(=)|([a-zA-Z]+)|(;)")
    private val tokenRules = listOf(
        TokenRule(RuleKT.NUMBER, Pattern.compile("\\d+(\\.\\d+)?")),
        TokenRule(RuleKT.WHITESPACES, Pattern.compile("[\\n\\r\\t ]+")),
        TokenRule(RuleKT.PLUS, Pattern.compile("\\+")),
        TokenRule(RuleKT.MINUS, Pattern.compile("-")),
        TokenRule(RuleKT.POW, Pattern.compile("\\*\\*")),
        TokenRule(RuleKT.MULTIPLY, Pattern.compile("\\*")),
        TokenRule(RuleKT.DIVIDE, Pattern.compile("/")),
        TokenRule(RuleKT.OPEN, Pattern.compile("\\(")),
        TokenRule(RuleKT.CLOSE, Pattern.compile("\\)")),
        TokenRule(RuleKT.EQUALS, Pattern.compile("=")),
        TokenRule(RuleKT.NAME, Pattern.compile("[a-zA-Z]+")),
        TokenRule(RuleKT.SEMICOLON, Pattern.compile(";"))
    )
    private val ignore = setOf(
        RuleKT.WHITESPACES
    )

    private fun find(s: String): RuleKT {
        var result = RuleKT.DUMMY
        for (r in tokenRules) {
            if (r.pattern.matcher(s).matches()) {
                result = r.rule
                break
            }
        }
        return result
    }

    fun nextToken() {
        word = StringBuilder()
        if (index == length) {
            curToken = Token(RuleKT.EOF, "")
            return
        }
        var cur = input[index]
        index++
        word.append(cur)
        var s = word.toString()
        var t = find(s)
        var m = allPatterns.matcher(s)
        if (m.matches()) {
            while (index < length) {
                cur = input[index]
                index++
                word.append(cur)
                s = word.toString()
                t = find(s)
                m = allPatterns.matcher(s)
                if (!m.matches()) {
                    index--
                    s = s.substring(0, s.length - 1)
                    t = find(s)
                    if (ignore.contains(t)) {
                        nextToken()
                        return
                    }
                    break
                }
            }
            curToken = Token(t, s)
        } else {
            throw LexicalException("Unexpected token $word")
        }
    }

    init {
        word = StringBuilder()
        curToken = Token(RuleKT.DUMMY, "")
        index = 0
        length = input.length
    }
}