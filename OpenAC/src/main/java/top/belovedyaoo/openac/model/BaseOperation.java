package top.belovedyaoo.openac.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.dromara.autotable.annotation.ColumnComment;
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
 * 操作实体基类
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
public class BaseOperation extends BaseFiled implements Serializable {

    @ColumnComment("操作名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String operationName;

    @ColumnComment("操作代码")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String operationCode;

    @ColumnComment("操作描述")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String operationDesc;

}
