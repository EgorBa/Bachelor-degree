package calculatorwithmemory
import java.util.regex.Pattern

class CalculatorWithMemoryLexer (private val input: String) {
    private val length: Int
    var curToken: Token
    private var word: StringBuilder
    private var index: Int

    class Token(val token: CalculatorWithMemoryToken, val text: String)
    
    class LexicalException(message: String?) : RuntimeException(message)

    private val patterns = listOf(
            Pair(CalculatorWithMemoryToken.NUMBER, Pattern.compile("\\d+")),
			Pair(CalculatorWithMemoryToken.WHITESPACES, Pattern.compile("[\\n\\r\\t ]+")),
			Pair(CalculatorWithMemoryToken.PLUS, Pattern.compile("\\+")),
			Pair(CalculatorWithMemoryToken.MINUS, Pattern.compile("-")),
			Pair(CalculatorWithMemoryToken.MULTIPLY, Pattern.compile("\\*")),
			Pair(CalculatorWithMemoryToken.DIVIDE, Pattern.compile("/")),
			Pair(CalculatorWithMemoryToken.OPEN, Pattern.compile("\\(")),
			Pair(CalculatorWithMemoryToken.CLOSE, Pattern.compile("\\)")),
			Pair(CalculatorWithMemoryToken.EQUALS, Pattern.compile("=")),
			Pair(CalculatorWithMemoryToken.NAME, Pattern.compile("[a-zA-Z]+")),
			Pair(CalculatorWithMemoryToken.SEMICOLON, Pattern.compile(";"))
    )
    
    private val skipSet = setOf(
            CalculatorWithMemoryToken.WHITESPACES
    )
    
    private fun findToken(s: String): CalculatorWithMemoryToken {
        var result = CalculatorWithMemoryToken.DUMMY
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
            curToken = Token(CalculatorWithMemoryToken.EOF, "")
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
        if (token != CalculatorWithMemoryToken.DUMMY) {
            while (index < length) {
                cur = input[index]
                index++
                word.append(cur)
                text = word.toString()
                token = findToken(text)
                if (token == CalculatorWithMemoryToken.DUMMY) {
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
        curToken = Token(CalculatorWithMemoryToken.DUMMY, "")
        index = 0
        length = input.length
    }
}