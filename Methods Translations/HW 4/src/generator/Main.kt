package generator

fun main() {
    //generate("src/generator/calculatorWithMemory.gr")
    generate("src/generator/calculator.gr")
}

private fun generate(path: String) {
    val grammar = makeGrammar(path)
    EnumGenerator(grammar).generate()
    LexerGenerator(grammar).generate()
    ParserGenerator(grammar).generate()
}