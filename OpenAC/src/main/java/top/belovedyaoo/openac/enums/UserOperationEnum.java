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

    // 所有通过 BaseUserService 服务类执行的操作结果的枚举
    NOT_EXIST_OPEN_ID(false, "用户不存在","OpenID不存在"),

    UNCONFORMITY_PASSWORD(false, "密码错误", "密码错误"),;

    private final boolean state;

    private final String message;

    private final String description;

}
