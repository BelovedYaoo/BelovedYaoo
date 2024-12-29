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
import top.belovedyaoo.opencore.base.BaseFiled;

import java.io.Serializable;

/**
 * 权限实体基类
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
public class BasePermission extends BaseFiled implements Serializable {

    @ColumnComment("权限名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String permissionName;
    
    @ColumnComment("权限代码")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String permissionCode;

    @ColumnComment("权限描述")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    private String permissionDesc;

}
