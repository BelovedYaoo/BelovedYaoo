package top.belovedyaoo.openiam.strategy;

import cn.dev33.satoken.SaManager;
import top.belovedyaoo.openiam.enums.OidcExceptionEnum;
import top.belovedyaoo.openiam.enums.OpenAuthExceptionEnum;
import top.belovedyaoo.openiam.function.strategy.CreateAccessTokenValueFunction;
import top.belovedyaoo.openiam.function.strategy.CreateClientTokenValueFunction;
import top.belovedyaoo.openiam.function.strategy.CreateCodeValueFunction;
import top.belovedyaoo.openiam.function.strategy.CreateRefreshTokenValueFunction;
import top.belovedyaoo.openiam.function.strategy.GrantTypeAuthFunction;
import top.belovedyaoo.openiam.function.strategy.ScopeWorkAccessTokenFunction;
import top.belovedyaoo.openiam.function.strategy.ScopeWorkClientTokenFunction;
import top.belovedyaoo.openiam.core.OpenAuthManager;
import top.belovedyaoo.openiam.config.ServerConfigurer;
import top.belovedyaoo.openiam.consts.OpenAuthGrantType;
import top.belovedyaoo.openiam.consts.OpenAuthConst;
import top.belovedyaoo.openiam.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openiam.data.model.request.ClientIdAndSecretModel;
import top.belovedyaoo.openiam.handler.AuthorizationCodeGrantTypeHandler;
import top.belovedyaoo.openiam.handler.PasswordGrantTypeHandler;
import top.belovedyaoo.openiam.handler.RefreshTokenGrantTypeHandler;
import top.belovedyaoo.openiam.handler.GrantTypeHandlerInterface;
import top.belovedyaoo.openiam.scope.CommonScope;
import top.belovedyaoo.openiam.scope.handler.OidcScopeHandler;
import top.belovedyaoo.openiam.scope.handler.OpenIdScopeHandler;
import top.belovedyaoo.openiam.scope.handler.OpenAuthScopeHandlerInterface;
import top.belovedyaoo.openiam.scope.handler.UserIdScopeHandler;
import cn.dev33.satoken.util.SaFoxUtil;
import top.belovedyaoo.opencore.advice.exception.OpenException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenAuth 相关策略
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public final class OpenAuthStrategy {

	private OpenAuthStrategy() {
		registerDefaultScopeHandler();
		registerDefaultGrantTypeHandler();
	}

	/**
	 * 全局单例引用
	 */
	public static final OpenAuthStrategy INSTANCE = new OpenAuthStrategy();

	// 权限处理器

	/**
	 * 权限处理器集合
	 */
	public Map<String, OpenAuthScopeHandlerInterface> scopeHandlerMap = new LinkedHashMap<>();

	/**
	 * 注册所有默认的权限处理器
	 */
	public void registerDefaultScopeHandler() {
		scopeHandlerMap.put(CommonScope.OPENID, new OpenIdScopeHandler());
		scopeHandlerMap.put(CommonScope.USERID, new UserIdScopeHandler());
		scopeHandlerMap.put(CommonScope.OIDC, new OidcScopeHandler());
	}

	/**
	 * 注册一个权限处理器
	 */
	public void registerScopeHandler(OpenAuthScopeHandlerInterface handler) {
		scopeHandlerMap.put(handler.getHandlerScope(), handler);
		SaManager.getLog().info("自定义 SCOPE [{}] (处理器: {})", handler.getHandlerScope(), handler.getClass().getCanonicalName());
	}

	/**
	 * 移除一个权限处理器
	 */
	public void removeScopeHandler(String scope) {
		scopeHandlerMap.remove(scope);
	}

	/**
	 * 根据 scope 信息对一个 AccessTokenModel 进行加工处理
	 */
	public ScopeWorkAccessTokenFunction workAccessTokenByScope = (at) -> {
		if(at.scopes != null && !at.scopes.isEmpty()) {
			for (String scope : at.scopes) {
				OpenAuthScopeHandlerInterface handler = scopeHandlerMap.get(scope);
				if(handler != null) {
					handler.workAccessToken(at);
				}
			}
		}
		OpenAuthScopeHandlerInterface finallyWorkScopeHandler = scopeHandlerMap.get(OpenAuthConst._FINALLY_WORK_SCOPE);
		if(finallyWorkScopeHandler != null) {
			finallyWorkScopeHandler.workAccessToken(at);
		}
	};

	/**
	 * 根据 scope 信息对一个 ClientTokenModel 进行加工处理
	 */
	public ScopeWorkClientTokenFunction workClientTokenByScope = (ct) -> {
		if(ct.scopes != null && !ct.scopes.isEmpty()) {
			for (String scope : ct.scopes) {
				OpenAuthScopeHandlerInterface handler = scopeHandlerMap.get(scope);
				if(handler != null) {
					handler.workClientToken(ct);
				}
			}
		}
		OpenAuthScopeHandlerInterface finallyWorkScopeHandler = scopeHandlerMap.get(OpenAuthConst._FINALLY_WORK_SCOPE);
		if(finallyWorkScopeHandler != null) {
			finallyWorkScopeHandler.workClientToken(ct);
		}
	};

	// grant_type 处理器

	/**
	 * grant_type 处理器集合
	 */
	public Map<String, GrantTypeHandlerInterface> grantTypeHandlerMap = new LinkedHashMap<>();

	/**
	 * 注册所有默认的权限处理器
	 */
	public void registerDefaultGrantTypeHandler() {
		grantTypeHandlerMap.put(OpenAuthGrantType.authorization_code, new AuthorizationCodeGrantTypeHandler());
		grantTypeHandlerMap.put(OpenAuthGrantType.password, new PasswordGrantTypeHandler());
		grantTypeHandlerMap.put(OpenAuthGrantType.refresh_token, new RefreshTokenGrantTypeHandler());
	}

	/**
	 * 注册一个权限处理器
	 */
	public void registerGrantTypeHandler(GrantTypeHandlerInterface handler) {
		grantTypeHandlerMap.put(handler.getHandlerGrantType(), handler);
		SaManager.getLog().info("自定义 GRANT_TYPE [{}] (处理器: {})", handler.getHandlerGrantType(), handler.getClass().getCanonicalName());
	}

	/**
	 * 移除一个权限处理器
	 */
	public void removeGrantTypeHandler(String scope) {
		scopeHandlerMap.remove(scope);
	}

	/**
	 * 根据 scope 信息对一个 AccessTokenModel 进行加工处理
	 */
	public GrantTypeAuthFunction grantTypeAuth = (req) -> {
		String grantType = req.getParamNotNull(OpenAuthConst.Param.grant_type);
		GrantTypeHandlerInterface grantTypeHandler = grantTypeHandlerMap.get(grantType);
		OpenException.throwBy(grantTypeHandler == null, OidcExceptionEnum.INVALID_GRANT_TYPE,grantType);

		// 看看全局是否开启了此 grantType
		ServerConfigurer config = OpenAuthManager.getServerConfig();
		OpenException.throwBy(grantType.equals(OpenAuthGrantType.authorization_code) && !config.getEnableAuthorizationCode(),OidcExceptionEnum.INVALID_GRANT_TYPE,grantType);
		OpenException.throwBy(grantType.equals(OpenAuthGrantType.password) && !config.getEnablePassword(),OidcExceptionEnum.INVALID_GRANT_TYPE,grantType);

		// 校验 clientSecret 和 scope
		ClientIdAndSecretModel clientIdAndSecretModel = OpenAuthManager.getDataResolver().readClientIdAndSecret(req);
		List<String> scopes = OpenAuthManager.getDataConverter().convertScopeStringToList(req.getParam(OpenAuthConst.Param.scope));
		OpenAuthClientModel clientModel = OpenAuthManager.getTemplate().checkClientSecretAndScope(clientIdAndSecretModel.getClientId(), clientIdAndSecretModel.getClientSecret(), scopes);

		// 检测应用是否开启此 grantType
		OpenException.throwBy(!clientModel.getAllowGrantTypes().contains(grantType), OpenAuthExceptionEnum.NOT_SUPPORTED_AUTH_MODEL,"应用暂未开放的 GrantType",grantType);

		// 调用 处理器
		return grantTypeHandler.getAccessToken(req, clientIdAndSecretModel.getClientId(), scopes);
	};


	// ----------------------- 所有策略

	/**
	 * 创建一个 code value
	 */
	public CreateCodeValueFunction createCodeValue = (clientId, loginId, scopes) -> {
		return SaFoxUtil.getRandomString(60);
	};

	/**
	 * 创建一个 AccessToken value
	 */
	public CreateAccessTokenValueFunction createAccessToken = (clientId, loginId, scopes) -> {
		return SaFoxUtil.getRandomString(60);
	};

	/**
	 * 创建一个 RefreshToken value
	 */
	public CreateRefreshTokenValueFunction createRefreshToken = (clientId, loginId, scopes) -> {
		return SaFoxUtil.getRandomString(60);
	};

	/**
	 * 创建一个 ClientToken value
	 */
	public CreateClientTokenValueFunction createClientToken = (clientId, scopes) -> {
		return SaFoxUtil.getRandomString(60);
	};

}
