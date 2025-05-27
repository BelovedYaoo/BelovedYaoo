package top.belovedyaoo.openiam.service;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.belovedyaoo.openac.model.Domain;
import top.belovedyaoo.openac.model.User;
import top.belovedyaoo.openac.model.mapping.MappingDomainUserRole;
import top.belovedyaoo.openiam.data.loader.OpenAuthDataLoader;
import top.belovedyaoo.openiam.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openiam.model.AuthApp;
import top.belovedyaoo.openiam.enums.OpenAuthResultEnum;
import top.belovedyaoo.openiam.function.ConfirmFunction;
import top.belovedyaoo.openiam.function.DoLoginFunction;
import top.belovedyaoo.openiam.function.NotLoginFunction;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.openiam.generateMapper.AuthAppMapper;

import static com.mybatisflex.core.query.QueryMethods.count;

/**
 * 自定义数据加载器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class OpenAuthDataLoaderImpl implements OpenAuthDataLoader {

    private final AuthAppMapper authAppMapper;

    private final BaseMapper<MappingDomainUserRole> mappingDomainUserRoleMapper;

    private final AuthenticationService authenticationService;

    /**
     * 根据 clientId 获取 Client 信息
     *
     * @param clientId 应用id
     *
     * @return Client信息
     */
    @Override
    public OpenAuthClientModel getClientModel(String clientId) {
        AuthApp authorizedApplication = authAppMapper
                .selectOneByQuery(
                        new QueryWrapper()
                                .where("client_id = '" + clientId + "'"));
        if (authorizedApplication == null) {
            return null;
        }
        return new OpenAuthClientModel()
                .setClientId(authorizedApplication.clientId())
                .setClientSecret(authorizedApplication.clientSecret())
                .setContractScopes(authorizedApplication.contractScopes())
                .setAllowRedirectUris(authorizedApplication.allowRedirectUris())
                .setAllowGrantTypes(authorizedApplication.allowGrantTypes());
        // .setIsNewRefresh(clientModel.getBoolean("isNewRefresh"))
        // .setAccessTokenTimeout(clientModel.getLong("accessTokenTimeout"))
        // .setRefreshTokenTimeout(clientModel.getLong("refreshTokenTimeout"))
        // .setClientTokenTimeout(clientModel.getLong("clientTokenTimeout"))
        // .setLowerClientTokenTimeout(clientModel.getLong("lowerClientTokenTimeout"));
    }

    /**
     * 根据 clientId 和 loginId 获取 openid
     *
     * @param clientId 应用id
     * @param loginId  账号id
     *
     * @return openid
     */
    @Override
    public String getOpenid(String clientId, Object loginId) {
        // 此处使用框架默认算法生成 openid
        return OpenAuthDataLoader.super.getOpenid(clientId, loginId);
    }

    public NotLoginFunction notLogin() {
        return () -> Result.failed().resultType(OpenAuthResultEnum.NEED_LOGIN);
    }

    public DoLoginFunction doLogin() {
        return (username, password) -> {
            User user = (User) authenticationService.getUser(username, password).data();
            // Sa-Token登录
            StpUtil.login(user.baseId());
            // 获取用户在 OpenIAM 具有的角色ID
            // 获取用户在 OpenIAM 具有的权限ID
            return Result.success()
                    .data("user",user)
                    .data("tokenValue", StpUtil.getTokenValue());
        };
    }

    public ConfirmFunction confirm() {
        return (clientId, scopes) -> Result.success().resultType(OpenAuthResultEnum.NEED_CONFIRM).data("client_id", clientId).data("scope", scopes);
    }

    /**
     * 判断此账号是否可以访问此Client
     *
     * @param userId   用户ID
     * @param clientId 应用ID
     *
     * @return 是否可以访问此Client
     */
    @Override
    public boolean canAccess(String userId, String clientId) {
        String authAppAlias = "a";
        String mappingAlias = "m";
        QueryWrapper joinQuery = new QueryWrapper()
                .select(count().as("match"))
                .from(AuthApp.class).as(authAppAlias)
                .join(MappingDomainUserRole.class).as(mappingAlias)
                .on(new QueryColumn(authAppAlias, Domain.DOMAIN_ID).eq(new QueryColumn(mappingAlias, Domain.DOMAIN_ID)))
                .where(new QueryColumn(authAppAlias, AuthApp.CLIENT_ID).eq(clientId))
                .and(new QueryColumn(mappingAlias, User.USER_ID).eq(userId));
        /*
         * SELECT COUNT(*) AS `match`
         * FROM `auth_app` AS `a`
         * JOIN `mapping_domain_user_role` AS `m`
         * ON (`a`.`domain_id` = `m`.`domain_id`) AND `m`.`deleted_at` IS NULL
         * WHERE (`a`.`base_id` = 'f09f0ed00ff949d7ba6148e236396585'
         * AND `m`.`user_id` = '0b2d398202aa79c77798d0108ce8fc5d') AND `a`.`deleted_at` IS NULL
         */
        long match = mappingDomainUserRoleMapper.selectCountByQuery(joinQuery);
        return match > 0;
    }
}
