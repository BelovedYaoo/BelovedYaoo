package top.belovedyaoo.openac.model;

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
import top.belovedyaoo.opencore.base.BaseFiled;

import java.io.Serializable;

/**
 * 菜单实体基类
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
public class BaseRouter extends BaseFiled implements Serializable {

    @ColumnNotNull
    @ColumnComment("路由名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String routerName;

    @ColumnNotNull
    @ColumnComment("路由路径")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 64)
    private String routerPath;

    @ColumnNotNull
    @ColumnComment("父路由ID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String parentId;

    @ColumnNotNull
    @ColumnComment("路由类型")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String routerType;

    @ColumnNotNull
    @ColumnComment("路由图标")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String routerIcon;

    @ColumnNotNull
    @ColumnComment("组件路径")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 64)
    private String componentPath;

    @ColumnNotNull
    @ColumnComment("是否隐藏")
    @ColumnType(value = MysqlTypeConstant.BIT, length = 1)
    private Boolean isHidden;

}
