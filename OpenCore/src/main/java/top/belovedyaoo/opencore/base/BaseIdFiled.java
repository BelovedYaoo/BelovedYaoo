package top.belovedyaoo.opencore.base;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mybatisflex.annotation.Id;
import com.tangzc.mybatisflex.annotation.InsertFillData;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.dromara.autotable.annotation.ColumnComment;
import org.dromara.autotable.annotation.ColumnNotNull;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.PrimaryKey;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import top.belovedyaoo.opencore.processor.BaseIdAutoFillProcessor;

import java.io.Serializable;

/**
 * 基础ID字段
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Getter(onMethod_ = @JsonGetter)
@Accessors(fluent = true, chain = true)
public abstract class BaseIdFiled implements Serializable {

    public static final String BASE_ID = "base_id";

    @Id
    @ColumnNotNull
    @PrimaryKey(autoIncrement = false)
    @ColumnComment("基础ID,仅系统内部使用")
    @Index(type = IndexTypeEnum.UNIQUE)
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    @InsertFillData(BaseIdAutoFillProcessor.class)
    private String baseId;

    /**
     * 获取BaseIdFiled的BaseID字段的查询条件
     *
     * @param id BaseID字段
     *
     * @return BaseID字段的查询条件
     */
    public static String eqBaseId(String id) {
        return BaseIdFiled.BASE_ID + " = '" + id + "'";
    }

    /**
     * 读取对象中的BaseID<p>
     *
     * @param object 需要读取的对象
     *
     * @return 对象中的BaseID
     */
    public static String readBaseId(Object object) {
        if (object instanceof BaseIdFiled baseIdFiled) {
            return baseIdFiled.baseId();
        } else {
            throw new IllegalArgumentException("传入的参数类型不是BaseIdFiled的派生类,请检查");
        }
    }

}
