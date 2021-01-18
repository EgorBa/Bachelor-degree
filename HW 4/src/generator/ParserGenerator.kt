package generator

import java.io.File

class ParserGenerator(private val grammar: Grammar) {
    private val path = "${MY_PARSER}/${grammar.name.toLowerCase()}"
    private val first = mutableMapOf<String, MutableSet<String>>()
    private val firstRight = mutableMapOf<List<Pair<String, String>>, MutableSet<String>>()
    private val follow = mutableMapOf<String, MutableSet<String>>()
    private val tokenName = "${grammar.name}Token"

    private fun generateFirst() {
        for (rule in grammar.parserRules) {
            first[rule.name] = mutableSetOf()
            when (rule) {
                is Terminal -> firstRight[listOf(rule.name to EMPTY)] = mutableSetOf()
                is NotTerminal -> firstRight[rule.children] = mutableSetOf()
            }
        }
        var flag = true
        while (flag) {
            flag = false
            for (rule in grammar.parserRules) {
                val cur = first[rule.name]!!.toMutableSet()
                when (rule) {
                    is NotTerminal -> {
                        first[rule.name]!!.addAll(first[rule.children[0].first]!!)
                        firstRight[rule.children]!!.addAll(first[rule.children[0].first]!!)
                        var i = 0
                        while (EPSILON in first[rule.children[i].first]!!) {
                            i++
                            if (i == rule.children.size) {
                                break
                            }
                            first[rule.name]!!.addAll(first[rule.children[i].first]!!)
                            firstRight[rule.children]!!.addAll(first[rule.children[i].first]!!)
                        }
                    }
                    is Terminal -> {
                        first[rule.name]!!.add(rule.name)
                        firstRight[listOf(rule.name to EMPTY)]!!.add(rule.name)
                    }
                }
                if (first[rule.name]!! != cur) {
                    flag = true
                }
            }
        }
    }

    private fun generateFollow() {
        val rules = grammar.parserRules
        val nonTerminals = rules.filterIsInstance<NotTerminal>()
        val nonTerminalNames = nonTerminals.map { it.name }.toHashSet()
        for (rule in rules) {
            follow[rule.name] = mutableSetOf()
        }
        follow[rules[0].name]!!.add("EOF")
        var flag = true
        while (flag) {
            flag = false
            for (nonTerm in nonTerminals) {
                for (i in nonTerm.children.indices) {
                    val nonTermName = nonTerm.children[i].first
                    if (nonTermName in nonTerminalNames) {
                        val s = if (i == nonTerm.children.size - 1) {
                            EPSILON
                        } else {
                            nonTerm.children[i + 1].first
                        }
                        val cur = follow[nonTermName]!!.toMutableSet()
                        follow[nonTermName]!!.addAll(first[s]!!.minus(EPSILON))
                        if (EPSILON in first[s]!!) {
                            follow[nonTermName]!!.addAll(follow[nonTerm.name]!!)
                        }
                        if (follow[nonTermName]!! != cur) {
                            flag = true
                        }
                    }
                }
            }
        }
    }

    private fun generateFunction(name: String): String {
        val follows = if (EPSILON in first[name]!!) {
            follow[name]!!
        } else {
            emptySet()
        }
        val terminalRules = HashMap<String, Rule>()
        val nonTerminals = grammar.parserRules.filterIsInstance<NotTerminal>()
        val curRule = nonTerminals.find { it.name == name }!!
        val curRules = grammar.parserRules.filter { it.name == name }.toMutableList()
        val defaultCode =
            curRules.find { it is NotTerminal && it.children.map { p -> p.first }.contains(EPSILON) }?.code ?: EMPTY
        for (rule in curRules) {
            when (rule) {
                is Terminal -> terminalRules[rule.name] = rule
                is NotTerminal -> firstRight[rule.children]!!.filter { it != EPSILON }
                    .forEach { terminalRules[it] = rule }
            }
        }
        val nonTerminalNames = nonTerminals.map { it.name }
        return """
    fun $name (${curRule.argument}): Node {
        val res = Node("$name", $tokenName.$name)
        when (val currentToken = lexer.curToken.token) {
            ${
            terminalRules.entries.joinToString("\n${TAB.repeat(3)}") {
                """
            $tokenName.${it.key} -> {
                ${
                    (it.value as NotTerminal).children.joinToString("\n${TAB.repeat(4)}") { pair ->

                        when (pair.first) {
                            !in nonTerminalNames -> {
                                """
                val ${pair.first} = Node(lexer.curToken.text, $tokenName.${pair.first})
                res.children.add(${pair.first})
                next($tokenName.${pair.first})
                        """.trim()
                            }
                            else -> {
                                """
                val ${pair.first} = ${pair.first}(${
                                    if (pair.second == EMPTY) EMPTY else
                                        pair.second.split("(")[1] + "," + pair.second.split("(")[2]
                                })
                res.children.add(${pair.first})
                        """.trim()
                            }
                        }
                    }
                }
                ${it.value.code}
            }
                """.trim()
            }
        }
            ${
            follows.joinToString("\n${TAB.repeat(3)}") {
                """
            ${grammar.name}Token.$it -> {$defaultCode}
                """.trim()
            }
        }
        else -> throw ParseException("Unknown token : " + currentToken.name)
        }
        return res
    }
        """.trim()
    }

    private fun generateParser() {
        val parserName = "${grammar.name}Parser"
        val lexerName = "${grammar.name}Lexer"
        val notTerminalsSet = grammar.parserRules.filterIsInstance<NotTerminal>().map { it.name }.toHashSet()
        File("${path}/${parserName}.kt").writeText(
            """
${grammar.header}
import java.util.ArrayList
import java.util.HashMap

class $parserName (private val lexer: $lexerName) {

    ${grammar.globals.joinToString("\n${TAB.repeat(2)}") { "private val $it" }}
    
    init {
        lexer.nextToken()
    }
    
    class Node(val text: String, val token: ${grammar.name}Token) {
        ${grammar.fields.joinToString("\n${TAB.repeat(2)}") { "var $it" }}
        val children: MutableList<Node> = ArrayList()
    }

    class ParseException(message: String?) : RuntimeException(message)
    
    ${notTerminalsSet.joinToString("\n\n${TAB}") { generateFunction(it) }}
    
    private fun next(expected: $tokenName) {
        val cur = lexer.curToken.token
        if (cur != expected) {
            throw ParseException("Unknown token : " + cur.name + ", expected token : " + expected.name)
        }
        lexer.nextToken()
    }
}
            """.trim()
        )
    }

    fun generate() {
        generateFirst()
        generateFollow()
        generateParser()
    }
}