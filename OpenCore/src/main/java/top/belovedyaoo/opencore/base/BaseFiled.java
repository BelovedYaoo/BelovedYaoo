package top.belovedyaoo.opencore.base;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.tangzc.mybatisflex.annotation.InsertFillData;
import com.tangzc.mybatisflex.annotation.InsertFillTime;
import com.tangzc.mybatisflex.annotation.InsertUpdateFillData;
import com.tangzc.mybatisflex.annotation.InsertUpdateFillTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.dromara.autotable.annotation.ColumnComment;
import org.dromara.autotable.annotation.ColumnNotNull;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Ignore;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.PrimaryKey;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import top.belovedyaoo.opencore.processor.BaseIdAutoFillProcessor;
import top.belovedyaoo.opencore.processor.CreatorIdAutoFillProcessor;
import top.belovedyaoo.opencore.processor.UpdaterIdAutoFillProcessor;
import top.belovedyaoo.opencore.tree.Tree;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础字段<p>
 * Data注解用于提供各属性的Getter、Setter、equals、hashCode、canEqual、toString方法<p>
 * SuperBuilder注解用于协同构造派生类,缺失会导致构造派生类时无法同时设置基类与派生类属性<p>
 * Getter注解用于Jackson的反序列化,缺失会导致拿不到BaseFiled的属性<p>
 * NoArgsConstructor注解用于提供无参构造方法,缺失会导致派生类中报错<p>
 * Accessors用于去除Getter、Setter前缀并开启链式调用,使Getter、Setter返回当前对象
 *
 * @author BelovedYaoo
 * @version 2.1
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Getter(onMethod_ = @JsonGetter)
@Accessors(fluent = true, chain = true)
public abstract class BaseFiled implements Serializable {

    public static final String BASE_ID = "base_id";

    public static final String ORDER_NUM = "order_num";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DISABLED_AT = "disabled_at";

    public static final String DELETED_AT = "deleted_at";

    @Id
    @ColumnNotNull
    @PrimaryKey(autoIncrement = false)
    @ColumnComment("基础ID,仅系统内部使用")
    @Index(type = IndexTypeEnum.UNIQUE)
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    @InsertFillData(BaseIdAutoFillProcessor.class)
    private String baseId;

    @ColumnComment("数据序号,用于数据排序")
    @ColumnType(value = MysqlTypeConstant.INT)
    private Integer orderNum;

    @ColumnComment("数据的创建时间")
    @ColumnType(value = MysqlTypeConstant.DATETIME, length = 3)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @InsertFillTime
    private Date createTime;

    @ColumnComment("数据最近一次的更新时间")
    @ColumnType(value = MysqlTypeConstant.DATETIME, length = 3)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @InsertUpdateFillTime
    private Date updateTime;

    @ColumnComment("数据创建者的BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    @InsertFillData(CreatorIdAutoFillProcessor.class)
    private String creatorId;

    @ColumnComment("数据上一次更新者BaseID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    @InsertUpdateFillData(UpdaterIdAutoFillProcessor.class)
    private String updaterId;

    @ColumnComment("不为NULL的情况表示数据的禁用时间")
    @ColumnType(value = MysqlTypeConstant.DATETIME, length = 3)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date disabledAt;

    @Column(isLogicDelete = true)
    @ColumnComment("不为NULL的情况表示数据的删除时间")
    @ColumnType(value = MysqlTypeConstant.DATETIME, length = 3)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date deletedAt;

    /**
     * 树节点数据<br>
     * Ignore注解用于忽略AutoTable的表结构自动维护<br>
     * Column注解用于忽略MyBatis-Flex的列定义<br>
     * Getter注解用于忽略Jackson的序列化与反序列化
     */
    @Ignore
    @Column(ignore = true)
    @Getter(onMethod_ = @JsonIgnore)
    private final Tree.TreeNode treeNode = new Tree.TreeNode();

    /**
     * 派生类方法在调用基类Setter时会传递基类类型对象<p>
     * 此方法用于将基类类型对象转换为指定的派生类类型对象<p>
     * 请通过上下文确保类型一致
     *
     * @param clazz 需要转换的派生类类型
     * @param <T>   派生类类型
     *
     * @return 转换后的派生类类型对象
     */
    public <T extends BaseFiled> T convertTo(Class<T> clazz) {
        if (clazz.isInstance(this)) {
            return clazz.cast(this);
        } else {
            throw new IllegalArgumentException(clazz.getSimpleName() + "与预期的类型不一致,请检查");
        }
    }

    /**
     * 将对象转换为BaseFiled类型<p>
     * 注意会丢失派生类类型信息
     *
     * @param object 需要转换的对象
     *
     * @return 转换后的BaseFiled类型对象
     */
    public static BaseFiled convertToBaseFiled(Object object) {
        if (object instanceof BaseFiled) {
            return (BaseFiled) object;
        } else {
            throw new IllegalArgumentException("传入的参数类型不是BaseFiled的基类,请检查");
        }
    }

    /**
     * 判断是否禁用<br>
     * 由于命名刚好是标准Bean规范，需要加上Jackson的注解忽略序列化与反序列化
     *
     * @return 数据的禁用状态
     */
    @JsonIgnore
    public boolean isDisabled() {
        return this.disabledAt != null;
    }

    /**
     * 禁用
     */
    public void ban() {
        this.disabledAt = new Date();
    }

    /**
     * 解禁
     */
    public void unban() {
        this.disabledAt = null;
    }

}
