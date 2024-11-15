package top.belovedyaoo.openiam.oauth2.controller;

import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.openiam.generateMapper.AuthorizedApplicationMapper;
import top.belovedyaoo.openiam.oauth2.data.loader.OpenAuthDataLoader;
import top.belovedyaoo.openiam.oauth2.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openiam.oauth2.enums.OpenAuthResultEnum;
import top.belovedyaoo.openiam.oauth2.function.ConfirmFunction;
import top.belovedyaoo.openiam.oauth2.function.DoLoginFunction;
import top.belovedyaoo.openiam.oauth2.function.NotLoginFunction;
import top.belovedyaoo.openiam.permission.entity.AuthorizedApplication;
import top.belovedyaoo.openiam.permission.service.impl.AuthenticationServiceImpl;

import static top.belovedyaoo.openiam.permission.entity.table.AuthorizedApplicationTableDef.AUTHORIZED_APPLICATION;

/**
 * Sa-Token OAuth2：自定义数据加载器
 *
 * @author click33
 */
@Component
@RequiredArgsConstructor
public class SaOAuth2DataLoaderImpl implements OpenAuthDataLoader {

    private final AuthenticationServiceImpl authenticationService;

    private final AuthorizedApplicationMapper authorizedApplicationMapper;

    // 根据 clientId 获取 Client 信息
    @Override
    public OpenAuthClientModel getClientModel(String clientId) {
        AuthorizedApplication authorizedApplication = authorizedApplicationMapper
                .selectOneByQuery(
                        new QueryWrapper()
                                .where(AUTHORIZED_APPLICATION.CLIENT_ID.eq(clientId)));
        OpenAuthClientModel clientModel = authorizedApplication
                .convertToClientModel();
        return clientModel;
    }

    // 根据 clientId 和 loginId 获取 openid
    @Override
    public String getOpenid(String clientId, Object loginId) {
        // 此处使用框架默认算法生成 openid，真实环境建议改为从数据库查询
        return OpenAuthDataLoader.super.getOpenid(clientId, loginId);
    }

    public NotLoginFunction notLogin() {
        return () -> Result.failed().resultType(OpenAuthResultEnum.NEED_LOGIN);
    }

    public DoLoginFunction doLogin() {
        return authenticationService::openLogin;
    }

    public ConfirmFunction confirm() {
        return (clientId, scopes) -> Result.success().resultType(OpenAuthResultEnum.NEED_CONFIRM).data("clientId", clientId).data("scope", scopes);
    }

}
