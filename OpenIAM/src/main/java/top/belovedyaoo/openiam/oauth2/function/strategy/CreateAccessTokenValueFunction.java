package top.belovedyaoo.openiam.oauth2.function.strategy;

import java.util.List;

/**
 * 函数式接口：创建一个 AccessToken value
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@FunctionalInterface
public interface CreateAccessTokenValueFunction {

    /**
     * 创建一个 AccessToken value
     * @param clientId 应用id
     * @param loginId 账号id
     * @param scopes 权限
     * @return AccessToken value
     */
    String execute(String clientId, Object loginId, List<String> scopes);

}