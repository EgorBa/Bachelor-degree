import launcher.calculator3.CalculatorLexer
import launcher.calculator3.CalculatorParser

fun main() {
    val input = "a = ((4 - 2) * (1 - 2 + 3 + 4)) / (6 - 1); b = ((4 - 2) * (1 - 2 + 3 + 4)) / (6 - 1);" + "a=b+4;"
    val input1 = "2**--3"
    val lexer = CalculatorLexer(input)
    val parser = CalculatorParser(lexer)
    val n = parser.all()
    //println(n.`val`)
    println(((4 - 2) * (1 - 2 + 3 + 4)) / (6 - 1))
}