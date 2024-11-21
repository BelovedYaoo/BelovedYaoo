package top.belovedyaoo.openiam.entity.po;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mybatisflex.annotation.Table;
import com.tangzc.autotable.annotation.ColumnComment;
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

import java.io.Serializable;

/**
 * (Permission)表持久化对象
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
@Table(value = "permission", dataSource = "primary")
public class Permission extends BaseFiled implements Serializable {

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
