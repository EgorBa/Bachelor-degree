package calculator

fun main() {
    test(" (56 + 1) * 1i  ")
    test(" (56 + 1) * 1i + 2i +3 ")
    test("((49 * (- 1)) * (1 * 2 + 3 + 4)) / (6 - 1) +56  ")
    test("((4 + 1) * (1 / 2 + 3 + 4)) / (6 - 1)  - (- 4 + 56 )    ")
}

private fun test(input: String) {
    val lexer = CalculatorLexer(input)
    val parser = CalculatorParser(lexer)
    parser.start()
    println("---------------------------")
}