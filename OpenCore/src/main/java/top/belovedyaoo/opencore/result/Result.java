package top.belovedyaoo.opencore.result;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import top.belovedyaoo.opencore.constants.enums.result.ResultEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回结果统一封装类
 *
 * @author BelovedYaoo
 * @version 1.6
 */
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Getter(onMethod_ = @JsonGetter)
@Accessors(chain = true, fluent = true)
public class Result {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 状态
     */
    private Boolean state;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息描述
     */
    private String description;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 返回数据缓存
     */
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private Map<String, Object> dataCache = new HashMap<>();

    /**
     * 成功方法
     */
    public static Result success() {
        Result result = new Result();
        result.resultType(ResultEnum.SUCCESS);
        return result;
    }

    /**
     * 失败方法
     */
    public static Result failed() {
        Result result = new Result();
        result.resultType(ResultEnum.FAILED);
        return result;
    }

    /**
     * 添加单一返回数据,无需再封装一层<p>
     * 添加单一数据后不能再添加键值对或Map数据,否则单一数据会被覆盖
     *
     * @param object 简单数据
     *
     * @return 数据体
     */
    public Result singleData(Object object) {
        this.data = object;
        return this;
    }

    /**
     * 添加键值对返回数据
     *
     * @param key   键
     * @param value 值
     *
     * @return 数据体
     */
    public Result data(String key, Object value) {
        dataCache.put(key, value);
        data = dataCache;
        return this;
    }

    /**
     * 将一个map添加到返回数据中
     *
     * @param map 被添加的map
     *
     * @return 数据体
     */
    public Result data(Map<String, Object> map) {
        dataCache.putAll(map);
        data = dataCache;
        return this;
    }

    @JsonGetter(value = "data")
    public Object data() {
        return data;
    }

    public Result resultType(ResultType resultEnum) {
        if (resultEnum instanceof ResultCode resultCode) {
            code(resultCode.code());
        }
        if (resultEnum instanceof ResultMessage resultMessage) {
            message(resultMessage.message());
        }
        if (resultEnum instanceof ResultDescription resultDescription) {
            description(resultDescription.description());
        }
        if (resultEnum instanceof ResultState resultState) {
            state(resultState.state());
        }
        return this;
    }

    public static Result tryConvert(Object obj) {
        if (obj instanceof Result result) {
            return result;
        } else {
            return new Result();
        }
    }

    @Override
    public String toString() {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("{");
        if (code != null) {
            resultBuilder.append("\"code\": ").append(code);
        }
        if (state != null) {
            resultBuilder.append(", ").append("\"state\": ").append(state);
        }
        if (message != null) {
            resultBuilder.append(", ").append("\"message\": ").append(transValue(message));
        }
        if (description != null) {
            resultBuilder.append(", ").append("\"description\": ").append(transValue(description));
        }
        if (data != null) {
            resultBuilder.append(", ").append("\"data\": ").append(data());
        }
        resultBuilder.append("}");
        return resultBuilder.toString();
    }

    private String transValue(Object value) {
        if (value == null) {
            return null;
        } else {
            return value instanceof String ? "\"" + value + "\"" : String.valueOf(value);
        }
    }

}
