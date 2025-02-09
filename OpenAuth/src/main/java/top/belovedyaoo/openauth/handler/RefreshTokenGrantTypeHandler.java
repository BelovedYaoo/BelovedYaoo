package top.belovedyaoo.openauth.handler;

import cn.dev33.satoken.context.model.SaRequest;
import top.belovedyaoo.openauth.core.OpenAuthManager;
import top.belovedyaoo.openauth.consts.OpenAuthGrantType;
import top.belovedyaoo.openauth.consts.OpenAuthConst;
import top.belovedyaoo.openauth.data.model.AccessTokenModel;
import top.belovedyaoo.openauth.data.model.RefreshTokenModel;
import top.belovedyaoo.openauth.enums.OidcExceptionEnum;
import top.belovedyaoo.opencore.exception.OpenException;

import java.util.List;

/**
 * refresh_token grant_type 处理器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class RefreshTokenGrantTypeHandler implements GrantTypeHandlerInterface {

    @Override
    public String getHandlerGrantType() {
        return OpenAuthGrantType.refresh_token;
    }

    @Override
    public AccessTokenModel getAccessToken(SaRequest req, String clientId, List<String> scopes) {
        // 获取参数
        String refreshToken = req.getParamNotNull(OpenAuthConst.Param.refresh_token);

        // 校验：Refresh-Token 是否存在
        RefreshTokenModel rt = OpenAuthManager.getDao().getRefreshToken(refreshToken);
        OpenException.throwBy(rt == null, OidcExceptionEnum.INVALID_REFRESH_TOKEN, refreshToken);

        // 校验：Refresh-Token 代表的 ClientId 与提供的 ClientId 是否一致
        OpenException.throwBy(!rt.clientId.equals(clientId), OidcExceptionEnum.UNCONFORMITY_CLIENT_ID, clientId);

        // 获取新 Access-Token
        return OpenAuthManager.getDataGenerate().refreshAccessToken(refreshToken);
    }

}