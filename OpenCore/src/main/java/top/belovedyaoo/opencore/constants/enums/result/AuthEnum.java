package top.belovedyaoo.opencore.constants.enums.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import top.belovedyaoo.opencore.result.ResultCode;
import top.belovedyaoo.opencore.result.ResultMessage;

/**
 * 认证与授权状态码
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum AuthEnum implements ResultCode, ResultMessage {

    // 认证与授权状态码
    SESSION_INVALID(700, "会话失效");

    private final Integer code;

    private final String message;

}
