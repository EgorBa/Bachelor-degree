package launcher.call
import java.util.regex.Pattern

class Calculator3Lexer (private val input: String) {
    private val length: Int
    var curToken: Token
    private var word: StringBuilder
    private var index: Int

    class Token(val rule: Rule, val text: String)
    
    private class TokenRule constructor(val rule: Rule, val pattern: Pattern)
    
    class LexicalException(message: String?) : RuntimeException(message)
    
    private val allPatterns = Pattern.compile("(\\d+(\\.\\d+)?)|([\\n\\r\\t ]+)|(\\+)|(-)|(\\*)|(/)|(\\()|(\\))|(=)|([a-zA-Z]+)|(;)")
    
    private val tokenRules = listOf(
            TokenRule(Rule.NUMBER, Pattern.compile("\\d+(\\.\\d+)?")),
            TokenRule(Rule.WHITESPACES, Pattern.compile("[\\n\\r\\t ]+")),
            TokenRule(Rule.PLUS, Pattern.compile("\\+")),
            TokenRule(Rule.MINUS, Pattern.compile("-")),
            TokenRule(Rule.MULTIPLY, Pattern.compile("\\*")),
            TokenRule(Rule.DIVIDE, Pattern.compile("/")),
            TokenRule(Rule.OPEN, Pattern.compile("\\(")),
            TokenRule(Rule.CLOSE, Pattern.compile("\\)")),
            TokenRule(Rule.EQUALS, Pattern.compile("=")),
            TokenRule(Rule.NAME, Pattern.compile("[a-zA-Z]+")),
            TokenRule(Rule.SEMICOLON, Pattern.compile(";"))
    )
    
    private val ignore = setOf(
            Rule.WHITESPACES
    )
    
    private fun find(s: String): Rule {
        var result = Rule.DUMMY
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
            curToken = Token(Rule.EOF, "")
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
        curToken = Token(Rule.DUMMY, "")
        index = 0
        length = input.length
    }
}