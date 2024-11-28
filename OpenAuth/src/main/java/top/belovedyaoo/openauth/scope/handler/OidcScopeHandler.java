package top.belovedyaoo.openauth.scope.handler;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.jwt.SaJwtUtil;
import cn.dev33.satoken.jwt.error.SaJwtErrorCode;
import cn.dev33.satoken.jwt.exception.SaJwtException;
import top.belovedyaoo.openauth.core.OpenAuthManager;
import top.belovedyaoo.openauth.consts.OpenAuthConst;
import top.belovedyaoo.openauth.data.model.AccessTokenModel;
import top.belovedyaoo.openauth.data.model.ClientTokenModel;
import top.belovedyaoo.openauth.data.model.oidc.IdTokenModel;
import top.belovedyaoo.openauth.data.model.request.ClientIdAndSecretModel;
import top.belovedyaoo.openauth.exception.OpenAuthException;
import top.belovedyaoo.openauth.scope.CommonScope;
import cn.dev33.satoken.util.SaFoxUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * id_token 权限处理器：在 AccessToken 扩展参数中追加 id_token 字段
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class OidcScopeHandler implements OpenAuthScopeHandlerInterface {

    @Override
    public String getHandlerScope() {
        return CommonScope.OIDC;
    }

    @Override
    public void workAccessToken(AccessTokenModel at) {
        SaRequest req = SaHolder.getRequest();
        ClientIdAndSecretModel client = OpenAuthManager.getDataResolver().readClientIdAndSecret(req);

        // 基础参数
        IdTokenModel idToken = new IdTokenModel();
        idToken.iss = getIss();
        idToken.sub = at.loginId;
        idToken.aud = client.clientId;
        idToken.iat = System.currentTimeMillis() / 1000;
        idToken.exp = idToken.iat + OpenAuthManager.getServerConfig().getOidc().getIdTokenTimeout();
        idToken.authTime = OpenAuthManager.getStpLogic().getSessionByLoginId(at.loginId).getCreateTime() / 1000;
        idToken.nonce = getNonce();
        idToken.acr = null;
        idToken.amr = null;
        idToken.azp = client.clientId;

        // 额外参数
        idToken.extraData = new LinkedHashMap<>();
        idToken = workExtraData(idToken);

        // 构建 jwtIdToken
        String jwtIdToken = generateJwtIdToken(idToken);

        // 放入 AccessTokenModel
        at.extraData.put("id_token", jwtIdToken);
    }

    @Override
    public void workClientToken(ClientTokenModel ct) {

    }

    /**
     * 获取 iss
     * @return /
     */
    public String getIss() {
        // 如果开发者配置了 iss，则使用开发者配置的 iss
        String cfgIss = OpenAuthManager.getServerConfig().getOidc().getIss();
        if(SaFoxUtil.isNotEmpty(cfgIss)) {
            return cfgIss;
        }
        // 否则根据请求的 url 计算 iss
        //      例如请求 url 为： http://localhost:8081/abc/xyz?name=张三
        //      则计算的 iss 为： http://localhost:8081
        String urlString = SaHolder.getRequest().getUrl();
        try {
            URL url = new URL(urlString);
            String iss = url.getProtocol() + "://" + url.getHost();
            if(url.getPort() != -1) {
                iss += ":" + url.getPort();
            }
            return iss;
        } catch (MalformedURLException e) {
            throw new OpenAuthException(e);
        }
    }

    /**
     * 获取 nonce
     * @return /
     */
    public String getNonce() {
        String nonce = SaHolder.getRequest().getParam(OpenAuthConst.Param.nonce);
        if(SaFoxUtil.isEmpty(nonce)) {
            //通过code查找nonce
            //为了避免其它handler可能会用到nonce,任由其自然过期，只取用不删除
            nonce = OpenAuthManager.getDao().getNonce(SaHolder.getRequest().getParam(OpenAuthConst.Param.code));
        }
        if(SaFoxUtil.isEmpty(nonce)) {
            nonce = SaFoxUtil.getRandomString(32);
        }
        SaManager.getSaSignTemplate().checkNonce(nonce);
        return nonce;
    }

    /**
     * 加工 IdTokenModel
     * @return /
     */
    public IdTokenModel workExtraData(IdTokenModel idToken) {
        //
        return idToken;
    }

    /**
     * 将 IdTokenModel 转化为 Map 数据
     * @return /
     */
    public Map<String, Object> convertIdTokenToMap(IdTokenModel idToken) {
        // 基础参数
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("iss", idToken.iss);
        map.put("sub", idToken.sub);
        map.put("aud", idToken.aud);
        map.put("exp", idToken.exp);
        map.put("iat", idToken.iat);
        map.put("auth_time", idToken.authTime);
        map.put("nonce", idToken.nonce);
        map.put("acr", idToken.acr);
        map.put("amr", idToken.amr);
        map.put("azp", idToken.azp);

        // 移除 null 值
        idToken.extraData.entrySet().removeIf(entry -> entry.getValue() == null);

        // 扩展参数
        map.putAll(idToken.extraData);

        // 返回
        return map;
    }

    /**
     * 生成 jwt 格式的 id_token
     * @param idToken /
     * @return /
     */
    public String generateJwtIdToken(IdTokenModel idToken) {
        Map<String, Object> dataMap = convertIdTokenToMap(idToken);
        String keyt = OpenAuthManager.getStpLogic().getConfigOrGlobal().getJwtSecretKey();
        SaJwtException.throwByNull(keyt, "请配置jwt秘钥", SaJwtErrorCode.CODE_30205);
        return SaJwtUtil.createToken(dataMap, keyt);
    }

}