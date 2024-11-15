package top.belovedyaoo.openiam.oauth2.exception;

import cn.dev33.satoken.exception.SaTokenException;

/**
 * 一个异常：代表 OAuth2 认证流程错误
 * 
 * @author BelovedYaoo
 * @version 1.0
 */
public class OpenAuthException extends SaTokenException {

	/**
	 * 一个异常：代表 OAuth2 认证流程错误
	 * @param cause 根异常原因
	 */
	public OpenAuthException(Throwable cause) {
		super(cause);
	}

	/**
	 * 一个异常：代表 OAuth2 认证流程错误
	 * @param message 异常描述 
	 */
	public OpenAuthException(String message) {
		super(message);
	}

	/**
	 * 如果 flag==true，则抛出 message 异常
	 * @param flag 标记
	 * @param message 异常信息 
	 * @param code 异常细分码 
	 */
	public static void throwBy(boolean flag, String message, int code) {
		if(flag) {
			throw new OpenAuthException(message).setCode(code);
		}
	}
	
}
