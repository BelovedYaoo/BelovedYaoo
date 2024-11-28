package top.belovedyaoo.openauth.handler;

import cn.dev33.satoken.context.model.SaRequest;
import top.belovedyaoo.openauth.core.OpenAuthManager;
import top.belovedyaoo.openauth.consts.OpenAuthConst;
import top.belovedyaoo.openauth.consts.OpenAuthGrantType;
import top.belovedyaoo.openauth.data.model.AccessTokenModel;
import top.belovedyaoo.openauth.data.model.request.ClientIdAndSecretModel;

import java.util.List;

/**
 * authorization_code grant_type 处理器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class AuthorizationCodeGrantTypeHandler implements GrantTypeHandlerInterface {

    @Override
    public String getHandlerGrantType() {
        return OpenAuthGrantType.authorization_code;
    }

    @Override
    public AccessTokenModel getAccessToken(SaRequest req, String clientId, List<String> scopes) {
        // 获取参数
        ClientIdAndSecretModel clientIdAndSecret = OpenAuthManager.getDataResolver().readClientIdAndSecret(req);
        // String clientId = clientIdAndSecret.clientId;
        String clientSecret = clientIdAndSecret.clientSecret;
        String code = req.getParamNotNull(OpenAuthConst.Param.code);
        String redirectUri = req.getParam(OpenAuthConst.Param.redirect_uri);

        // 校验参数
        OpenAuthManager.getTemplate().checkGainTokenParam(code, clientId, clientSecret, redirectUri);

        // 构建 Access-Token、返回
        return OpenAuthManager.getDataGenerate().generateAccessToken(code);
    }

}