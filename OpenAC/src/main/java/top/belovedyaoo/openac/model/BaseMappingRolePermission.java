package top.belovedyaoo.openac.model;

import com.fasterxml.jackson.annotation.JsonGetter;
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

import java.io.Serializable;

/**
 * 角色权限关系映射基类
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
public class BaseMappingRolePermission extends BaseFiled implements Serializable {

    @ColumnNotNull
    @ColumnComment("角色的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String roleId;

    @ColumnNotNull
    @ColumnComment("权限的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String permissionId;

}