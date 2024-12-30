package top.belovedyaoo.openauth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import top.belovedyaoo.opencore.exception.ExceptionType;

/**
 * OIDC协议 异常枚举
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Getter
@AllArgsConstructor
@Accessors(fluent = true, chain = true)
public enum OidcExceptionEnum implements ExceptionType {

    // OAuth2.0 与 OIDC 协议相关异常
    MISSING_CLIENT_ID(30101, "参数缺失", "缺少 ClientID"),

    MISSING_SCOPE(30102, "参数缺失", "缺少 Scope"),

    MISSING_REDIRECT_URI(30103, "参数缺失", "缺少 RedirectURI"),

    MISSING_LOGIN_ID(30104, "参数缺失", "缺少 LoginID"),

    INVALID_ACCESS_TOKEN(30105, "参数无效", "AccessToken 无效"),

    INVALID_CLIENT_TOKEN(30106, "参数无效", "ClientToken 无效"),

    INVALID_REFRESH_TOKEN(30107, "参数无效", "RefreshToken 无效"),

    INVALID_CLIENT_SECRET(30108, "参数无效", "ClientSecret 无效"),

    INVALID_RESPONSE_TYPE(30109, "参数无效", "ResponseType 无效"),

    INVALID_GRANT_TYPE(30110, "参数无效", "GrantType 无效"),

    INVALID_REDIRECT_URI(30111, "参数无效", "RedirectURI 无效"),

    INVALID_STATE(30112, "参数无效", "State 无效"),

    INVALID_CODE(30113, "参数无效", "Code 无效"),

    INVALID_CLIENT_ID(30114, "参数无效", "ClientID 无效"),

    UNCONFORMITY_CLIENT_ID(30115, "参数无效", "ClientID 不一致"),

    UNCONFORMITY_REDIRECT_URI(30116, "参数无效", "RedirectURI 不一致");

    private final Integer code;

    private final String message;

    private final String description;

}
