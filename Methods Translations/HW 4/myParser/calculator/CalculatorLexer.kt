package calculator
import java.util.regex.Pattern

class CalculatorLexer (private val input: String) {
    private val length: Int
    var curToken: Token
    private var word: StringBuilder
    private var index: Int

    class Token(val token: CalculatorToken, val text: String)
    
    class LexicalException(message: String?) : RuntimeException(message)

    private val patterns = listOf(
            Pair(CalculatorToken.NUMBER, Pattern.compile("\\d+")),
			Pair(CalculatorToken.COMP, Pattern.compile("\\d+i")),
			Pair(CalculatorToken.WHITESPACES, Pattern.compile("[\\n\\r\\t ]+")),
			Pair(CalculatorToken.PLUS, Pattern.compile("\\+")),
			Pair(CalculatorToken.MINUS, Pattern.compile("-")),
			Pair(CalculatorToken.MULTIPLY, Pattern.compile("\\*")),
			Pair(CalculatorToken.DIVIDE, Pattern.compile("/")),
			Pair(CalculatorToken.OPEN, Pattern.compile("\\(")),
			Pair(CalculatorToken.CLOSE, Pattern.compile("\\)")),
			Pair(CalculatorToken.EQUALS, Pattern.compile("=")),
			Pair(CalculatorToken.NAME, Pattern.compile("[a-zA-Z]+")),
			Pair(CalculatorToken.SEMICOLON, Pattern.compile(";"))
    )
    
    private val skipSet = setOf(
            CalculatorToken.WHITESPACES
    )
    
    private fun findToken(s: String): CalculatorToken {
        var result = CalculatorToken.DUMMY
        for (r in patterns) {
            if (r.second.matcher(s).matches()) {
                result = r.first
                return result
            }
        }
        return result
    }
    
    fun nextToken() {
        word = StringBuilder()
        if (index == length) {
            curToken = Token(CalculatorToken.EOF, "")
            return
        }
        var cur = input[index]
        index++
        word.append(cur)
        var text = word.toString()
        var token = findToken(text)
        if (skipSet.contains(token)){
            nextToken()
            return
        }
        if (token != CalculatorToken.DUMMY) {
            while (index < length) {
                cur = input[index]
                index++
                word.append(cur)
                text = word.toString()
                token = findToken(text)
                if (token == CalculatorToken.DUMMY) {
                    text = text.substring(0, text.length - 1)
                    token = findToken(text)
                    index--
                    break
                }
            }
            curToken = Token(token, text)
        } else {
            throw LexicalException("Unexpected token $word")
        }
    }

    init {
        word = StringBuilder()
        curToken = Token(CalculatorToken.DUMMY, "")
        index = 0
        length = input.length
    }
}