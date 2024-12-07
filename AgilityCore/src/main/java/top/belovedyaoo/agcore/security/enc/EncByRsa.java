package top.belovedyaoo.agcore.security.enc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密方法参数<br>
 * 使用非对称加密 RSA 算法
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EncByRsa {

    /**
     * 是否解密参数
     */
    boolean decParams() default true;

    /**
     * 是否加密结果
     */
    boolean encResult() default true;

}
