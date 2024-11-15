package top.belovedyaoo.openiam.oauth2.function;

import java.util.function.Supplier;

/**
 * 函数式接口：OAuth-Server端 未登录时的函数
 *
 * <p>  参数：clientId, scope  </p>
 * <p>  返回：未登录状态  </p>
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@FunctionalInterface
public interface NotLoginFunction extends Supplier<Object> {

}