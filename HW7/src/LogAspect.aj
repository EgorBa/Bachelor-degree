import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;

@Aspect
public class LogAspect {

    private static final HashMap<String, LinkedList<Long>> map = new HashMap<>();

    @Pointcut("execution(* dao.*.*(..))")
    public void methodDaoExecuting() {
    }

    @Pointcut("execution(* model.*.*(..))")
    public void methodModelExecuting() {
    }

    @Pointcut("methodDaoExecuting() || methodModelExecuting()")
    public void methodExecuting() {
    }

    @Before(value = "methodExecuting()")
    public void beforeDaoExecution(JoinPoint joinPoint) {
        String key = getKey(joinPoint);
        if (key == null) return;
        map.putIfAbsent(key, new LinkedList<>());
        map.get(key).add(System.nanoTime());
    }

    @AfterReturning(value = "methodExecuting()")
    public void recordSuccessfulDaoExecution(JoinPoint joinPoint) {
        String key = getKey(joinPoint);
        if (key == null) return;
        long time = System.nanoTime() - map.get(key).getLast();
        map.get(key).removeLast();
        map.get(key).addLast(time);
    }

    private String getKey(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String fullName = joinPoint.getSourceLocation().getWithinType().getName();
        if (!fullName.contains(Main.packages)) return null;
        return String.format("%s.%s", fullName, methodName);
    }

    public static String getStatistics() {
        StringBuilder str = new StringBuilder(String.format("===STATISTICS FOR PACKAGES : %s===\n", Main.packages.toUpperCase()));
        for (String method : map.keySet()) {
            str.append("Method : ").append(method).append('\n');
            str.append("Count calls : ").append(map.get(method).size()).append('\n');
            str.append("Sum time work, ms : ")
                    .append(map.get(method).stream().mapToLong(e -> e).sum() / 1000000d).append('\n');
            str.append("Avg time work, ms : ")
                    .append(map.get(method).stream().mapToLong(e -> e).average().getAsDouble() / 1000000d).append('\n');
            str.append("=============================\n");
        }
        return str.toString();
    }

}