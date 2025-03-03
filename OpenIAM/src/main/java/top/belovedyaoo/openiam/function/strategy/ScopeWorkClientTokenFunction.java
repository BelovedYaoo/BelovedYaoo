package top.belovedyaoo.openiam.function.strategy;

import top.belovedyaoo.openiam.data.model.ClientTokenModel;

import java.util.function.Consumer;

/**
 * 函数式接口：ClientTokenModel 加工
 *
 * <p>  参数：ClientTokenModel </p>
 * <p>  返回：无  </p>
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@FunctionalInterface
public interface ScopeWorkClientTokenFunction extends Consumer<ClientTokenModel> {

}