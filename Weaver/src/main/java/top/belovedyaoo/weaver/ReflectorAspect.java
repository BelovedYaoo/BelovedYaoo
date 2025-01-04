package top.belovedyaoo.weaver;

import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.invoker.MethodInvoker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import top.belovedyaoo.opencore.tree.Tree;

import java.util.HashMap;
import java.util.Map;

/**
 * 反射切面类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Aspect
public class ReflectorAspect {

    /**
     * Tree Getter映射
     */
    private static final Map<String, Invoker> GETTERS = new HashMap<>();

    /**
     * Tree Setter映射
     */
    private static final Map<String, Invoker> SETTERS = new HashMap<>();

    static {
        // Tree 映射初始化
        try {
            GETTERS.put("parentId", new MethodInvoker(Tree.class.getMethod("parentId")));
            GETTERS.put("treePath", new MethodInvoker(Tree.class.getMethod("treePath")));
            GETTERS.put("isRoot", new MethodInvoker(Tree.class.getMethod("isRoot")));
            GETTERS.put("isLeaf", new MethodInvoker(Tree.class.getMethod("isLeaf")));
            SETTERS.put("parentId", new MethodInvoker(Tree.class.getMethod("parentId", String.class)));
            SETTERS.put("treePath", new MethodInvoker(Tree.class.getMethod("treePath", String.class)));
            SETTERS.put("isRoot", new MethodInvoker(Tree.class.getMethod("isRoot", boolean.class)));
            SETTERS.put("isLeaf", new MethodInvoker(Tree.class.getMethod("isLeaf", boolean.class)));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Around("execution(* org.apache.ibatis.reflection.Reflector.getGetInvoker(..))")
    public Object getGetInvoker(ProceedingJoinPoint joinPoint) throws Throwable {
        // 如果字段命中，则返回映射，否则执行原方法
        if (GETTERS.containsKey((String) joinPoint.getArgs()[0])) {
            return GETTERS.get((String) joinPoint.getArgs()[0]);
        } else {
            return joinPoint.proceed();
        }
    }

    @Around("execution(* org.apache.ibatis.reflection.Reflector.getSetInvoker(..))")
    public Object getSetInvoker(ProceedingJoinPoint joinPoint) throws Throwable {
        // 如果字段命中，则返回映射，否则执行原方法
        if (SETTERS.containsKey((String) joinPoint.getArgs()[0])) {
            return SETTERS.get((String) joinPoint.getArgs()[0]);
        } else {
            return joinPoint.proceed();
        }
    }

}
