package top.belovedyaoo.openiam.handler;

import cn.dev33.satoken.context.model.SaRequest;
import top.belovedyaoo.openiam.core.OpenAuthManager;
import top.belovedyaoo.openiam.consts.OpenAuthConst;
import top.belovedyaoo.openiam.consts.OpenAuthGrantType;
import top.belovedyaoo.openiam.data.model.AccessTokenModel;
import top.belovedyaoo.openiam.data.model.request.ClientIdAndSecretModel;

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