package top.belovedyaoo.openac.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import top.belovedyaoo.opencore.result.ResultDescription;
import top.belovedyaoo.opencore.result.ResultMessage;

/**
 * 操作权限枚举
 *
 * @author Celrx
 * @version 1.0
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum OperationAccessEnum implements ResultMessage, ResultDescription {

    /**
     * 操作接口
     */
    NO_PERMISSION("权限不足", "你无权限执行此操作");

    private final String message;

    private final String description;

}
