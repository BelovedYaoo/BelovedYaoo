package top.belovedyaoo.openiam.oauth2.dao;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.dao.SaTokenDao;
import top.belovedyaoo.openiam.oauth2.OpenAuthManager;
import top.belovedyaoo.openiam.oauth2.data.model.AccessTokenModel;
import top.belovedyaoo.openiam.oauth2.data.model.ClientTokenModel;
import top.belovedyaoo.openiam.oauth2.data.model.CodeModel;
import top.belovedyaoo.openiam.oauth2.data.model.RefreshTokenModel;
import top.belovedyaoo.openiam.oauth2.data.model.loader.OpenAuthClientModel;
import cn.dev33.satoken.util.SaFoxUtil;

import java.util.List;

import static top.belovedyaoo.openiam.oauth2.OpenAuthUtil.checkClientModel;


/**
 * OpenAuth 数据持久层
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface OpenAuthDao {

	// ------------------- save 数据

	/**
	 * 持久化：Code-Model
	 * @param c .
	 */
	default void saveCode(CodeModel c) {
		if(c == null) {
			return;
		}
		getSaTokenDao().setObject(splicingCodeSaveKey(c.code), c, OpenAuthManager.getServerConfig().getCodeTimeout());
	}

	/**
	 * 持久化：Code-索引
	 * @param c .
	 */
	default void saveCodeIndex(CodeModel c) {
		if(c == null) {
			return;
		}
		getSaTokenDao().set(splicingCodeIndexKey(c.clientId, c.loginId), c.code, OpenAuthManager.getServerConfig().getCodeTimeout());
	}

	/**
	 * 持久化：AccessToken-Model
	 * @param at .
	 */
	default void saveAccessToken(AccessTokenModel at) {
		if(at == null) {
			return;
		}
		getSaTokenDao().setObject(splicingAccessTokenSaveKey(at.accessToken), at, at.getExpiresIn());
	}

	/**
	 * 持久化：AccessToken-索引
	 * @param at .
	 */
	default void saveAccessTokenIndex(AccessTokenModel at) {
		if(at == null) {
			return;
		}
		getSaTokenDao().set(splicingAccessTokenIndexKey(at.clientId, at.loginId), at.accessToken, at.getExpiresIn());
	}

	/**
	 * 持久化：RefreshToken-Model
	 * @param rt .
	 */
	default void saveRefreshToken(RefreshTokenModel rt) {
		if(rt == null) {
			return;
		}
		getSaTokenDao().setObject(splicingRefreshTokenSaveKey(rt.refreshToken), rt, rt.getExpiresIn());
	}

	/**
	 * 持久化：RefreshToken-索引
	 * @param rt .
	 */
	default void saveRefreshTokenIndex(RefreshTokenModel rt) {
		if(rt == null) {
			return;
		}
		getSaTokenDao().set(splicingRefreshTokenIndexKey(rt.clientId, rt.loginId), rt.refreshToken, rt.getExpiresIn());
	}

	/**
	 * 持久化：ClientToken-Model
	 * @param ct .
	 */
	default void saveClientToken(ClientTokenModel ct) {
		if(ct == null) {
			return;
		}
		getSaTokenDao().setObject(splicingClientTokenSaveKey(ct.clientToken), ct, ct.getExpiresIn());
	}

	/**
	 * 持久化：ClientToken-索引
	 * @param ct .
	 */
	default void saveClientTokenIndex(ClientTokenModel ct) {
		if(ct == null) {
			return;
		}
		getSaTokenDao().set(splicingClientTokenIndexKey(ct.clientId), ct.clientToken, ct.getExpiresIn());
	}

	/**
	 * 持久化：Lower-Client-Token 索引
	 * @param ct /
	 */
	default void saveLowerClientTokenIndex(ClientTokenModel ct) {
		if(ct == null) {
			return;
		}
		long ttl = ct.getExpiresIn();
		// 如果此 client 单独配置了 Lower-Client-Token 的 TTL，则使用单独配置
		OpenAuthClientModel cm = checkClientModel(ct.clientId);
		if (cm.getLowerClientTokenTimeout() != -1) {
			ttl = cm.getLowerClientTokenTimeout();
		}
		getSaTokenDao().set(splicingLowerClientTokenIndexKey(ct.clientId), ct.clientToken, ttl);
	}

	/**
	 * 持久化：用户授权记录
	 * @param clientId 应用id
	 * @param loginId 账号id
	 * @param scopes 权限列表
	 */
	default void saveGrantScope(String clientId, Object loginId, List<String> scopes) {
		if( ! SaFoxUtil.isEmpty(scopes)) {
			long ttl = checkClientModel(clientId).getAccessTokenTimeout();
			String value = OpenAuthManager.getDataConverter().convertScopeListToString(scopes);
			getSaTokenDao().set(splicingGrantScopeKey(clientId, loginId), value, ttl);
		}
	}

	/**
	 * 持久化：state
	 * @param state /
	 */
	default void saveState(String state) {
		if( ! SaFoxUtil.isEmpty(state)) {
			long ttl = OpenAuthManager.getServerConfig().getCodeTimeout();
			getSaTokenDao().set(splicingStateSaveKey(state), state, ttl);
		}
	}

	/**
	 * 持久化：nonce-索引
	 * @param c .
	 */
	default void saveCodeNonceIndex(CodeModel c) {
		if(c == null || SaFoxUtil.isEmpty(c.nonce)) {
			return;
		}
		getSaTokenDao().set(splicingCodeNonceIndexSaveKey(c.code), c.nonce, OpenAuthManager.getServerConfig().getCodeTimeout());
	}


	// ------------------- delete数据

	/**
	 * 删除：Code
	 * @param code 值
	 */
	default void deleteCode(String code) {
		if(code != null) {
			getSaTokenDao().deleteObject(splicingCodeSaveKey(code));
		}
	}

	/**
	 * 删除：Code索引
	 * @param clientId 应用id
	 * @param loginId 账号id
	 */
	default void deleteCodeIndex(String clientId, Object loginId) {
		getSaTokenDao().delete(splicingCodeIndexKey(clientId, loginId));
	}

	/**
	 * 删除：Access-Token
	 * @param accessToken 值
	 */
	default void deleteAccessToken(String accessToken) {
		if(accessToken != null) {
			getSaTokenDao().deleteObject(splicingAccessTokenSaveKey(accessToken));
		}
	}

	/**
	 * 删除：Access-Token索引
	 * @param clientId 应用id
	 * @param loginId 账号id
	 */
	default void deleteAccessTokenIndex(String clientId, Object loginId) {
		getSaTokenDao().delete(splicingAccessTokenIndexKey(clientId, loginId));
	}

	/**
	 * 删除：Refresh-Token
	 * @param refreshToken 值
	 */
	default void deleteRefreshToken(String refreshToken) {
		if(refreshToken != null) {
			getSaTokenDao().deleteObject(splicingRefreshTokenSaveKey(refreshToken));
		}
	}

	/**
	 * 删除：Refresh-Token索引
	 * @param clientId 应用id
	 * @param loginId 账号id
	 */
	default void deleteRefreshTokenIndex(String clientId, Object loginId) {
		getSaTokenDao().delete(splicingRefreshTokenIndexKey(clientId, loginId));
	}

	/**
	 * 删除：Client-Token
	 * @param clientToken 值
	 */
	default void deleteClientToken(String clientToken) {
		if(clientToken != null) {
			getSaTokenDao().deleteObject(splicingClientTokenSaveKey(clientToken));
		}
	}

	/**
	 * 删除：Client-Token索引
	 * @param clientId 应用id
	 */
	default void deleteClientTokenIndex(String clientId) {
		getSaTokenDao().delete(splicingClientTokenIndexKey(clientId));
	}

	/**
	 * 删除：Lower-Client-Token
	 * @param lowerClientToken 值
	 */
	default void deleteLowerClientToken(String lowerClientToken) {
		// 其实就是删除 ClientToken
		deleteClientToken(lowerClientToken);
	}

	/**
	 * 删除：Lower-Client-Token索引
	 * @param clientId 应用id
	 */
	default void deleteLowerClientTokenIndex(String clientId) {
		getSaTokenDao().delete(splicingLowerClientTokenIndexKey(clientId));
	}

	/**
	 * 删除：用户授权记录
	 * @param clientId 应用id
	 * @param loginId 账号id
	 */
	default void deleteGrantScope(String clientId, Object loginId) {
		getSaTokenDao().delete(splicingGrantScopeKey(clientId, loginId));
	}

	/**
	 * 删除：state记录
	 * @param state /
	 */
	default void deleteState(String state) {
		getSaTokenDao().delete(splicingStateSaveKey(state));
	}


	// ------------------- get 数据

	/**
	 * 获取：Code Model
	 * @param code .
	 * @return .
	 */
	default CodeModel getCode(String code) {
		if(code == null) {
			return null;
		}
		return (CodeModel)getSaTokenDao().getObject(splicingCodeSaveKey(code));
	}

	/**
	 * 获取：Code Value
	 * @param clientId 应用id
	 * @param loginId 账号id
	 * @return .
	 */
	default String getCodeValue(String clientId, Object loginId) {
		return getSaTokenDao().get(splicingCodeIndexKey(clientId, loginId));
	}

	/**
	 * 获取：Access-Token Model
	 * @param accessToken .
	 * @return .
	 */
	default AccessTokenModel getAccessToken(String accessToken) {
		if(accessToken == null) {
			return null;
		}
		return (AccessTokenModel)getSaTokenDao().getObject(splicingAccessTokenSaveKey(accessToken));
	}

	/**
	 * 获取：Access-Token Value
	 * @param clientId 应用id
	 * @param loginId 账号id
	 * @return .
	 */
	default String getAccessTokenValue(String clientId, Object loginId) {
		return getSaTokenDao().get(splicingAccessTokenIndexKey(clientId, loginId));
	}

	/**
	 * 获取：Refresh-Token Model
	 * @param refreshToken .
	 * @return .
	 */
	default RefreshTokenModel getRefreshToken(String refreshToken) {
		if(refreshToken == null) {
			return null;
		}
		return (RefreshTokenModel)getSaTokenDao().getObject(splicingRefreshTokenSaveKey(refreshToken));
	}

	/**
	 * 获取：Refresh-Token Value
	 * @param clientId 应用id
	 * @param loginId 账号id
	 * @return .
	 */
	default String getRefreshTokenValue(String clientId, Object loginId) {
		return getSaTokenDao().get(splicingRefreshTokenIndexKey(clientId, loginId));
	}

	/**
	 * 获取：Client-Token Model
	 * @param clientToken .
	 * @return .
	 */
	default ClientTokenModel getClientToken(String clientToken) {
		if(clientToken == null) {
			return null;
		}
		return (ClientTokenModel)getSaTokenDao().getObject(splicingClientTokenSaveKey(clientToken));
	}

	/**
	 * 获取：Client-Token Value
	 * @param clientId 应用id
	 * @return .
	 */
	default String getClientTokenValue(String clientId) {
		return getSaTokenDao().get(splicingClientTokenIndexKey(clientId));
	}

	/**
	 * 获取：Lower-Client-Token Value
	 * @param clientId 应用id
	 * @return .
	 */
	default String getLowerClientTokenValue(String clientId) {
		return getSaTokenDao().get(splicingLowerClientTokenIndexKey(clientId));
	}

	/**
	 * 获取：用户授权记录
	 * @param clientId 应用id
	 * @param loginId 账号id
	 * @return 权限
	 */
	default List<String> getGrantScope(String clientId, Object loginId) {
		String value = getSaTokenDao().get(splicingGrantScopeKey(clientId, loginId));
		return OpenAuthManager.getDataConverter().convertScopeStringToList(value);
	}

	/**
	 * 获取：state
	 * @param state /
	 * @return /
	 */
	default String getState(String state) {
		if(SaFoxUtil.isEmpty(state)) {
			return null;
		}
		return getSaTokenDao().get(splicingStateSaveKey(state));
	}

	/**
	 * 获取：nonce
	 * @param code /
	 * @return /
	 */
	default String getNonce(String code) {
		if(SaFoxUtil.isEmpty(code)) {
			return null;
		}
		return getSaTokenDao().get(splicingCodeNonceIndexSaveKey(code));
	}


	// ------------------- 拼接key

	/**
	 * 拼接key：Code持久化
	 * @param code 授权码
	 * @return key
	 */
	default String splicingCodeSaveKey(String code) {
		return getSaTokenConfig().getTokenName() + ":oauth2:code:" + code;
	}

	/**
	 * 拼接key：Code索引
	 * @param clientId 应用id
	 * @param loginId 账号id
	 * @return key
	 */
	default String splicingCodeIndexKey(String clientId, Object loginId) {
		return getSaTokenConfig().getTokenName() + ":oauth2:code-index:" + clientId + ":" + loginId;
	}

	/**
	 * 拼接key：Access-Token持久化
	 * @param accessToken accessToken
	 * @return key
	 */
	default String splicingAccessTokenSaveKey(String accessToken) {
		return getSaTokenConfig().getTokenName() + ":oauth2:access-token:" + accessToken;
	}

	/**
	 * 拼接key：Access-Token索引
	 * @param clientId 应用id
	 * @param loginId 账号id
	 * @return key
	 */
	default String splicingAccessTokenIndexKey(String clientId, Object loginId) {
		return getSaTokenConfig().getTokenName() + ":oauth2:access-token-index:" + clientId + ":" + loginId;
	}

	/**
	 * 拼接key：Refresh-Token持久化
	 * @param refreshToken refreshToken
	 * @return key
	 */
	default String splicingRefreshTokenSaveKey(String refreshToken) {
		return getSaTokenConfig().getTokenName() + ":oauth2:refresh-token:" + refreshToken;
	}

	/**
	 * 拼接key：Refresh-Token索引
	 * @param clientId 应用id
	 * @param loginId 账号id
	 * @return key
	 */
	default String splicingRefreshTokenIndexKey(String clientId, Object loginId) {
		return getSaTokenConfig().getTokenName() + ":oauth2:refresh-token-index:" + clientId + ":" + loginId;
	}

	/**
	 * 拼接key：Client-Token持久化
	 * @param clientToken clientToken
	 * @return key
	 */
	default String splicingClientTokenSaveKey(String clientToken) {
		return getSaTokenConfig().getTokenName() + ":oauth2:client-token:" + clientToken;
	}

	/**
	 * 拼接key：Client-Token 索引
	 * @param clientId clientId
	 * @return key
	 */
	default String splicingClientTokenIndexKey(String clientId) {
		return getSaTokenConfig().getTokenName() + ":oauth2:client-token-index:" + clientId;
	}

	/**
	 * 拼接key：Lower-Client-Token 索引
	 * @param clientId clientId
	 * @return key
	 */
	default String splicingLowerClientTokenIndexKey(String clientId) {
		return getSaTokenConfig().getTokenName() + ":oauth2:lower-client-token-index:" + clientId;
	}

	/**
	 * 拼接key：用户授权记录
	 * @param clientId 应用id
	 * @param loginId 账号id
	 * @return key
	 */
	default String splicingGrantScopeKey(String clientId, Object loginId) {
		return getSaTokenConfig().getTokenName() + ":oauth2:grant-scope:" + clientId + ":" + loginId;
	}

	/**
	 * 拼接key：state 参数持久化
	 * @param state /
	 * @return key
	 */
	default String splicingStateSaveKey(String state) {
		return getSaTokenConfig().getTokenName() + ":oauth2:state:" + state;
	}

	/**
	 * 拼接key：code-nonce 索引 参数持久化
	 * @param code 授权码
	 * @return key
	 */
	default String splicingCodeNonceIndexSaveKey(String code) {
		return getSaTokenConfig().getTokenName() + ":oauth2:code-nonce-index:" + code;
	}


	// -------- bean 对象代理

	/**
	 * 获取使用的 getSaTokenDao 实例
	 * 
	 * @return /
	 */
	default SaTokenDao getSaTokenDao() {
		return SaManager.getSaTokenDao();
	}

	/**
	 * 获取使用的 SaTokenConfig 实例
	 *
	 * @return /
	 */
	default SaTokenConfig getSaTokenConfig() {
		return SaManager.getConfig();
	}

}
