package launcher.calculator3;
public enum Rule {
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
    POW(true),
    NAME(true),
    f(false),
    DIVIDE(true),
    b(false),
    begin(false),
    j(false),
    NUMBER(true),
    EQUALS(true),
    k(false),
    g(false),
    h(false),
    start(false),
    CLOSE(true),
    EOF(true),
    DUMMY(true);
                    
    private final boolean isTerminal;
    Rule(final boolean isTerminal) {
        this.isTerminal = isTerminal;
    }
    public boolean isTerminal() {
        return isTerminal;
    }
}