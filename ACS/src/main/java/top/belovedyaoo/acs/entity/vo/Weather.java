package top.belovedyaoo.acs.entity.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 天气参数实体类
 *
 * @author BelovedYaoo
 * @version 1.3
 */
@Data
@Builder
@Getter(onMethod_ = @JsonGetter)
@Accessors(chain = true, fluent = true)
public class Weather {

    /**
     * 返回码
     */
    private String infoCode;

    /**
     * 是今日天气还是明日天气
     */
    private int state;

    /**
     * 日期
     */
    private String date;

    /**
     * 当前天气
     */
    private String weather;

    /**
     * 最低温度
     */
    private String lowest;

    /**
     * 最高温度
     */
    private String highest;

    /**
     * 当前天气预报地区
     */
    private String area;

}
