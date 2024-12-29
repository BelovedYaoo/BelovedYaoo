package top.belovedyaoo.openiam.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.belovedyaoo.opencore.result.ResultType;

/**
 * 认证结果枚举类
 *
 * @author BelovedYaoo
 * @version 1.2
 */
@Getter
@AllArgsConstructor
public enum AuthenticationResultEnum implements ResultType {

    // 所有认证相关的错误码均应以 7 开头
    ACCOUNT_LOGIN_ID_INVALID(701, "登录失败", "账号不存在"),

    ACCOUNT_PASSWORD_ERROR(702, "登录失败", "密码错误"),

    ACCOUNT_BANNED(703, "登录失败", "账号被封禁"),

    CODE_VERIFY_ERROR(704, "验证失败", "验证码错误"),

    LOGIN_ID_ALREADY_USE(705, "注册失败", "账号ID已被使用"),

    PHONE_NUMBER_ALREADY_USE(706, "注册失败", "手机号已被使用"),

    EMAIL_ALREADY_ALREADY_USE(707, "注册失败", "邮箱已被使用"),

    MUST_USE_PHONE_OR_EMAIL(708, "注册失败", "必须使用手机号或邮箱绑定账号");

    private final Integer code;

    private final String message;

    private final String description;

}
