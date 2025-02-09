package top.belovedyaoo.openac.model;

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
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import top.belovedyaoo.opencore.base.BaseFiled;
import top.belovedyaoo.opencore.tree.Tree;

import java.io.Serializable;

/**
 * 域实体基类
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
public class BaseDomain extends BaseFiled implements Serializable, Tree {

    @ColumnComment("域名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String domainName;

    @ColumnComment("域代码")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String domainCode;

    @ColumnComment("域描述")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String domainDesc;

}
