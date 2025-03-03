package top.belovedyaoo.openac.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.dromara.autotable.annotation.ColumnComment;
import org.dromara.autotable.annotation.ColumnNotNull;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Ignore;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import top.belovedyaoo.opencore.base.BaseFiled;

import java.io.Serializable;
import java.util.List;

/**
 * 用户组实体类
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
@Table(value = "user_group", dataSource = "primary")
public class UserGroup extends BaseFiled implements Serializable {

    @ColumnNotNull
    @ColumnComment("用户组名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String userGroupName;

    @ColumnNotNull
    @ColumnComment("用户组代码")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String userGroupCode;

    @ColumnComment("用户组描述")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 128)
    private String userGroupDesc;

    @Ignore
    @RelationManyToMany(
            joinTable = "mapping_user_group_user",
            joinSelfColumn = "userGroup_id",
            joinTargetColumn = "user_id"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<User> users;
    
}
