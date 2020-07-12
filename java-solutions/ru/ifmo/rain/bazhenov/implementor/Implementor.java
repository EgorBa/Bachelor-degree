package ru.ifmo.rain.bazhenov.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class implementing {@link Impler}. Provides public methods to generate abstract class or
 * interface basic implementation.
 *
 * @author Egor Bazhenov
 */
public class Implementor implements Impler {
    /**
     * Tokens to construct implementation source code.
     */
    private static int id = 0;
    private static final String NAME_PREFIX = "arg";
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String COMMA = ", ";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SEMICOLON = ";";
    private static final String BRACKET_OPEN = "(";
    private static final String BRACKET_CLOSE = ")";
    private static final String BRACES_OPEN = "{";
    private static final String BRACES_CLOSE = "}";
    private static final String PACKAGE = "package";
    private static final String CLASS = "class";
    private static final String IMPLEMENTS = "implements";
    private static final String THROWS = "throws";
    private static final String RETURN = "return";
    private static final String PUBLIC = "public";
    private static final String NULL = "null";
    private static final String FALSE = "false";
    private static final String ZERO = "0";
    private static final String IMPL_SUFFIX = "Impl";
    private static final String CODE_SUFFIX = "Impl.java";

    /**
     * Main function to provide console interface of the program.
     * <p>
     * Allowed signature: <code>token outputPath</code>
     * The program runs in Implementation mode and {@link #implement(Class, Path)} is invoked.
     * All arguments must not be null. Any errors and warnings are printed to <code>STDOUT</code> and
     * <code>STDERR</code>.
     *
     * @param args Provided to program arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Exception: Wrong number of arguments");
            return;
        }
        Class<?> token;
        Path path;
        try {
            token = Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            System.err.println("Exception: Class not found");
            return;
        } catch (NullPointerException e) {
            System.err.println("Exception: Class not be null");
            return;
        }
        try {
            path = Paths.get(args[1]);
        } catch (InvalidPathException e) {
            System.err.println("Exception: Invalid path");
            return;
        } catch (NullPointerException e) {
            System.err.println("Exception: Path not be null");
            return;
        }
        try {
            new Implementor().implement(token, path);
        } catch (ImplerException e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Produces file implementing interface specified by provided <code>token</code>.
     * <p>
     * Generated class classes name should be same as classes name of the type token with <tt>Impl</tt> suffix added.
     *
     * @param token type token to create implementation for.
     * @param path root directory.
     * @throws ImplerException when implementation cannot be generated.
     */
    @Override
    public void implement(Class<?> token, Path path) throws ImplerException {
        int modifiers = token.getModifiers();
        if (token.isPrimitive() || token.isArray() || token.isEnum() || Modifier.isFinal(modifiers) || Modifier.isPrivate(modifiers)) {
            throw new ImplerException("Unsupported type");
        }
        Path sourcePath = createPath(token, path);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(sourcePath)) {
            bufferedWriter.write(generateCode(token));
        } catch (IOException e) {
            throw new ImplerException("IO Exception: ", e);
        }
    }

    /**
     * Creates {@link Path} of implementation source code and create missing parent directories.
     *
     * @param token {@link Class} which implementation is required
     * @param root Root {@link Path} for implementation files
     * @return {@link Path} where implementation must be created
     * @throws ImplerException in case generated path is invalid
     */
    private static Path createPath(Class<?> token, Path root) throws ImplerException {
        String sourcePath = String.join(File.separator, token.getPackageName().split("\\.")) +
                File.separator +
                token.getSimpleName() +
                CODE_SUFFIX;
        Path path;
        try {
            path = Paths.get(root.toString(), sourcePath);
        } catch (InvalidPathException e) {
            throw new ImplerException("Exception: Invalid path generated", e);
        }
        Path parentPath = path.getParent();
        if (parentPath != null) {
            try {
                Files.createDirectories(parentPath);
            } catch (IOException e) {
                throw new ImplerException("Exception: Failed to create directory", e);
            }
        }
        return path;
    }

    /**
     * Generated complete source code of given {@link Class}.
     *
     * @param token {@link Class} which implementation is required
     * @return {@link String} containing complete generated source code     *
     * @see #generatePackage(Class)
     * @see #generateFirstLine(Class)
     * @see #generateMethods(Class)
     */
    static String generateCode(Class<?> token) {
        return String.join(LINE_SEPARATOR,
                generatePackage(token),
                generateFirstLine(token),
                String.join(EMPTY, generateMethods(token)),
                BRACES_CLOSE);
    }

    /**
     * Generates {@link String} declaring implementation class package.
     *
     * @param token {@link Class} which implementation is required
     * @return Package declaration {@link String} if token is in package or empty {@link String} otherwise
     */
    private static String generatePackage(Class<?> token) {
        Package pkg = token.getPackage();
        return (pkg != null) ? String.join(SPACE, PACKAGE, pkg.getName(), SEMICOLON) : "";
    }

    /**
     * Generates class opening line. Includes modifiers, name and super class.
     *
     * @param token {@link Class} which implementation is required
     * @return Implementation class opening line
     */
    private static String generateFirstLine(Class<?> token) {
        return String.join(SPACE,
                PUBLIC,
                CLASS,
                token.getSimpleName() + IMPL_SUFFIX,
                IMPLEMENTS,
                token.getCanonicalName(),
                BRACES_OPEN);
    }

    /**
     * Generates {@link List<String>} of implementations of each {@link Method} ones.
     *
     * @param token {@link Class} which implementation is required
     * @return {@link List<String>} of {@link Method}s implementations.
     * @see #generateMethod(Method)
     */
    private static List<String> generateMethods(Class<?> token) {
        return Arrays
                .stream(token.getMethods())
                .filter(method -> Modifier.isAbstract(method.getModifiers()))
                .map(Implementor::generateMethod)
                .collect(Collectors.toList());
    }

    /**
     * Generates {@link Method} code.
     *
     * @param method {@link Method} to generate code of
     * @return Method implementation code as {@link String}
     * @see #generateFistLineMethod(Method)
     * @see #generateMethodBody(Method)
     */
    private static String generateMethod(Method method) {
        return String.join(LINE_SEPARATOR,
                generateFistLineMethod(method),
                generateMethodBody(method),
                BRACES_CLOSE,
                LINE_SEPARATOR);
    }

    /**
     * Generates method opening line. Includes modifiers, return code if it is
     * an instance of {@link Method}, name, generated args and possible exceptions.
     *
     * @param method {@link Method} which opening line is required
     * @return Opening {@link String} of requested {@link Method}
     * @see #getArguments(Method)
     * @see #getThrowingExceptions(Method)
     */
    private static String generateFistLineMethod(Method method) {
        String methodName = method.getName();
        String returnType = method.getReturnType().getCanonicalName();
        return String.join(SPACE,
                Modifier.toString(method.getModifiers() & ~Modifier.TRANSIENT & ~Modifier.ABSTRACT),
                returnType,
                methodName,
                BRACKET_OPEN,
                getArguments(method),
                BRACKET_CLOSE,
                getThrowingExceptions(method),
                BRACES_OPEN);
    }

    /**
     * Generates {@link Method} body. All methods return the default value of an appropriate type.
     *
     * @param method {@link Method} to generate body of
     * @return Method implementation body as {@link String}
     * @see #getDefaultValue(Class)
     */
    private static String generateMethodBody(Method method) {
        return String.join(SPACE,
                RETURN,
                getDefaultValue(method.getReturnType()),
                SEMICOLON);
    }

    /**
     * Generates source code of default value.
     *
     * @param cls {@link Class} to find default value of
     * @return {@link String} source code of default value
     */
    private static String getDefaultValue(Class<?> cls) {
        if (!cls.isPrimitive()) {
            return NULL;
        } else if (cls.equals(void.class)) {
            return EMPTY;
        } else if (cls.equals(boolean.class)) {
            return FALSE;
        } else {
            return ZERO;
        }
    }

    /**
     * Generates {@link String} declaring and enumerating {@link Method} arguments
     * separated by comma.
     *
     * @param method {@link Method} which arguments are required
     * @return {@link String} of arguments list
     */
    private static String getArguments(Method method) {
        return Arrays
                .stream(method.getParameterTypes())
                .map(arg -> String.join(SPACE, arg.getCanonicalName(), NAME_PREFIX + id++))
                .collect(Collectors.joining(COMMA));
    }

    /**
     * Generates {@link String} enumerating {@link Method} exceptions.
     *
     * @param method {@link Method} which exceptions are required
     * @return {@link String} exceptions list
     */
    private static String getThrowingExceptions(Method method) {
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        if (exceptionTypes.length == 0) return "";
        return String.join(SPACE,
                THROWS,
                Arrays.stream(exceptionTypes).map(Class::getCanonicalName).collect(Collectors.joining(COMMA)));
    }
}