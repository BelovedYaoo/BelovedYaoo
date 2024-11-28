package top.belovedyaoo.openauth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.belovedyaoo.agcore.result.ResultType;

/**
 * OpenAuth 服务
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum OpenAuthResultEnum implements ResultType {

    // 所有 OpenAuth 服务的状态码应以 9 开头
    NEED_LOGIN(900, "未登录", "请先登录再进行操作"),

    NEED_CONFIRM(901, "需要授权", "请先授权");

    private final Integer code;

    private final String message;

    private final String description;

}
