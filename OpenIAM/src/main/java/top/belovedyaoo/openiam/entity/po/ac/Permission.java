package top.belovedyaoo.openiam.entity.po.ac;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import org.dromara.autotable.annotation.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import top.belovedyaoo.openac.model.BasePermission;

import java.io.Serializable;
import java.util.List;

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
public class Permission extends BasePermission implements Serializable {

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
