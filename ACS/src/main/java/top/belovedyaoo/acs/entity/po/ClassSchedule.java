package top.belovedyaoo.acs.entity.po;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.dromara.autotable.annotation.ColumnComment;
import org.dromara.autotable.annotation.ColumnNotNull;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import top.belovedyaoo.agcore.base.BaseTenantFiled;

import java.io.Serializable;

/**
 * 课程表持久化对象
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
@Table(value = "class_schedule", dataSource = "primary")
public class ClassSchedule extends BaseTenantFiled implements Serializable {

    @ColumnNotNull
    @ColumnComment("课程名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String courseName;

    @ColumnNotNull
    @ColumnComment("上课地点")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String courseVenue;

    @ColumnComment("课程类型")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 10)
    private String courseType;

    @ColumnNotNull
    @ColumnComment("课程周期")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 10)
    private String schedulePeriod;

    @ColumnNotNull
    @ColumnComment("课程星期")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 10)
    private String scheduleWeek;

    @ColumnNotNull
    @ColumnComment("课程节次")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 10)
    private String scheduleSection;

}
