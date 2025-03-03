package top.belovedyaoo.openiam.function.strategy;

import java.util.List;

/**
 * 函数式接口：创建一个 ClientToken value
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@FunctionalInterface
public interface CreateClientTokenValueFunction {

    /**
     * 创建一个 ClientToken value
     * @param clientId 应用id
     * @param scopes 权限
     * @return ClientToken value
     */
    String execute(String clientId, List<String> scopes);

}