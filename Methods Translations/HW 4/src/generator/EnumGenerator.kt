package generator

import java.io.File

class EnumGenerator(private val grammar: Grammar) {
    private val path = "${MY_PARSER}/${grammar.name.toLowerCase()}"

    private fun generateEnum() {
        if (!File(path).exists()) {
            File(path).mkdirs()
        }
        val tokens = grammar.parserRules.map { it.name }.toHashSet().joinToString(",\n$TAB")
        File("${path}/${grammar.name}Token.kt").writeText(
            """
${grammar.header}

enum class ${grammar.name}Token {
    $tokens,
    EOF,
    DUMMY;
}
            """.trimIndent()
        )
    }

    fun generate() {
        generateEnum()
    }
}