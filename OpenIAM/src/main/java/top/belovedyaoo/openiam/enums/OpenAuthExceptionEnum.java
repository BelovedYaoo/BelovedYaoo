package top.belovedyaoo.openiam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import top.belovedyaoo.opencore.exception.ExceptionType;

/**
 * OpenAuth 异常枚举
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Getter
@AllArgsConstructor
@Accessors(fluent = true, chain = true)
public enum OpenAuthExceptionEnum implements ExceptionType {

    // OpenAuth 异常
    PERMISSION_DENIED(30000, "权限不足", null),

    NOT_SUPPORTED_AUTH_MODEL(30001, "不支持的认证模式", null),

    INVALID_REQUEST_METHOD(30002, "不支持的请求方式", null),

    INVALID_PARAMETER(30003, "参数无效", null),

    MISSING_PARAMETER(30004, "参数缺失", null);

    private final Integer code;

    private final String message;

    private final String description;

}
