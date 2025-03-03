package top.belovedyaoo.openiam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Client-Token 校验：指定请求中必须包含有效的 client_token ，并且包含指定的 scope<br>
 * 可标注在方法、类上（效果等同于标注在此类的所有方法上）
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CheckClientToken {

    /**
     * 需要校验的 scope [ 数组 ]
     *
     * @return /
     */
    String[] scope() default {};

}
