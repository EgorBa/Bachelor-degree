package generator

import grammerParser.GrammerParserLexer
import grammerParser.GrammerParserParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

const val EMPTY = ""
const val TAB = "\t"
const val MY_PARSER = "myParser"
const val EPSILON = "EPSILON"

data class Grammar(
    val name: String,
    val header: String,
    val parserRules: List<Rule>,
    val fields: Set<String>,
    val globals: Set<String>
)

fun makeGrammar(filename: String): Grammar {
    val inputFile = File(filename).readText()
    val lexer = GrammerParserLexer(CharStreams.fromString(inputFile))
    val parser = GrammerParserParser(CommonTokenStream(lexer))
    val grammarParser = parser.grammarParser()
    val grammar = Grammar(
        grammarParser.parserName().NAME().text,
        grammarParser.header().CODE().toString().substring(1, grammarParser.header().CODE().text.length - 1).trim()
            .toLowerCase(),
        getParserRules(grammarParser.parserRules()),
        getFields(grammarParser.fields()),
        getGlobals(grammarParser.globals())
    )
    grammar.check()
    return grammar
}

private fun getParserRules(
    rulesContent: GrammerParserParser.ContentRuleContext,
    listOfRules: MutableList<Rule>,
    name: String,
    argument: String
) {
    if (rulesContent.OR() != null) {
        getParserRules(rulesContent.contentRule(0), listOfRules, name, argument)
        getParserRules(rulesContent.contentRule(1), listOfRules, name, argument)
        return
    }
    var code = rulesContent.CODE()?.toString() ?: EMPTY
    code = if (code.isNotEmpty()) code.substring(1, code.length - 1).trim() else EMPTY
    val count = rulesContent.childCount + if (code == EMPTY) 0 else -1
    val children = mutableListOf<Pair<String, String>>()
    for (i in 0 until count) {
        val ruleName = rulesContent.name(i)
        var funcArgument = EMPTY
        for (j in 0 until 2) {
            funcArgument += if (ruleName.FUNC_ARGUMENT(j) != null) ruleName.FUNC_ARGUMENT(j).text else EMPTY
            if (funcArgument != EMPTY) {
                funcArgument = funcArgument.substring(0, funcArgument.length - 1).trim()
            }
        }
        val contentName = if (ruleName.NAME() != null) ruleName.NAME().text else ruleName.TOKEN().text
        if (funcArgument != EMPTY) {
            children.add(contentName to funcArgument)
        } else {
            children.add(contentName to funcArgument)
        }
    }
    listOfRules.add(NotTerminal(name, code, argument, children))

}

private fun getParserRules(rulesContext: GrammerParserParser.ParserRulesContext): List<Rule> {
    val parserRules = mutableListOf<Rule>()
    val count = rulesContext.childCount
    for (i in 0 until count) {
        val principle = rulesContext.principle(i)
        val parserRule = principle.grammerRule()
        val token = principle.token()
        if (parserRule != null) {
            val name = parserRule.NAME().text
            val names = parserRule.contentRule()
            val argument = parserRule.constructor()
            val arg = argument?.toStr() ?: EMPTY
            getParserRules(names, parserRules, name, arg)
        } else if (token != null) {
            val name = token.TOKEN().text
            val regex = token.REGEX().text
            val skip = token.SKIP_RULE() != null
            parserRules.add(Terminal(name, EMPTY, regex.substring(1, regex.length - 1), skip))
        }
    }
    parserRules.add(Terminal(EPSILON, EMPTY, EMPTY, false))
    return parserRules
}

private fun getFields(fields: GrammerParserParser.FieldsContext?): Set<String> {
    if (fields == null) {
        return emptySet()
    }
    val result = mutableSetOf<String>()
    for (i in 0 until fields.childCount - 3) {
        result.add(fields.field(i).toStr())
    }
    return result
}

private fun getGlobals(globals: GrammerParserParser.GlobalsContext?): Set<String> {
    if (globals == null) {
        return emptySet()
    }
    val result = mutableSetOf<String>()
    for (i in 0 until globals.childCount - 3) {
        result.add(globals.global(i).toStr())
    }
    return result
}

private fun GrammerParserParser.ConstructorContext.toStr(): String {
    return "${NAME(0).text} : ${NAME(1).text}, ${NAME(2).text} : ${NAME(3).text}"
}

private fun GrammerParserParser.FieldContext.toStr(): String {
    return "${NAME(0).text} = ${NAME(1).text}"
}

private fun GrammerParserParser.GlobalContext.toStr(): String {
    return "${NAME().text} = ${TYPE().text}"
}

private fun Grammar.check() {
    val rules = parserRules.map { it.name }.toHashSet()
    for (rule in this.parserRules) {
        if (rule is NotTerminal) {
            val children = rule.children
            for (c in children) {
                if (c.first !in rules) {
                    throw IllegalStateException("Not this rule : $c ")
                }
            }
        }
    }
}

open class Rule(open val name: String, open val code: String)

data class Terminal(
    override val name: String,
    override val code: String,
    val pattern: String,
    val isSkip: Boolean
) :
    Rule(name, code)

data class NotTerminal(
    override val name: String,
    override val code: String,
    val argument: String,
    val children: List<Pair<String, String>>
) :
    Rule(name, code)