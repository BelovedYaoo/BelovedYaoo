package top.belovedyaoo.opencore.advice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对一个操作的标记
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationMark {

    String name() default "";

    String code() default "";

    String desc() default "";

}
