package top.belovedyaoo.openiam.entity.po.ac;

import com.fasterxml.jackson.annotation.JsonGetter;
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
import top.belovedyaoo.openac.model.BaseUser;

import java.io.Serializable;
import java.util.List;

/**
 * (User)表持久化对象
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Getter(onMethod_ = @JsonGetter)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true, fluent = true)
@Table(value = "user", dataSource = "primary")
public class User extends BaseUser implements Serializable {

    @Ignore
    @RelationManyToMany(
            joinTable = "mapping_user_role",
            joinSelfColumn = "user_id",
            joinTargetColumn = "role_id"
    )
    private List<Role> roles;

}

