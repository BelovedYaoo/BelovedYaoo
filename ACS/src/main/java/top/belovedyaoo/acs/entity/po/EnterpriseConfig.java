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
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import top.belovedyaoo.opencore.tenant.TenantFiled;

import java.io.Serializable;

/**
 * 企业配置持久化对象
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
@Table(value = "enterprise_config", dataSource = "primary")
public class EnterpriseConfig extends TenantFiled implements Serializable {

    @ColumnComment("企业id(此参数为身份唯一标识，请保密)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 64)
    private String corpId;

    @ColumnComment("自建应用id(此参数为身份唯一标识，请保密)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 64)
    private String agentId;

    @ColumnComment("自建应用secret(此参数为密钥，请保密)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 64)
    private String secret;

    @ColumnComment("高德地图天气api")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 64)
    private String amapKey;

    @ColumnComment("天行数据api密钥(此参数为密钥，请保密)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 64)
    private String tianApiKey;

    @ColumnComment("天气数据来源(为 0 则使用高德数据，为 1 则使用天行数据)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String dataSources;

    @ColumnComment("推送部门ID(此参数用于控制被推送的部门成员)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String departmentId;

    @ColumnComment("推送时间(为 0 则每日早晨推送当日课程与天气，为 1 则每日夜间推送明日课程与天气)")
    @ColumnType(value = MysqlTypeConstant.INT)
    private Integer pushMode;

    @ColumnComment("天气推送位置(使用城市编码，也就是身份证的前6位)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 10)
    private String weatherValue;

    @ColumnComment("图文消息的图片url")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 64)
    private String imgUrl;

    @ColumnComment("卡片消息的跳转url")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 64)
    private String url;

    @ColumnComment("开学日期")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 10)
    private String dateStarting;

    @ColumnComment("放假日期")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 10)
    private String dateEnding;

    @ColumnComment("累计上课天数(此项不可为空)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String classDays;

    @ColumnComment("累计早课天数(此项不可为空)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String morningClassDays;

    @ColumnComment("累计晚课天数(此项不可为空)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String nightClassDays;

    @ColumnComment("累计课程次数(此项不可为空)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String totalClassTimes;

    @ColumnComment("累计专业课程次数(此项不可为空)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String totalSpecializedClassTimes;

    @ColumnComment("调试模式下的部门推送ID，使用此推送部门ID时，不会将课程计入次数")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String debugPushMode;

    @ColumnComment("调试周期(如不启用调试，请置空)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String debugPeriod;

    @ColumnComment("调试星期(如不启用调试，请置空)")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String debugWeek;

    @ColumnComment("当推送异常时，将提示此用户")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String debugUser;

    @ColumnComment("是否开启推送任务")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 5)
    private String isEnabledPush;

    @ColumnComment("推送任务corn表达式")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 25)
    private String pushCorn;

}
