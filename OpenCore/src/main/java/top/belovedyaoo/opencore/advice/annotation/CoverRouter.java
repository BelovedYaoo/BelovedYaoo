package top.belovedyaoo.opencore.advice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记需要覆盖父类路由的Controller<p>
 * 当Controller被此注解标记时，其父类中的路由将被覆盖
 *
 * @author Celrx
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CoverRouter {
} 