package top.belovedyaoo.openac.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.dromara.autotable.annotation.ColumnComment;
import org.dromara.autotable.annotation.ColumnNotNull;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import top.belovedyaoo.agcore.base.BaseFiled;

import java.io.Serializable;

/**
 * 接口实体基类
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
public class BaseInterface extends BaseFiled implements Serializable {

    @ColumnComment("接口名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String interfaceName;

    @ColumnNotNull
    @ColumnComment("接口路径")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 64)
    private String interfacePath;

    @ColumnNotNull
    @ColumnComment("接口请求方式")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 10)
    private String requestType;

    @ColumnComment("接口描述")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String interfaceDesc;

}
