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
 * 域用户角色关系映射类<p>
 * 用于存储 某一个域所拥有的用户信息 以及 某一个用户在某一个域中所拥有的角色信息<p>
 * 若要查询某域具有哪些角色，则直接通过Role表中的 domainID 查询<p>
 * 输入：域ID，输出：用户ID<p>
 * 输入：用户ID、域ID，输出：角色ID
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
@Table(value = "mapping_domain_user_role", dataSource = "primary")
public class MappingDomainUserRole extends BaseFiled implements Serializable {

    @ColumnNotNull
    @ColumnComment("域的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String domainId;

    @ColumnNotNull
    @ColumnComment("用户的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String userId;

    @ColumnNotNull
    @ColumnComment("角色的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String roleId;

}
