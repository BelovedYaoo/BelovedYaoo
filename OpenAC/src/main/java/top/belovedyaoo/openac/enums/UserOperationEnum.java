package top.belovedyaoo.openac.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import top.belovedyaoo.opencore.result.ResultDescription;
import top.belovedyaoo.opencore.result.ResultMessage;
import top.belovedyaoo.opencore.result.ResultState;

/**
 * 用户操作结果枚举
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum UserOperationEnum implements ResultState, ResultMessage, ResultDescription {

    // 所有通过 BaseUserServiceImpl 服务类执行的操作结果的枚举
    NOT_EXIST_OPEN_ID(false, "用户不存在", "OpenID不存在"),

    UNCONFORMITY_PASSWORD(false, "密码错误", "密码与OpenID不一致"),

    ALREADY_USED_OPEN_ID(false, "OpenID已被使用", "OpenID已存在"),

    ALREADY_USED_EMAIL(false, "邮箱已被使用", "邮箱已存在"),

    ALREADY_USED_PHONE(false, "手机号已被使用", "手机号已存在"),

    UNKNOWN_ERROR(false, "未知错误", "未知异常");

    private final boolean state;

    private final String message;

    private final String description;

}
