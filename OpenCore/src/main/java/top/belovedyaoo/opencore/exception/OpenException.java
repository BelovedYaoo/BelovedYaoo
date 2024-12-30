package top.belovedyaoo.opencore.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用异常
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class OpenException extends RuntimeException {

    /**
     * 异常码
     */
    private Integer code;

    /**
     * 异常描述
     */
    private String description;

    /**
     * 异常数据
     */
    private List<Object> data = new ArrayList<>();

    /**
     * 构造函数：仅包含根异常
     *
     * @param cause 根异常
     */
    public OpenException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数：包含异常消息
     *
     * @param message 异常内容
     */
    public OpenException(String message) {
        super(message);
    }

    /**
     * 构造函数：包含异常类型
     *
     * @param exceptionType 异常类型
     */
    public OpenException(ExceptionType exceptionType) {
        super(exceptionType.message());
        this.code(exceptionType.code())
                .description(exceptionType.description());
    }

    /**
     * 添加单个数据项到异常数据列表中
     *
     * @param data 数据项
     * @return 当前异常对象
     */
    public OpenException data(Object data) {
        if (data != null) {
            this.data.add(data);
        }
        return this;
    }

    /**
     * 添加多个数据项到异常数据列表中
     *
     * @param data 数据项数组
     * @return 当前异常对象
     */
    public OpenException data(List<Object> data) {
        if (data != null) {
            this.data.addAll(data);
        }
        return this;
    }

    /**
     * 如果 flag 为 True，则抛出异常
     *
     * @param flag    标记
     * @param message 异常内容
     * @param code    异常码
     */
    public static void throwBy(boolean flag, String message, int code) {
        if (flag) {
            throw new OpenException(message).code(code);
        }
    }

    /**
     * 如果 flag 为 True，则抛出异常
     *
     * @param flag          标记
     * @param exceptionType 异常类型
     */
    public static void throwBy(boolean flag, ExceptionType exceptionType) {
        if (flag) {
            throw new OpenException(exceptionType);
        }
    }

    /**
     * 如果 flag 为 True，则抛出异常
     *
     * @param flag          标记
     * @param exceptionType 异常类型
     * @param data          异常数据
     */
    public static void throwBy(boolean flag, ExceptionType exceptionType, Object... data) {
        if (flag) {
            throw new OpenException(exceptionType).data(Arrays.asList(data));
        }
    }

    /**
     * 如果 flag 为 True，则抛出异常
     *
     * @param flag          标记
     * @param exceptionType 异常类型
     * @param description   异常描述
     * @param data          异常数据
     */
    public static void throwBy(boolean flag, ExceptionType exceptionType, String description, Object... data) {
        if (flag) {
            throw new OpenException(exceptionType).description(description).data(Arrays.asList(data));
        }
    }

    /**
     * 如果 flag 为 True，则抛出异常
     *
     * @param flag    标记
     * @param message 异常内容
     * @param code    异常码
     * @param data    异常数据
     */
    public static void throwBy(boolean flag, String message, int code, Object... data) {
        if (flag) {
            throw new OpenException(message).code(code).data(Arrays.asList(data));
        }
    }

    /**
     * 如果 flag 为 True，则抛出异常
     *
     * @param flag    标记
     * @param message 异常内容
     * @param code    异常码
     * @param description 异常描述
     * @param data    异常数据
     */
    public static void throwBy(boolean flag, String message, int code, String description, Object... data) {
        if (flag) {
            throw new OpenException(message).code(code).description(description).data(Arrays.asList(data));
        }
    }

}