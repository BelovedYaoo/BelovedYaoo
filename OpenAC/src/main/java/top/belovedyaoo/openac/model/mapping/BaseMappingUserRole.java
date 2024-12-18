package top.belovedyaoo.openac.model.mapping;

import com.fasterxml.jackson.annotation.JsonGetter;
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
import top.belovedyaoo.agcore.base.BaseFiled;

import java.io.Serializable;

/**
 * 用户角色关系映射基类
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
public class BaseMappingUserRole extends BaseFiled implements Serializable {

    @ColumnNotNull
    @ColumnComment("用户的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String userId;

    @ColumnNotNull
    @ColumnComment("角色的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String roleId;

}
