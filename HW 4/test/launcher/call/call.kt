import launcher.call.Calculator3Lexer
import launcher.call.Calculator3Parser

fun main() {
    val input = "a = ((4 - 2) * (1 - 2 + 3 + 4)) / (6 - 1); b = ((4 - 2) * (1 - 2 + 3 + 4)) / (6 - 1);" + "a=b+4;"
    val input1 = "2**--3"
    val lexer = Calculator3Lexer(input)
    val parser = Calculator3Parser(lexer)
    val n = parser.all()
    //println(n.`val`)
    println(((4 - 2) * (1 - 2 + 3 + 4)) / (6 - 1))
}