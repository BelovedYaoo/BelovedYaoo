package top.belovedyaoo.openac.model.mapping;

import com.fasterxml.jackson.annotation.JsonGetter;
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
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import top.belovedyaoo.opencore.base.BaseFiled;

import java.io.Serializable;

/**
 * 用户组用户关系映射类
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
@Table(value = "mapping_user_group_user", dataSource = "primary")
public class MappingUserGroupUser extends BaseFiled implements Serializable {

    @ColumnNotNull
    @ColumnComment("用户组的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String userGroupId;

    @ColumnNotNull
    @ColumnComment("用户的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String userId;

}
