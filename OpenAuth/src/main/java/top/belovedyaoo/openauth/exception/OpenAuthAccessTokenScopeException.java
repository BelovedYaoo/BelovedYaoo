package top.belovedyaoo.openauth.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 一个异常：代表 Access-Token Scope 相关错误
 * 
 * @author BelovedYaoo
 * @version 1.0
 */
@Setter
@Getter
@Accessors(chain = true)
public class OpenAuthAccessTokenScopeException extends OpenAuthAccessTokenException {

	/**
	 * 一个异常：代表 Access-Token Scope 相关错误
	 * @param cause 根异常原因
	 */
	public OpenAuthAccessTokenScopeException(Throwable cause) {
		super(cause);
	}

	/**
	 * 一个异常：代表 Access-Token Scope 相关错误
	 * @param message 异常描述
	 */
	public OpenAuthAccessTokenScopeException(String message) {
		super(message);
	}

	/**
	 * 具体引起异常的 Access-Token 值
	 */
	public String accessToken;

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
			throw new OpenAuthAccessTokenScopeException(message).setCode(code);
		}
	}
	
}
