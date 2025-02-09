package top.belovedyaoo.openauth.function.strategy;

import top.belovedyaoo.openauth.data.model.AccessTokenModel;

import java.util.function.Consumer;

/**
 * 函数式接口：AccessTokenModel 加工
 *
 * <p>  参数：AccessTokenModel </p>
 * <p>  返回：无  </p>
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@FunctionalInterface
public interface ScopeWorkAccessTokenFunction extends Consumer<AccessTokenModel> {

}