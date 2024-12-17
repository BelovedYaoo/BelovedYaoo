package top.belovedyaoo.agcore.enums.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.belovedyaoo.agcore.result.ResultType;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum AuthEnum implements ResultType {

    // 认证与授权状态码
    SESSION_INVALID(700, "会话失效");

    private final Integer code;

    private final String message;

    private String description;

}
