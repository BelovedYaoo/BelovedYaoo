package top.belovedyaoo.openac.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import top.belovedyaoo.opencore.base.BaseIdFiled;

import java.io.Serializable;

/**
 * 操作实体类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Getter(onMethod_ = @JsonGetter)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true, fluent = true)
public class Operation extends BaseIdFiled implements Serializable {

    public static final String OPERATION_ID = "operation_id";

    /**
     * 操作名称
     */
    private String operationName;

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 控制器类
     */
    private String className;

    /**
     * 操作方法名称
     */
    private String operationMethodName;

    /**
     * 操作代码
     */
    private String operationCode;

    /**
     * 操作描述
     */
    private String operationDesc;

}
