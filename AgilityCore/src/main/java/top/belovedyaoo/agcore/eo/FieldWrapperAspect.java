package top.belovedyaoo.agcore.eo;

import com.mybatisflex.core.util.FieldWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Field;

/**
 * 字段包装器切面类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Aspect
public class FieldWrapperAspect {

    @Around("execution(* com.mybatisflex.core.util.FieldWrapper.of(..))")
    public Object interceptOfMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行原方法
        FieldWrapper fieldWrapper = (FieldWrapper) joinPoint.proceed();
        // 介入getter和setter查找逻辑
        modifyGetterSetter(fieldWrapper, (Class<?>) joinPoint.getArgs()[0]);
        return fieldWrapper;
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