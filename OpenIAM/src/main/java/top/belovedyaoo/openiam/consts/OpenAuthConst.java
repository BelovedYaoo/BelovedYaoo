package top.belovedyaoo.openiam.consts;

/**
 * OpenAuth 所有常量
 *
 * @author BelovedYaoo
 * @version 1.1
 */
public class OpenAuthConst {

	/**
	 * 所有API接口 
	 * @author click33 
	 */
	public static final class Api {
		public static String authorize = "/oauth2/authorize";
		public static String token = "/oauth2/token";
		public static String refresh = "/oauth2/refresh";
		public static String revoke = "/oauth2/revoke";
		public static String client_token = "/oauth2/client_token";
		public static String doLogin = "/oauth2/doLogin";
		public static String doConfirm = "/oauth2/doConfirm";
	}
	
	/**
	 * 所有参数名称 
	 * @author click33 
	 */
	public static final class Param {
		public static String response_type = "response_type";
		public static String client_id = "client_id";
		public static String client_secret = "client_secret";
		public static String redirect_uri = "redirect_uri";
		public static String scope = "scope";
		public static String state = "state";
		public static String code = "code";
		public static String token = "token";
		public static String access_token = "access_token";
		public static String refresh_token = "refresh_token";
		public static String client_token = "client_token";
		public static String grant_type = "grant_type";
		public static String username = "username";
		public static String password = "password";
		public static String build_redirect_uri = "build_redirect_uri";
		public static String Authorization = "Authorization";
		public static String nonce = "nonce";
	}

	/**
	 * 所有返回类型 
	 */
	public static final class ResponseType {
		public static String code = "code";
		public static String token = "token";
	}

	/**
	 * 所有 token 类型
	 */
	public static final class TokenType {
		// 全小写
		public static String basic = "basic";
		public static String digest = "digest";
		public static String bearer = "bearer";

		// 首字母大写
		public static String Basic = "Basic";
		public static String Digest = "Digest";
		public static String Bearer = "Bearer";
	}

	/**
	 * 扩展字段
	 */
	public static final class ExtraField {
		public static String openid = "openid";
		public static String userid = "userid";
		public static String id_token = "id_token";
	}


	/** 默认 openid 生成算法中使用的前缀 */
	public static final String OPENID_DEFAULT_DIGEST_PREFIX = "openid_default_digest_prefix";

	/** 表示OK的返回结果 */
	public static final String OK = "ok";

	/** 表示请求没有得到任何有效处理 {msg: "not handle"} */
	public static final String NOT_HANDLE = "{\"msg\": \"not handle\"}";

	/**
	 * 最终权限处理器标识符：在所有权限处理器执行之后，执行此 scope 标识符代表的权限处理器
	 */
	public static final String _FINALLY_WORK_SCOPE = "_FINALLY_WORK_SCOPE";

}
