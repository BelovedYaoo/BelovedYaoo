package top.belovedyaoo.weaver;

import com.mybatisflex.core.util.FieldWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import top.belovedyaoo.opencore.eo.EntityOperate;
import top.belovedyaoo.opencore.tree.Tree;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static top.belovedyaoo.opencore.tree.Tree.TreeNode.isTree;

/**
 * 字段包装器切面类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Aspect
public class FieldWrapperAspect {

    /**
     * Tree Getter映射
     */
    private static final Map<String, Method> GETTERS = new HashMap<>();

    /**
     * Tree Setter映射
     */
    private static final Map<String, Method> SETTERS = new HashMap<>();

    static {
        // Tree 映射初始化
        try {
            GETTERS.put("parentId", Tree.class.getMethod("parentId"));
            GETTERS.put("treePath", Tree.class.getMethod("treePath"));
            GETTERS.put("isLeaf", Tree.class.getMethod("isLeaf"));
            SETTERS.put("parentId", Tree.class.getMethod("parentId", String.class));
            SETTERS.put("treePath", Tree.class.getMethod("treePath", String.class));
            SETTERS.put("isLeaf", Tree.class.getMethod("isLeaf", boolean.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static FieldWrapperAspect aspectOf() {
        return new FieldWrapperAspect();
    }

    @Around("execution(* com.mybatisflex.core.util.FieldWrapper.of(..))")
    public Object of(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> clazz = (Class<?>) joinPoint.getArgs()[0];
        String fieldName = (String) joinPoint.getArgs()[1];
        if (isTree(clazz) && GETTERS.containsKey(fieldName)) {
            FieldWrapper fieldWrapper = new FieldWrapper();
            Field getterMethodField = FieldWrapper.class.getDeclaredField("getterMethod");
            getterMethodField.setAccessible(true);
            Field setterMethodField = FieldWrapper.class.getDeclaredField("setterMethod");
            setterMethodField.setAccessible(true);
            Field fieldTypeField = FieldWrapper.class.getDeclaredField("fieldType");
            fieldTypeField.setAccessible(true);

            getterMethodField.set(fieldWrapper, GETTERS.get(fieldName));
            setterMethodField.set(fieldWrapper, SETTERS.get(fieldName));

            System.out.println(joinPoint.getArgs()[1]);
            switch (fieldName) {
                case "isLeaf" -> {
                    fieldTypeField.set(fieldWrapper, boolean.class);
                }
                default -> {
                    fieldTypeField.set(fieldWrapper, String.class);
                }
            }

            return fieldWrapper;
        } else {
            // 执行原方法
            FieldWrapper fieldWrapper = (FieldWrapper) joinPoint.proceed();
            // 介入getter和setter查找逻辑
            modifyGetterSetter(fieldWrapper, clazz);
            return fieldWrapper;
        }
    }

    private void modifyGetterSetter(FieldWrapper fieldWrapper, Class<?> clazz) {
        try {
            // 获取私有字段
            Field getterMethodField = FieldWrapper.class.getDeclaredField("getterMethod");
            getterMethodField.setAccessible(true);
            Field setterMethodField = FieldWrapper.class.getDeclaredField("setterMethod");
            setterMethodField.setAccessible(true);
            Field originalField = fieldWrapper.getField();

            // 如果原 Getter 为 null，则查找新的 Getter 方法
            if (getterMethodField.get(fieldWrapper) == null) {
                getterMethodField.set(fieldWrapper, EntityOperate.getGetterOrSetterByField(clazz, originalField, true));
            }
            // 如果原 Setter 为 null，则查找新的 Setter 方法
            if (setterMethodField.get(fieldWrapper) == null) {
                setterMethodField.set(fieldWrapper, EntityOperate.getGetterOrSetterByField(clazz, originalField, false));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}