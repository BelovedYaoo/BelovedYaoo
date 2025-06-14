package top.belovedyaoo.logs.annotation;

import top.belovedyaoo.logs.enums.BusinessType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口日志注解
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceLog {

    /**
     * 是否持久化
     */
    boolean persistence() default true;

    /**
     * 是否打印在控制台
     */
    boolean print() default false;

    /**
     * 接口业务类型
     */
    BusinessType[] businessType() default {BusinessType.OTHER};

    /**
     * 接口唯一标识符<p>
     * 每一个接口都建议有一串随机的唯一ID<p>
     * 即使接口重构、重命名,也不会导致日志无法关联
     */
    String identifierCode() default "";

    /**
     * 接口名称
     */
    String interfaceName();

    /**
     * 接口描述
     */
    String interfaceDesc() default "";

}
