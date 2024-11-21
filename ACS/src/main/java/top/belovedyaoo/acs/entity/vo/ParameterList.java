package top.belovedyaoo.acs.entity.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 推送参数实体类
 *
 * @author BelovedYaoo
 * @version 1.3
 */
@Data
@Builder
@Getter(onMethod_ = @JsonGetter)
@Accessors(chain = true, fluent = true)
public class ParameterList {

    /**
     * 天气参数
     */
    private Weather weather;

    /**
     * 彩虹屁
     */
    private String caiHongPi;

    /**
     * 计算距离放假天数
     */
    private Integer dateEnding;

    /**
     * 计算开学天数
     */
    private Integer dateStarting;

}
