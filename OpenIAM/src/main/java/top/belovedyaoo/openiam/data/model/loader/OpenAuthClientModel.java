package top.belovedyaoo.openiam.data.model.loader;

import lombok.Data;
import lombok.experimental.Accessors;
import top.belovedyaoo.openiam.core.OpenAuthManager;
import top.belovedyaoo.openiam.config.ServerConfigurer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Client应用信息 Model
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@Accessors(chain =true)
public class OpenAuthClientModel implements Serializable {

	/**
	 * 应用id
	 */
	public String clientId;

	/**
	 * 应用秘钥
	 */
	public String clientSecret;

	/**
	 * 应用签约的所有权限
	 */
	public List<String> contractScopes;

	/**
	 * 应用允许授权的所有 redirect_uri
	 */
	public List<String> allowRedirectUris;

	/**
	 * 应用允许的所有 grant_type
	 */
	public List<String> allowGrantTypes = new ArrayList<>();

	/** 单独配置此Client：是否在每次 Refresh-Token 刷新 Access-Token 时，产生一个新的 Refresh-Token [默认取全局配置] */
	public Boolean isNewRefresh;

	/** 单独配置此Client：Access-Token 保存的时间(单位秒)  [默认取全局配置] */
	public long accessTokenTimeout;

	/** 单独配置此Client：Refresh-Token 保存的时间(单位秒) [默认取全局配置] */
	public long refreshTokenTimeout;

	/** 单独配置此Client：Client-Token 保存的时间(单位秒) [默认取全局配置] */
	public long clientTokenTimeout;

	/** 单独配置此Client：Lower-Client-Token 保存的时间(单位：秒) [默认取全局配置] */
	public long lowerClientTokenTimeout;


	public OpenAuthClientModel() {
		ServerConfigurer config = OpenAuthManager.getServerConfig();
		this.isNewRefresh = config.getIsNewRefresh();
		this.accessTokenTimeout = config.getAccessTokenTimeout();
		this.refreshTokenTimeout = config.getRefreshTokenTimeout();
		this.clientTokenTimeout = config.getClientTokenTimeout();
		this.lowerClientTokenTimeout = config.getLowerClientTokenTimeout();
	}
	public OpenAuthClientModel(String clientId, String clientSecret, List<String> contractScopes, List<String> allowRedirectUris) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.contractScopes = contractScopes;
		this.allowRedirectUris = allowRedirectUris;
	}

	// 追加方法

	/**
	 * @param scopes 添加应用签约的所有权限
	 * @return 对象自身
	 */
	public OpenAuthClientModel addContractScopes(String... scopes) {
		if(this.contractScopes == null) {
			this.contractScopes = new ArrayList<>();
		}
		this.contractScopes.addAll(Arrays.asList(scopes));
		return this;
	}

	/**
	 * @param redirectUris 添加应用允许授权的所有 redirect_uri
	 * @return 对象自身
	 */
	public OpenAuthClientModel addAllowRedirectUris(String... redirectUris) {
		if(this.allowRedirectUris == null) {
			this.allowRedirectUris = new ArrayList<>();
		}
		this.allowRedirectUris.addAll(Arrays.asList(redirectUris));
		return this;
	}

	/**
	 * @param grantTypes 应用允许的所有 grant_type
	 * @return 对象自身
	 */
	public OpenAuthClientModel addAllowGrantTypes(String... grantTypes) {
		if(this.allowGrantTypes == null) {
			this.allowGrantTypes = new ArrayList<>();
		}
		this.allowGrantTypes.addAll(Arrays.asList(grantTypes));
		return this;
	}


}
