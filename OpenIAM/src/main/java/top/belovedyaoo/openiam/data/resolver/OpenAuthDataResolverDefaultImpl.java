package top.belovedyaoo.openiam.data.resolver;

import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.httpauth.basic.SaHttpBasicUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.dev33.satoken.util.SaResult;
import top.belovedyaoo.openiam.consts.OpenAuthConst.Param;
import top.belovedyaoo.openiam.consts.OpenAuthConst.TokenType;
import top.belovedyaoo.openiam.core.OpenAuthManager;
import top.belovedyaoo.openiam.data.model.AccessTokenModel;
import top.belovedyaoo.openiam.data.model.ClientTokenModel;
import top.belovedyaoo.openiam.data.model.request.ClientIdAndSecretModel;
import top.belovedyaoo.openiam.data.model.request.RequestAuthModel;
import top.belovedyaoo.openiam.enums.OpenAuthExceptionEnum;
import top.belovedyaoo.opencore.advice.exception.OpenException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Sa-Token OAuth2 数据解析器，负责 Web 交互层面的数据进出：
 * <p>1、从请求中按照指定格式读取数据</p>
 * <p>2、构建数据输出格式</p>
 *
 * @author BelovedYaoo
 * @version 1.1
 */
public class OpenAuthDataResolverDefaultImpl implements OpenAuthDataResolver {

    /**
     * 数据读取：从请求对象中读取 ClientId、Secret，如果获取不到则抛出异常
     *
     * @param request /
     *
     * @return /
     */
    @Override
    public ClientIdAndSecretModel readClientIdAndSecret(SaRequest request) {
        // 优先从请求参数中获取
        String clientId = request.getParam(Param.client_id);
        String clientSecret = request.getParam(Param.client_secret);
        if (SaFoxUtil.isNotEmpty(clientId)) {
            return new ClientIdAndSecretModel(clientId, clientSecret);
        }

        // 如果请求参数中没有提供 client_id 参数，则尝试从 Authorization 中获取
        String authorizationValue = SaHttpBasicUtil.getAuthorizationValue();
        if (SaFoxUtil.isNotEmpty(authorizationValue)) {
            String[] arr = authorizationValue.split(":");
            clientId = arr[0];
            if (arr.length > 1) {
                clientSecret = arr[1];
            }
            return new ClientIdAndSecretModel(clientId, clientSecret);
        }

        // 如果都没有提供，则抛出异常
        throw new OpenException(OpenAuthExceptionEnum.MISSING_PARAMETER).description("请提供 Client 信息");
    }

    /**
     * 数据读取：从请求对象中读取 AccessToken，获取不到返回 null
     */
    @Override
    public String readAccessToken(SaRequest request) {
        return readTokenFromRequest(request, Param.access_token);
    }

    /**
     * 数据读取：从请求对象中读取 ClientToken，获取不到返回 null
     */
    @Override
    public String readClientToken(SaRequest request) {
        return readTokenFromRequest(request, Param.client_token);
    }

    /**
     * 通用方法：从请求对象中读取指定参数或 Authorization 头中的令牌
     *
     * @param request   请求对象
     * @param paramName 参数名（access_token 或 client_token）
     *
     * @return 令牌字符串，获取不到返回 null
     */
    private String readTokenFromRequest(SaRequest request, String paramName) {
        // 优先从请求参数中获取
        String token = request.getParam(paramName);
        if (SaFoxUtil.isNotEmpty(token)) {
            return token;
        }
        // 如果请求参数中没有提供 token 参数，则尝试从 Authorization 中获取
        String authorizationValue = request.getHeader(Param.Authorization);
        if (SaFoxUtil.isEmpty(authorizationValue)) {
            return null;
        }
        // 判断前缀，裁剪
        String prefix = TokenType.Bearer + " ";
        if (authorizationValue.startsWith(prefix)) {
            return authorizationValue.substring(prefix.length());
        }
        // 前缀不符合，返回 null
        return null;
    }

    /**
     * 数据读取：从请求对象中构建 RequestAuthModel
     */
    @Override
    public RequestAuthModel readRequestAuthModel(SaRequest req, Object loginId) {
        RequestAuthModel ra = new RequestAuthModel();
        ra.clientId = req.getParamNotNull(Param.client_id);
        ra.responseType = req.getParamNotNull(Param.response_type);
        ra.redirectUri = req.getParamNotNull(Param.redirect_uri);
        ra.state = req.getParam(Param.state);
        ra.nonce = req.getParam(Param.nonce);
        ra.scopes = OpenAuthManager.getDataConverter().convertScopeStringToList(req.getParam(Param.scope));
        ra.loginId = loginId;
        return ra;
    }


    /**
     * 构建返回值: 获取 token
     */
    @Override
    public Map<String, Object> buildAccessTokenReturnValue(AccessTokenModel at) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("token_type", at.tokenType);
        map.put("access_token", at.accessToken);
        map.put("refresh_token", at.refreshToken);
        map.put("expires_in", at.getExpiresIn());
        map.put("refresh_expires_in", at.getRefreshExpiresIn());
        map.put("client_id", at.clientId);
        map.put("scope", OpenAuthManager.getDataConverter().convertScopeListToString(at.scopes));
        map.putAll(at.extraData);
        SaResult result = SaResult.ok().setMap(map);
        if (OpenAuthManager.getServerConfig().hideStatusField) {
            result.removeDefaultFields();
        }
        return result;
    }

    /**
     * 构建返回值: 凭证式 模式认证 获取 token
     */
    @Override
    public Map<String, Object> buildClientTokenReturnValue(ClientTokenModel ct) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("token_type", ct.tokenType);
        map.put("client_token", ct.clientToken);
        if (OpenAuthManager.getServerConfig().mode4ReturnAccessToken) {
            map.put("access_token", ct.clientToken);
        }
        map.put("expires_in", ct.getExpiresIn());
        map.put("client_id", ct.clientId);
        map.put("scope", OpenAuthManager.getDataConverter().convertScopeListToString(ct.scopes));
        map.putAll(ct.extraData);

        SaResult result = SaResult.ok().setMap(map);
        if (OpenAuthManager.getServerConfig().hideStatusField) {
            result.removeDefaultFields();
        }
        return result;
    }

}

