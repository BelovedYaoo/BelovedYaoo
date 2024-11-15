package top.belovedyaoo.wxapp.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mybatisflex.annotation.Table;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.autotable.annotation.ColumnType;
import com.tangzc.autotable.annotation.mysql.MysqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import top.belovedyaoo.agcore.base.BaseFiled;

/**
 * 轮播列表
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
@Table(value = "swiper_list", dataSource = "primary")
public class SwiperList extends BaseFiled {

    @ColumnNotNull
    @ColumnComment("轮播资源类型")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 10)
    private String type;

    @ColumnNotNull
    @ColumnComment("轮播资源地址")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 255)
    private String url;

}
