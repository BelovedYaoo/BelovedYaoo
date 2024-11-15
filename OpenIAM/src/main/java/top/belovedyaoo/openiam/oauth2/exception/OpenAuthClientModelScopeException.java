package top.belovedyaoo.openiam.oauth2.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 一个异常：代表 ClientModel Scope 相关错误
 * 
 * @author BelovedYaoo
 * @version 1.0
 */
@Setter
@Getter
@Accessors(chain = true)
public class OpenAuthClientModelScopeException extends OpenAuthClientModelException {

	/**
	 * 一个异常：代表 ClientModel Scope 相关错误
	 * @param cause 根异常原因
	 */
	public OpenAuthClientModelScopeException(Throwable cause) {
		super(cause);
	}

	/**
	 * 一个异常：代表 ClientModel Scope 相关错误
	 * @param message 异常描述
	 */
	public OpenAuthClientModelScopeException(String message) {
		super(message);
	}

	/**
	 * 具体引起异常的 ClientId 值
	 */
	public String clientId;

	/**
	 * 具体引起异常的 scope 值
	 */
	public String scope;

	/**
	 * 如果 flag==true，则抛出 message 异常
	 * @param flag 标记
	 * @param message 异常信息 
	 * @param code 异常细分码 
	 */
	public static void throwBy(boolean flag, String message, int code) {
		if(flag) {
			throw new OpenAuthClientModelScopeException(message).setCode(code);
		}
	}
	
}
