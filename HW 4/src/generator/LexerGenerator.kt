package generator

import java.io.File

class LexerGenerator(private val grammar: Grammar) {
    private val path = "${MY_PARSER}/${grammar.name.toLowerCase()}"

    private fun generateLexer(tokens: List<Terminal>) {
        val tokenName = "${grammar.name}Token"
        fun escape(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
        File("${path}/${grammar.name}Lexer.kt").writeText(
            """
${grammar.header}
import java.util.regex.Pattern

class ${grammar.name}Lexer (private val input: String) {
    private val length: Int
    var curToken: Token
    private var word: StringBuilder
    private var index: Int

    class Token(val token: $tokenName, val text: String)
    
    class LexicalException(message: String?) : RuntimeException(message)

    private val patterns = listOf(
            ${
                tokens.joinToString(",\n${TAB.repeat(3)}") {
                    "Pair(${grammar.name}Token.${it.name}, Pattern.compile(\"${
                        escape(
                            it.pattern
                        )
                    }\"))"
                }
            }
    )
    
    private val skipSet = setOf(
            ${tokens.filter { it.isSkip }.joinToString(",\n${TAB.repeat(3)}") { "${grammar.name}Token.${it.name}" }}
    )
    
    private fun findToken(s: String): ${grammar.name}Token {
        var result = $tokenName.DUMMY
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
            curToken = Token($tokenName.EOF, "")
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
        if (token != $tokenName.DUMMY) {
            while (index < length) {
                cur = input[index]
                index++
                word.append(cur)
                text = word.toString()
                token = findToken(text)
                if (token == $tokenName.DUMMY) {
                    text = text.substring(0, text.length - 1)
                    token = findToken(text)
                    index--
                    break
                }
            }
            curToken = Token(token, text)
        } else {
            throw LexicalException("Unexpected token ${'$'}word")
        }
    }

    init {
        word = StringBuilder()
        curToken = Token($tokenName.DUMMY, "")
        index = 0
        length = input.length
    }
}
            """.trimIndent()
        )
    }

    fun generate() {
        generateLexer(grammar.parserRules.filterIsInstance<Terminal>().filter { it.name != EPSILON })
    }
}