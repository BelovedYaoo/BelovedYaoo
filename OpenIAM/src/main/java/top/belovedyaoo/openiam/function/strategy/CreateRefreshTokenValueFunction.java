package top.belovedyaoo.openiam.function.strategy;

import java.util.List;

/**
 * 函数式接口：创建一个 RefreshToken value
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@FunctionalInterface
public interface CreateRefreshTokenValueFunction {

    /**
     * 创建一个 RefreshToken value
     * @param clientId 应用id
     * @param loginId 账号id
     * @param scopes 权限
     * @return RefreshToken value
     */
    String execute(String clientId, Object loginId, List<String> scopes);

}