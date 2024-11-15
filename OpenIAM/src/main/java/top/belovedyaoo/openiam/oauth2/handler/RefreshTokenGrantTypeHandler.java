package top.belovedyaoo.openiam.oauth2.handler;

import cn.dev33.satoken.context.model.SaRequest;
import top.belovedyaoo.openiam.oauth2.OpenAuthManager;
import top.belovedyaoo.openiam.oauth2.consts.OpenAuthGrantType;
import top.belovedyaoo.openiam.oauth2.consts.OpenAuthConst;
import top.belovedyaoo.openiam.oauth2.data.model.AccessTokenModel;
import top.belovedyaoo.openiam.oauth2.data.model.RefreshTokenModel;
import top.belovedyaoo.openiam.oauth2.error.OpenAuthErrorCode;
import top.belovedyaoo.openiam.oauth2.exception.OpenAuthClientModelException;
import top.belovedyaoo.openiam.oauth2.exception.OpenAuthRefreshTokenException;

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
        OpenAuthRefreshTokenException.throwBy(rt == null, "无效refresh_token: " + refreshToken, refreshToken, OpenAuthErrorCode.CODE_30111);

        // 校验：Refresh-Token 代表的 ClientId 与提供的 ClientId 是否一致
        OpenAuthClientModelException.throwBy( ! rt.clientId.equals(clientId), "无效client_id: " + clientId, clientId, OpenAuthErrorCode.CODE_30122);

        // 获取新 Access-Token
        return OpenAuthManager.getDataGenerate().refreshAccessToken(refreshToken);
    }

}