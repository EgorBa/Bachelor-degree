package calculatorwithmemory

fun main() {
    test("a = ((56 - 1) * (1 / 2 + 3 + 4)) / (6 - 1)  ; b = a; g   =  a /b    ;  ")
    test("a = ((49 * (- 1)) * (1 * 2 + 3 + 4)) / (6 - 1)  ; b = a+1; g   =  a *b*a+56;  ")
    test("a = ((4 + 1) * (1 / 2 + 3 + 4)) / (6 - 1)  ; b = -a; g   =  a *b*a+56    ;  ")
}

private fun test(input: String) {
    val lexer = CalculatorWithMemoryLexer(input)
    val parser = CalculatorWithMemoryParser(lexer)
    parser.start()
    println("---------------------------")
}