package top.belovedyaoo.openiam.oauth2.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * Model: 授权码
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CodeModel implements Serializable {

	/**
	 * 授权码 
	 */
	public String code;
	
	/**
	 * 应用id 
	 */
	public String clientId;
	
	/**
	 * 授权范围
	 */
	public List<String> scopes;

	/**
	 * 对应账号id 
	 */
	public Object loginId;

	/**
	 * 重定向的地址 
	 */
	public String redirectUri;

	/**
	 * 随机数
	 */
	public String nonce;

}
