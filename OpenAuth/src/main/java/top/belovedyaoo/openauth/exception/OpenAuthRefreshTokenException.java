package top.belovedyaoo.openauth.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 一个异常：代表 Refresh-Token 相关错误
 * 
 * @author BelovedYaoo
 * @version 1.0
 */
@Setter
@Getter
@Accessors(chain = true)
public class OpenAuthRefreshTokenException extends OpenAuthException {

	/**
	 * 一个异常：代表 Refresh-Token 相关错误
	 * @param cause 根异常原因
	 */
	public OpenAuthRefreshTokenException(Throwable cause) {
		super(cause);
	}

	/**
	 * 一个异常：代表 Refresh-Token 相关错误
	 * @param message 异常描述
	 */
	public OpenAuthRefreshTokenException(String message) {
		super(message);
	}

	/**
	 * 具体引起异常的 Refresh-Token 值
	 */
	public String refreshToken;

	/**
	 * 如果 flag==true，则抛出 message 异常
	 * @param flag 标记
	 * @param message 异常信息 
	 * @param code 异常细分码 
	 */
	public static void throwBy(boolean flag, String message, int code) {
		if(flag) {
			throw new OpenAuthRefreshTokenException(message).setCode(code);
		}
	}

	/**
	 * 如果 flag==true，则抛出 message 异常
	 * @param flag 标记
	 * @param message 异常信息
	 * @param refreshToken refreshToken
	 * @param code 异常细分码
	 */
	public static void throwBy(boolean flag, String message, String refreshToken, int code) {
		if(flag) {
			throw new OpenAuthRefreshTokenException(message).setRefreshToken(refreshToken).setCode(code);
		}
	}

}
