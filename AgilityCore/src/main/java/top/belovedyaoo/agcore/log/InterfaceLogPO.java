package top.belovedyaoo.agcore.log;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mybatisflex.annotation.Table;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.autotable.annotation.ColumnType;
import com.tangzc.autotable.annotation.mysql.MysqlTypeConstant;
import com.tangzc.mybatisflex.annotation.InsertFillData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.belovedyaoo.agcore.base.BaseFiled;

import java.util.Date;

/**
 * (InterfaceLog)表持久化对象
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter(onMethod_ = @JsonGetter)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true, fluent = true)
@Table(value = "log_interface", dataSource = "primary")
public class InterfaceLogPO extends BaseFiled {

    @ColumnNotNull
    @ColumnComment("每条日志记录的操作者ID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    @InsertFillData(OperatorIdAutoFillProcessor.class)
    private String operatorId;

    @ColumnNotNull
    @ColumnComment("每条日志记录的业务类型")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    private String businessTypes;

    @ColumnNotNull
    @ColumnComment("每条日志记录的请求方式")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 10)
    private String requestType;

    @ColumnNotNull
    @ColumnComment("每条日志记录的请求接口路径")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    private String requestUrl;

    @ColumnNotNull
    @ColumnComment("每条日志记录的方法名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    private String methodName;

    @ColumnComment("每条日志记录的请求方法描述")
    @ColumnType(value = MysqlTypeConstant.TEXT)
    private String description;

    @ColumnComment("每条日志记录的方法入参")
    @ColumnType(value = MysqlTypeConstant.TEXT)
    private String params;

    @ColumnComment("每条日志记录的方法执行结果")
    @ColumnType(value = MysqlTypeConstant.MEDIUMTEXT)
    private String result;

    @ColumnNotNull
    @ColumnComment("请求方法开始执行的时间")
    @ColumnType(value = MysqlTypeConstant.DATETIME, length = 3)
    protected Date startTime;

    @ColumnNotNull
    @ColumnComment("请求方法执行完成的时间")
    @ColumnType(value = MysqlTypeConstant.DATETIME, length = 3)
    private Date finishTime;

    @ColumnNotNull
    @ColumnComment("每条日志记录的请求IP地址")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    private String requestIp;

    @ColumnComment("异常情况")
    @ColumnType(value = MysqlTypeConstant.TEXT)
    private String exceptionMessage;

}
