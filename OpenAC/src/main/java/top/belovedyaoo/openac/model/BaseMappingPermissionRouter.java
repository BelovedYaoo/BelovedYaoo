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
 * 权限路由关系映射基类
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
public class BaseMappingPermissionRouter extends BaseFiled implements Serializable {

    @ColumnNotNull
    @ColumnComment("权限的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String permissionId;

    @ColumnNotNull
    @ColumnComment("路由的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String routerId;

}
