package top.belovedyaoo.openac.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.RelationOneToMany;
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
 * 域实体类
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
@Table(value = "domain", dataSource = "primary")
public class Domain extends BaseFiled implements Serializable, Tree {

    @ColumnNotNull
    @ColumnComment("域名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String domainName;

    @ColumnNotNull
    @ColumnComment("域代码")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String domainCode;

    @ColumnComment("域描述")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 128)
    private String domainDesc;

    @Ignore
    @RelationOneToMany(targetField = "parentId")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Domain> domains;

}
