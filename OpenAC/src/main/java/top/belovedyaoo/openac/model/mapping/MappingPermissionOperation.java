package top.belovedyaoo.openac.model.mapping;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mybatisflex.annotation.Table;
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
import top.belovedyaoo.opencore.base.BaseFiled;

import java.io.Serializable;

/**
 * 权限操作关系映射类
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
@Table(value = "mapping_permission_operation", dataSource = "primary")
public class MappingPermissionOperation extends BaseFiled implements Serializable {

    @ColumnNotNull
    @ColumnComment("权限的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String permissionId;

    @ColumnNotNull
    @ColumnComment("操作的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String operationId;

}
