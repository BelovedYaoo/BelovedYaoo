package top.belovedyaoo.openiam.oauth2.processor;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.util.SaResult;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.openiam.oauth2.OpenAuthManager;
import top.belovedyaoo.openiam.oauth2.OpenAuthTemplate;
import top.belovedyaoo.openiam.oauth2.config.ServerConfig;
import top.belovedyaoo.openiam.oauth2.consts.OpenAuthConst;
import top.belovedyaoo.openiam.oauth2.consts.OpenAuthConst.Api;
import top.belovedyaoo.openiam.oauth2.consts.OpenAuthConst.Param;
import top.belovedyaoo.openiam.oauth2.consts.OpenAuthConst.ResponseType;
import top.belovedyaoo.openiam.oauth2.consts.OpenAuthGrantType;
import top.belovedyaoo.openiam.oauth2.data.generate.OpenAuthDataGenerate;
import top.belovedyaoo.openiam.oauth2.data.model.AccessTokenModel;
import top.belovedyaoo.openiam.oauth2.data.model.ClientTokenModel;
import top.belovedyaoo.openiam.oauth2.data.model.CodeModel;
import top.belovedyaoo.openiam.oauth2.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openiam.oauth2.data.model.request.ClientIdAndSecretModel;
import top.belovedyaoo.openiam.oauth2.data.model.request.RequestAuthModel;
import top.belovedyaoo.openiam.oauth2.error.OpenAuthErrorCode;
import top.belovedyaoo.openiam.oauth2.exception.OpenAuthException;
import top.belovedyaoo.openiam.oauth2.strategy.OpenAuthStrategy;

import java.util.List;

/**
 * OpenAuth 请求处理器
 *
 * @author BelovedYaoo
 * @version 1.1
 */
public class OpenAuthServerProcessor {

    /**
     * 全局默认实例
     */
    public static OpenAuthServerProcessor instance = new OpenAuthServerProcessor();

    /**
     * 处理 Server 端请求， 路由分发
     *
     * @return 处理结果
     */
    public Object distribute() {

        // 获取变量
        SaRequest req = SaHolder.getRequest();

        // ------------------ 路由分发 ------------------

        // 模式一：Code授权码 || 模式二：隐藏式
        if (req.isPath(Api.authorize)) {
            return authorize();
        }

        // Code 换 Access-Token || 模式三：密码式
        if (req.isPath(Api.token)) {
            return token();
        }

        // Refresh-Token 刷新 Access-Token
        if (req.isPath(Api.refresh)) {
            return refresh();
        }

        // 回收 Access-Token
        if (req.isPath(Api.revoke)) {
            return revoke();
        }

        // doLogin 登录接口
        if (req.isPath(Api.doLogin)) {
            return doLogin();
        }

        // doConfirm 确认授权接口
        if (req.isPath(Api.doConfirm)) {
            return doConfirm();
        }

        // 模式四：凭证式
        if (req.isPath(Api.client_token)) {
            return clientToken();
        }

        // 默认返回
        return OpenAuthConst.NOT_HANDLE;
    }

    /**
     * 模式一：Code授权码 / 模式二：隐藏式
     *
     * @return 处理结果
     */
    public Object authorize() {

        // 获取变量
        SaRequest req = SaHolder.getRequest();
        SaResponse res = SaHolder.getResponse();
        ServerConfig cfg = OpenAuthManager.getServerConfig();
        OpenAuthDataGenerate dataGenerate = OpenAuthManager.getDataGenerate();
        OpenAuthTemplate oauth2Template = OpenAuthManager.getTemplate();
        String responseType = req.getParamNotNull(Param.response_type);

        // 1、先判断是否开启了指定的授权模式
        checkAuthorizeResponseType(responseType, req, cfg);

        // 2、如果尚未登录, 则先去登录
        if (!OpenAuthManager.getStpLogic().isLogin()) {
            return cfg.notLogin.get();
        }

        // 3、构建请求 Model
        RequestAuthModel ra = OpenAuthManager.getDataResolver().readRequestAuthModel(req, OpenAuthManager.getStpLogic().getLoginId());

        // 4、校验：重定向域名是否合法
        oauth2Template.checkRedirectUri(ra.clientId, ra.redirectUri);

        // 5、校验：此次申请的Scope，该Client是否已经签约
        oauth2Template.checkContractScope(ra.clientId, ra.scopes);

        // 6、判断：如果此次申请的Scope，该用户尚未授权，则转到授权页面
        boolean isNeedCarefulConfirm = oauth2Template.isNeedCarefulConfirm(ra.loginId, ra.clientId, ra.scopes);
        if (isNeedCarefulConfirm) {
            return cfg.confirm.apply(ra.clientId, ra.scopes);
        }

        // 7、判断授权类型，重定向到不同地址
        // 如果是 授权码式，则：开始重定向授权，下放code
        if (ResponseType.code.equals(ra.responseType)) {
            CodeModel codeModel = dataGenerate.generateCode(ra);
            String redirectUri = dataGenerate.buildRedirectUri(ra.redirectUri, codeModel.code, ra.state);
            // return res.redirect(redirectUri);
            return Result.success().singleData(redirectUri);
        }

        // 如果是 隐藏式，则：开始重定向授权，下放 token
        if (ResponseType.token.equals(ra.responseType)) {
            AccessTokenModel at = dataGenerate.generateAccessToken(ra, false);
            String redirectUri = dataGenerate.buildImplicitRedirectUri(ra.redirectUri, at.accessToken, ra.state);
            // return res.redirect(redirectUri);
            return Result.success().singleData(redirectUri);
        }

        // 默认返回
        throw new OpenAuthException("无效 response_type: " + ra.responseType).setCode(OpenAuthErrorCode.CODE_30125);
    }

    /**
     * Code 换 Access-Token / 模式三：密码式 / 自定义 grant_type
     *
     * @return 处理结果
     */
    public Object token() {
        AccessTokenModel accessTokenModel = OpenAuthStrategy.INSTANCE.grantTypeAuth.apply(SaHolder.getRequest());
        return OpenAuthManager.getDataResolver().buildAccessTokenReturnValue(accessTokenModel);
    }

    /**
     * Refresh-Token 刷新 Access-Token
     *
     * @return 处理结果
     */
    public Object refresh() {
        SaRequest req = SaHolder.getRequest();
        String grantType = req.getParamNotNull(Param.grant_type);
        OpenAuthException.throwBy(!grantType.equals(OpenAuthGrantType.refresh_token), "无效 grant_type：" + grantType, OpenAuthErrorCode.CODE_30126);
        AccessTokenModel accessTokenModel = OpenAuthStrategy.INSTANCE.grantTypeAuth.apply(req);
        return OpenAuthManager.getDataResolver().buildRefreshTokenReturnValue(accessTokenModel);
    }

    /**
     * 回收 Access-Token
     *
     * @return 处理结果
     */
    public Object revoke() {
        // 获取变量
        OpenAuthTemplate oauth2Template = OpenAuthManager.getTemplate();
        SaRequest req = SaHolder.getRequest();

        // 获取参数
        ClientIdAndSecretModel clientIdAndSecret = OpenAuthManager.getDataResolver().readClientIdAndSecret(req);
        String clientId = clientIdAndSecret.clientId;
        String clientSecret = clientIdAndSecret.clientSecret;
        String accessToken = req.getParamNotNull(Param.access_token);

        // 如果 Access-Token 不存在，直接返回
        if (oauth2Template.getAccessToken(accessToken) == null) {
            return SaResult.ok("access_token不存在：" + accessToken);
        }

        // 校验参数
        oauth2Template.checkAccessTokenParam(clientId, clientSecret, accessToken);

        // 回收 Access-Token
        oauth2Template.revokeAccessToken(accessToken);

        // 返回
        return OpenAuthManager.getDataResolver().buildRevokeTokenReturnValue();
    }

    /**
     * doLogin 登录接口
     *
     * @return 处理结果
     */
    public Object doLogin() {
        // 获取变量
        SaRequest req = SaHolder.getRequest();
        ServerConfig cfg = OpenAuthManager.getServerConfig();

        return cfg.doLogin.apply(req.getParam(Param.username), req.getParam(Param.password));
    }

    /**
     * doConfirm 确认授权接口
     *
     * @return 处理结果
     */
    public Object doConfirm() {
        // 获取变量
        SaRequest req = SaHolder.getRequest();
        System.out.println(req.getParamMap());
        String clientId = req.getParamNotNull(Param.client_id);
        Object loginId = OpenAuthManager.getStpLogic().getLoginId();
        String scope = req.getParamNotNull(Param.scope);
        List<String> scopes = OpenAuthManager.getDataConverter().convertScopeStringToList(scope);
        OpenAuthDataGenerate dataGenerate = OpenAuthManager.getDataGenerate();
        OpenAuthTemplate oauth2Template = OpenAuthManager.getTemplate();

        // 此请求只允许 POST 方式
        if (!req.isMethod(SaHttpMethod.POST)) {
            throw new OpenAuthException("无效请求方式：" + req.getMethod()).setCode(OpenAuthErrorCode.CODE_30151);
        }

        // 确认授权
        oauth2Template.saveGrantScope(clientId, loginId, scopes);

        // 判断所需的返回结果模式
        boolean buildRedirectUri = req.isParam(Param.build_redirect_uri, "true");

        // -------- 情况1：只返回确认结果即可
        if (!buildRedirectUri) {
            oauth2Template.saveGrantScope(clientId, loginId, scopes);
            return SaResult.ok();
        }

        // -------- 情况2：需要返回最终的 redirect_uri 地址

        // s3、构建请求 Model
        RequestAuthModel ra = OpenAuthManager.getDataResolver().readRequestAuthModel(req, loginId);

        // 7、判断授权类型，构建不同的重定向地址
        // 		如果是 授权码式，则：开始重定向授权，下放code
        if (ResponseType.code.equals(ra.responseType)) {
            CodeModel codeModel = dataGenerate.generateCode(ra);
            String redirectUri = dataGenerate.buildRedirectUri(ra.redirectUri, codeModel.code, ra.state);
            return SaResult.ok().set(Param.redirect_uri, redirectUri);
        }

        // 		如果是 隐藏式，则：开始重定向授权，下放 token
        if (ResponseType.token.equals(ra.responseType)) {
            AccessTokenModel at = dataGenerate.generateAccessToken(ra, false);
            String redirectUri = dataGenerate.buildImplicitRedirectUri(ra.redirectUri, at.accessToken, ra.state);
            return SaResult.ok().set(Param.redirect_uri, redirectUri);
        }

        // 默认返回
        throw new OpenAuthException("无效response_type: " + ra.responseType).setCode(OpenAuthErrorCode.CODE_30125);
    }

    /**
     * 模式四：凭证式
     *
     * @return 处理结果
     */
    public Object clientToken() {
        // 获取变量
        SaRequest req = SaHolder.getRequest();
        ServerConfig cfg = OpenAuthManager.getServerConfig();
        OpenAuthTemplate oauth2Template = OpenAuthManager.getTemplate();

        String grantType = req.getParamNotNull(Param.grant_type);
        if (!grantType.equals(OpenAuthGrantType.client_credentials)) {
            throw new OpenAuthException("无效 grant_type：" + grantType).setCode(OpenAuthErrorCode.CODE_30126);
        }
        if (!cfg.enableClientCredentials) {
            throwErrorSystemNotEnableModel();
        }
        if (!currClientModel().getAllowGrantTypes().contains(OpenAuthGrantType.client_credentials)) {
            throwErrorClientNotEnableModel();
        }

        // 获取参数
        ClientIdAndSecretModel clientIdAndSecret = OpenAuthManager.getDataResolver().readClientIdAndSecret(req);
        String clientId = clientIdAndSecret.clientId;
        String clientSecret = clientIdAndSecret.clientSecret;
        List<String> scopes = OpenAuthManager.getDataConverter().convertScopeStringToList(req.getParam(Param.scope));

        // 校验 ClientScope
        oauth2Template.checkContractScope(clientId, scopes);

        // 校验 ClientSecret
        oauth2Template.checkClientSecret(clientId, clientSecret);

        // 生成
        ClientTokenModel ct = OpenAuthManager.getDataGenerate().generateClientToken(clientId, scopes);

        // 返回
        return OpenAuthManager.getDataResolver().buildClientTokenReturnValue(ct);
    }


    // ----------- 代码块封装 --------------

    /**
     * 根据当前请求提交的 client_id 参数获取 SaClientModel 对象
     *
     * @return /
     */
    public OpenAuthClientModel currClientModel() {
        OpenAuthTemplate oauth2Template = OpenAuthManager.getTemplate();
        ClientIdAndSecretModel clientIdAndSecret = OpenAuthManager.getDataResolver().readClientIdAndSecret(SaHolder.getRequest());
        return oauth2Template.checkClientModel(clientIdAndSecret.clientId);
    }

    /**
     * 校验当前请求中提交的 clientId 和 clientSecret 是否正确，如果正确则返回 SaClientModel 对象
     *
     * @return /
     */
    public OpenAuthClientModel checkCurrClientSecret() {
        OpenAuthTemplate oauth2Template = OpenAuthManager.getTemplate();
        ClientIdAndSecretModel clientIdAndSecret = OpenAuthManager.getDataResolver().readClientIdAndSecret(SaHolder.getRequest());
        return oauth2Template.checkClientSecret(clientIdAndSecret.clientId, clientIdAndSecret.clientSecret);
    }

    /**
     * 校验 authorize 路由的 ResponseType 参数
     */
    public void checkAuthorizeResponseType(String responseType, SaRequest req, ServerConfig cfg) {
        // 模式一：Code授权码
        if (responseType.equals(ResponseType.code)) {
            if (!cfg.enableAuthorizationCode) {
                throwErrorSystemNotEnableModel();
            }
            if (!currClientModel().getAllowGrantTypes().contains(OpenAuthGrantType.authorization_code)) {
                throwErrorClientNotEnableModel();
            }
        }
        // 模式二：隐藏式
        else if (responseType.equals(ResponseType.token)) {
            if (!cfg.enableImplicit) {
                throwErrorSystemNotEnableModel();
            }
            if (!currClientModel().getAllowGrantTypes().contains(OpenAuthGrantType.implicit)) {
                throwErrorClientNotEnableModel();
            }
        }
        // 其它
        else {
            throw new OpenAuthException("无效 response_type: " + responseType).setCode(OpenAuthErrorCode.CODE_30125);
        }
    }

    /**
     * 系统未开放此授权模式时抛出异常
     */
    public void throwErrorSystemNotEnableModel() {
        throw new OpenAuthException("系统暂未开放此授权模式").setCode(OpenAuthErrorCode.CODE_30141);
    }

    /**
     * 应用未开放此授权模式时抛出异常
     */
    public void throwErrorClientNotEnableModel() {
        throw new OpenAuthException("应用暂未开放此授权模式").setCode(OpenAuthErrorCode.CODE_30142);
    }

}
