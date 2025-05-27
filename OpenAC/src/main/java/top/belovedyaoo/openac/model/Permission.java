package top.belovedyaoo.openac.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import org.dromara.autotable.annotation.ColumnComment;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Ignore;
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
import java.util.List;

/**
 * 权限实体类
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

    public static final String PERMISSION_ID = "permission_id";

    public static final String PERMISSION_NAME = "permission_name";

    public static final String PERMISSION_CODE = "permission_code";

    public static final String PERMISSION_DESC = "permission_desc";

    @ColumnComment("权限名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String permissionName;
    
    @ColumnComment("权限代码")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String permissionCode;

    @ColumnComment("权限描述")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    private String permissionDesc;

    @Ignore
    @RelationManyToMany(
            joinTable = "mapping_permission_router",
            joinSelfColumn = "permission_id",
            joinTargetColumn = "router_id"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Router> routers;

    @Ignore
    @RelationManyToMany(
            joinTable = "mapping_permission_operation",
            joinSelfColumn = "permission_id",
            joinTargetColumn = "operation_id"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Operation> operations;

}
