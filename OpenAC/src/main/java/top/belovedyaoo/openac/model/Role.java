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
import top.belovedyaoo.opencore.tree.Tree;

import java.io.Serializable;
import java.util.List;

/**
 * 角色实体类<p>
 * 角色必须有一个明确的归属域，通过TenantID来定义<p>
 * 上级域的角色可以被下级域继承与使用<p>
 * 比如我的顶级域有管理员这个角色<p>
 * 那么下级域虽然本身没有管理员，但是也可以使用管理员<p>
 * 但是管理员这个角色是归属于顶级域的，而不是下级域<p>
 * 因此不同域的角色信息都只有一份，不会冗余定义<p>
 * 但是这样会引入一个问题，那就是不同域的权限是不一样的<p>
 * 比如A域和B域的管理员具有的权限不同<p>
 * 那么此时B域就可以选择重写顶级域的管理员<p>
 * 赋予或删除某些具体权限<p>
 * 重写后的角色会在自己的域中多出一个角色数据<p>
 * 这个多出来的角色也叫管理员<p>
 * 但是是归属B域，继承自顶级域<p>
 * 也就是通过ParentID指向顶级域的管理员角色<p>
 * 通过TenantID指向B域
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
public class Role extends BaseFiled implements Serializable, Tree<Role> {

    @ColumnNotNull
    @ColumnComment("所指向的域的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String domainId;

    @ColumnComment("角色名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String roleName;

    @ColumnComment("角色代码")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String roleCode;

    @ColumnComment("角色描述")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    private String roleDesc;

    @ColumnComment("角色是否对子域可见")
    @ColumnType(value = MysqlTypeConstant.BIT, length = 1)
    private boolean isVisibleToSubDomain;

    @ColumnComment("角色能否被子域分配")
    @ColumnType(value = MysqlTypeConstant.BIT, length = 1)
    private boolean isAssignableToSubDomain;

    @ColumnComment("角色能否被子域重写")
    @ColumnType(value = MysqlTypeConstant.BIT, length = 1)
    private boolean isOverridableToSubDomain;

    @Ignore
    @RelationManyToMany(
            joinTable = "mapping_role_permission",
            joinSelfColumn = "role_id",
            joinTargetColumn = "permission_id"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Permission> permissions;

}
