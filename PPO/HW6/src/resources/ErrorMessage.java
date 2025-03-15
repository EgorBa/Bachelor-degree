package resources;

public class ErrorMessage {
    public final static String LEFT_BRACE_ERROR = "Left brace without right. Default palace for right brace is tne end of the expression.";
    public final static String RIGHT_BRACE_ERROR = "Right brace without left. Default palace for left brace is tne start of the expression.";
    public final static String EMPTY_LIST = "Empty list of tokens. You get 0 as a result.";
    public final static String LONE_OPERAND = "Incorrect expression. Some operands without operators. You get 0 as a result.";
    public final static String UNEXPECTED_BRACE = "Unexpected brace.";
    public final static String NO_SECOND_OPERAND = "No the second operand for operation : %s. You get 0 as a result.";
    public final static String NO_OPERANDS = "No operands for operation : %s. You get 0 as a result.";
    public final static String UNKNOWN_TOKEN= "Unknown token %c at position : %d%n";
}
