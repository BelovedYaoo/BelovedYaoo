package top.belovedyaoo.opencore.base;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mybatisflex.annotation.Column;
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

import java.io.Serializable;

/**
 * 多租户的基础字段
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
public abstract class BaseTenantFiled extends BaseFiled implements Serializable {

    public final static String TENANT_ID = "tenant_id";

    @ColumnNotNull
    @Column(tenantId = true)
    @ColumnComment("多租户ID,其值通常为其他用户数据的BaseID")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String tenantId;

}
