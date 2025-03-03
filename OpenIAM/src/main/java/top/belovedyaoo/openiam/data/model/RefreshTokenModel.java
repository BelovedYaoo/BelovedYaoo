package top.belovedyaoo.openiam.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Model: Refresh-Token
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class RefreshTokenModel implements Serializable {

	/**
	 * Refresh-Token 值
	 */
	public String refreshToken;

	/**
	 * Refresh-Token 到期时间 
	 */
	public long expiresTime;
	
	/**
	 * 应用id 
	 */
	public String clientId;

	/**
	 * 对应账号id 
	 */
	public Object loginId;

	/**
	 * 授权范围
	 */
	public List<String> scopes;

	/**
	 * 扩展数据
	 */
	public Map<String, Object> extraData;

	/**
	 * 获取：此 Refresh-Token 的剩余有效期（秒）
	 * @return /
	 */
	public long getExpiresIn() {
		long s = (expiresTime - System.currentTimeMillis()) / 1000;
		return s < 1 ? -2 : s;
	}
	
}
