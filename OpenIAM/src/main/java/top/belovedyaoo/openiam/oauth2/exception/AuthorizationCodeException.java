package top.belovedyaoo.openiam.oauth2.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 一个异常：代表 Code 授权码相关错误
 * 
 * @author BelovedYaoo
 * @version 1.0
 */
@Setter
@Getter
@Accessors(chain = true)
public class AuthorizationCodeException extends OpenAuthException {

	/**
	 * 一个异常：代表 Access-Token 相关错误
	 * @param cause 根异常原因
	 */
	public AuthorizationCodeException(Throwable cause) {
		super(cause);
	}

	/**
	 * 一个异常：代表 Access-Token 相关错误
	 * @param message 异常描述
	 */
	public AuthorizationCodeException(String message) {
		super(message);
	}

	/**
	 * 具体引起异常的 code 值
	 */
	public String authorizationCode;

	/**
	 * 如果 flag==true，则抛出 message 异常
	 * @param flag 标记
	 * @param message 异常信息 
	 * @param authorizationCode 引入异常的 code 值
	 * @param code 异常细分码
	 */
	public static void throwBy(boolean flag, String message, String authorizationCode, int code) {
		if(flag) {
			throw new AuthorizationCodeException(message).setAuthorizationCode(authorizationCode).setCode(code);
		}
	}
	
}
