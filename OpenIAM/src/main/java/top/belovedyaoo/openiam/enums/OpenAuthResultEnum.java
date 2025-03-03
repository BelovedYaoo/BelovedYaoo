package top.belovedyaoo.openiam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import top.belovedyaoo.opencore.result.ResultCode;
import top.belovedyaoo.opencore.result.ResultDescription;
import top.belovedyaoo.opencore.result.ResultMessage;

/**
 * OpenAuth 服务
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum OpenAuthResultEnum implements ResultCode, ResultMessage, ResultDescription {

    // OpenAuth 的异常状态码均以 9 开头
    NEED_LOGIN(900, "未登录", "请先登录再进行操作"),

    NEED_CONFIRM(901, "需要授权", "请先授权"),

    ACCESS_DENIED(902, "权限不足", "请联系管理员");

    private final Integer code;

    private final String message;

    private final String description;

}
