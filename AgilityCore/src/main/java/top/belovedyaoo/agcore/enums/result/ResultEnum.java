package top.belovedyaoo.agcore.enums.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.belovedyaoo.agcore.result.ResultType;

/**
 * 通用结果枚举类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum ResultEnum implements ResultType {

    // 通用状态码应与约定保持一致
    SUCCESS(200, "请求成功"),

    FAILED(400, "请求失败");

    private final Integer code;

    private final String message;

    private String description;

}
