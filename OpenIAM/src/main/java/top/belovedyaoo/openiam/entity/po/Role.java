package top.belovedyaoo.openiam.entity.po;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import com.tangzc.autotable.annotation.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import top.belovedyaoo.openac.model.BaseRole;

import java.io.Serializable;
import java.util.List;

/**
 * (Role)表持久化对象
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
@Table(value = "role", dataSource = "primary")
public class Role extends BaseRole implements Serializable {

    @Ignore
    @RelationManyToMany(
            joinTable = "mapping_role_permission",
            joinSelfColumn = "role_id",
            joinTargetColumn = "permission_id"
    )
    private List<Permission> permissions;

}
