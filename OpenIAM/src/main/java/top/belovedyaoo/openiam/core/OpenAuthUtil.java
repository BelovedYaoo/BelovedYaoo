package top.belovedyaoo.openiam.core;

import top.belovedyaoo.openiam.data.model.AccessTokenModel;
import top.belovedyaoo.openiam.data.model.ClientTokenModel;
import top.belovedyaoo.openiam.data.model.RefreshTokenModel;
import top.belovedyaoo.openiam.data.model.loader.OpenAuthClientModel;

import java.util.List;

/**
 * OpenAuth 模块 工具类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class OpenAuthUtil {

	// ----------------- ClientModel 相关 -----------------

	/**
	 * 获取 ClientModel，根据 clientId
	 *
	 * @param clientId /
	 * @return /
	 */
	public static OpenAuthClientModel getClientModel(String clientId) {
		return OpenAuthManager.getTemplate().getClientModel(clientId);
	}

	/**
	 * 校验 clientId 信息并返回 ClientModel，如果找不到对应 Client 信息则抛出异常
	 * @param clientId /
	 * @return /
	 */
	public static OpenAuthClientModel checkClientModel(String clientId) {
		return OpenAuthManager.getTemplate().checkClientModel(clientId);
	}

	/**
	 * 校验：clientId 与 clientSecret 是否正确
	 * @param clientId 应用id
	 * @param clientSecret 秘钥
	 * @return SaClientModel对象
	 */
	public static OpenAuthClientModel checkClientSecret(String clientId, String clientSecret) {
		return OpenAuthManager.getTemplate().checkClientSecret(clientId, clientSecret);
	}

	/**
	 * 校验：clientId 与 clientSecret 是否正确，并且是否签约了指定 scopes
	 * @param clientId 应用id
	 * @param clientSecret 秘钥
	 * @param scopes 权限
	 * @return SaClientModel对象
	 */
	public static OpenAuthClientModel checkClientSecretAndScope(String clientId, String clientSecret, List<String> scopes) {
		return OpenAuthManager.getTemplate().checkClientSecretAndScope(clientId, clientSecret, scopes);
	}

	/**
	 * 判断：该 Client 是否签约了指定的 Scope，返回 true 或 false
	 * @param clientId 应用id
	 * @param scopes 权限
	 * @return /
	 */
	public static boolean isContractScope(String clientId, List<String> scopes) {
		return OpenAuthManager.getTemplate().isContractScope(clientId, scopes);
	}

	/**
	 * 校验：该 Client 是否签约了指定的 Scope，如果没有则抛出异常
	 * @param clientId 应用id
	 * @param scopes 权限列表
	 * @return /
	 */
	public static OpenAuthClientModel checkContractScope(String clientId, List<String> scopes) {
		return OpenAuthManager.getTemplate().checkContractScope(clientId, scopes);
	}

	/**
	 * 校验：该 Client 是否签约了指定的 Scope，如果没有则抛出异常
	 * @param cm 应用
	 * @param scopes 权限列表
	 * @return /
	 */
	public static OpenAuthClientModel checkContractScope(OpenAuthClientModel cm, List<String> scopes) {
		return OpenAuthManager.getTemplate().checkContractScope(cm, scopes);
	}

	// --------- redirect_uri 相关

	/**
	 * 校验：该 Client 使用指定 url 作为回调地址，是否合法
	 * @param clientId 应用id
	 * @param url 指定url
	 */
	public static void checkRedirectUri(String clientId, String url) {
		OpenAuthManager.getTemplate().checkRedirectUri(clientId, url);
	}

	// --------- 授权相关

	/**
	 * 判断：指定 loginId 是否对一个 Client 授权给了指定 Scope
	 * @param loginId 账号id
	 * @param clientId 应用id
	 * @param scopes 权限
	 * @return 是否已经授权
	 */
	public static boolean isGrantScope(Object loginId, String clientId, List<String> scopes) {
		return OpenAuthManager.getTemplate().isGrantScope(loginId, clientId, scopes);
	}


	// ----------------- Access-Token 相关 -----------------

	/**
	 * 获取 AccessTokenModel，无效的 AccessToken 会返回 null
	 * @param accessToken /
	 * @return /
	 */
	public static AccessTokenModel getAccessToken(String accessToken) {
		return OpenAuthManager.getTemplate().getAccessToken(accessToken);
	}

	/**
	 * 校验 Access-Token，成功返回 AccessTokenModel，失败则抛出异常
	 * @param accessToken /
	 * @return /
	 */
	public static AccessTokenModel checkAccessToken(String accessToken) {
		return OpenAuthManager.getTemplate().checkAccessToken(accessToken);
	}

	/**
	 * 获取 Access-Token，根据索引： clientId、loginId
	 * @param clientId /
	 * @param loginId /
	 * @return /
	 */
	public static String getAccessTokenValue(String clientId, Object loginId) {
		return OpenAuthManager.getTemplate().getAccessTokenValue(clientId, loginId);
	}

	/**
	 * 判断：指定 Access-Token 是否具有指定 Scope 列表，返回 true 或 false
	 * @param accessToken Access-Token
	 * @param scopes 需要校验的权限列表
	 */
	public static boolean hasAccessTokenScope(String accessToken, String... scopes) {
		return OpenAuthManager.getTemplate().hasAccessTokenScope(accessToken, scopes);
	}

	/**
	 * 校验：指定 Access-Token 是否具有指定 Scope 列表，如果不具备则抛出异常
	 * @param accessToken Access-Token
	 * @param scopes 需要校验的权限列表
	 */
	public static void checkAccessTokenScope(String accessToken, String... scopes) {
		OpenAuthManager.getTemplate().checkAccessTokenScope(accessToken, scopes);
	}

	/**
	 * 获取 Access-Token 所代表的LoginId
	 * @param accessToken Access-Token
	 * @return LoginId
	 */
	public static Object getLoginIdByAccessToken(String accessToken) {
		return OpenAuthManager.getTemplate().getLoginIdByAccessToken(accessToken);
	}

	/**
	 * 获取 Access-Token 所代表的 clientId
	 * @param accessToken Access-Token
	 * @return LoginId
	 */
	public static Object getClientIdByAccessToken(String accessToken) {
		return OpenAuthManager.getTemplate().getClientIdByAccessToken(accessToken);
	}

	/**
	 * 回收 Access-Token
	 * @param accessToken Access-Token值
	 */
	public static void revokeAccessToken(String accessToken) {
		OpenAuthManager.getTemplate().revokeAccessToken(accessToken);
	}

	/**
	 * 回收 Access-Token，根据索引： clientId、loginId
	 * @param clientId /
	 * @param loginId /
	 */
	public static void revokeAccessTokenByIndex(String clientId, Object loginId) {
		OpenAuthManager.getTemplate().revokeAccessTokenByIndex(clientId, loginId);
	}


	// ----------------- Refresh-Token 相关 -----------------

	/**
	 * 获取 RefreshTokenModel，无效的 RefreshToken 会返回 null
	 * @param refreshToken /
	 * @return /
	 */
	public static RefreshTokenModel getRefreshToken(String refreshToken) {
		return OpenAuthManager.getTemplate().getRefreshToken(refreshToken);
	}

	/**
	 * 校验 Refresh-Token，成功返回 RefreshTokenModel，失败则抛出异常
	 * @param refreshToken /
	 * @return /
	 */
	public static RefreshTokenModel checkRefreshToken(String refreshToken) {
		return OpenAuthManager.getTemplate().checkRefreshToken(refreshToken);
	}

	/**
	 * 获取 Refresh-Token，根据索引： clientId、loginId
	 * @param clientId /
	 * @param loginId /
	 * @return /
	 */
	public static String getRefreshTokenValue(String clientId, Object loginId) {
		return OpenAuthManager.getTemplate().getRefreshTokenValue(clientId, loginId);
	}

	/**
	 * 根据 RefreshToken 刷新出一个 AccessToken
	 * @param refreshToken /
	 * @return /
	 */
	public static AccessTokenModel refreshAccessToken(String refreshToken) {
		return OpenAuthManager.getTemplate().refreshAccessToken(refreshToken);
	}


	// ----------------- Client-Token 相关 -----------------

	/**
	 * 获取 ClientTokenModel，无效的 ClientToken 会返回 null
	 * @param clientToken /
	 * @return /
	 */
	public static ClientTokenModel getClientToken(String clientToken) {
		return OpenAuthManager.getTemplate().getClientToken(clientToken);
	}

	/**
	 * 校验 Client-Token，成功返回 ClientTokenModel，失败则抛出异常
	 * @param clientToken /
	 * @return /
	 */
	public static ClientTokenModel checkClientToken(String clientToken) {
		return OpenAuthManager.getTemplate().checkClientToken(clientToken);
	}

	/**
	 * 获取 ClientToken，根据索引： clientId
	 * @param clientId /
	 * @return /
	 */
	public static String getClientTokenValue(String clientId) {
		return OpenAuthManager.getTemplate().getClientTokenValue(clientId);
	}

	/**
	 * 判断：指定 Client-Token 是否具有指定 Scope 列表，返回 true 或 false
	 * @param clientToken Client-Token
	 * @param scopes 需要校验的权限列表
	 */
	public static boolean hasClientTokenScope(String clientToken, String... scopes) {
		return OpenAuthManager.getTemplate().hasClientTokenScope(clientToken, scopes);
	}

	/**
	 * 校验：指定 Client-Token 是否具有指定 Scope 列表，如果不具备则抛出异常
	 * @param clientToken Client-Token
	 * @param scopes 需要校验的权限列表
	 */
	public static void checkClientTokenScope(String clientToken, String... scopes) {
		OpenAuthManager.getTemplate().checkClientTokenScope(clientToken, scopes);
	}

	/**
	 * 回收 ClientToken
	 *
	 * @param clientToken /
	 */
	public static void revokeClientToken(String clientToken) {
		OpenAuthManager.getTemplate().revokeClientToken(clientToken);
	}

	/**
	 * 回收 ClientToken，根据索引： clientId
	 *
	 * @param clientId /
	 */
	public static void revokeClientTokenByIndex(String clientId) {
		OpenAuthManager.getTemplate().revokeClientTokenByIndex(clientId);
	}

	/**
	 * 回收 Lower-Client-Token，根据索引： clientId
	 *
	 * @param clientId /
	 */
	public static void revokeLowerClientTokenByIndex(String clientId) {
		OpenAuthManager.getTemplate().revokeLowerClientTokenByIndex(clientId);
	}

}
