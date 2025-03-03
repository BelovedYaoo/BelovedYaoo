package top.belovedyaoo.openiam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClientSecret 校验：指定请求中必须包含有效的 client_id 和 client_secret 信息<br>
 * 可标注在方法、类上（效果等同于标注在此类的所有方法上）
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CheckClientIdSecret {

}
