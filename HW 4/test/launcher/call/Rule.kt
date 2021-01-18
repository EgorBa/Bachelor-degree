package launcher.call

enum class Rule(val isTerminal: Boolean) {
    OPEN(true),
    s(false),
    t(false),
    EPSILON(true),
    MULTIPLY(true),
    x(false),
    y(false),
    PLUS(true),
    SEMICOLON(true),
    all(false),
    WHITESPACES(true),
    MINUS(true),
    NAME(true),
    f(false),
    DIVIDE(true),
    b(false),
    begin(false),
    j(false),
    NUMBER(true),
    EQUALS(true),
    g(false),
    start(false),
    CLOSE(true),
    EOF(true),
    DUMMY(true);
}