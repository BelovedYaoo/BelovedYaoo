package top.belovedyaoo.openiam.model.mapping;

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
 * 授权应用用户组关系映射表<p>
 * 负责记录允许登录该授权应用的用户组
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
@Table(value = "mapping_auth_app_user_group", dataSource = "primary")
public class MappingAuthAppUserGroup extends BaseFiled implements Serializable {

    @ColumnNotNull
    @ColumnComment("应用的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String appId;

    @ColumnNotNull
    @ColumnComment("用户组的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String userGroupId;

    @ColumnComment("授权描述")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 256)
    private String desc;

}
