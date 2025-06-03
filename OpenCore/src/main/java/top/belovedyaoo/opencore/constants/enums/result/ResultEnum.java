package top.belovedyaoo.opencore.constants.enums.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import top.belovedyaoo.opencore.result.ResultCode;
import top.belovedyaoo.opencore.result.ResultMessage;

/**
 * 通用结果枚举类
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ResultEnum implements ResultCode, ResultMessage {

    // 通用结果状态码与HTTP状态码保持一致
    SUCCESS(200, "操作成功"),

    FAILED(400, "操作失败");

    private final Integer code;

    private final String message;

}
