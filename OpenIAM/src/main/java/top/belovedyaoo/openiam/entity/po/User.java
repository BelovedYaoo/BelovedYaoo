package top.belovedyaoo.openiam.entity.po;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mybatisflex.annotation.Table;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.autotable.annotation.ColumnType;
import com.tangzc.autotable.annotation.mysql.MysqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import top.belovedyaoo.agcore.base.BaseFiled;
import top.belovedyaoo.agcore.sensitization.Sensitization;
import top.belovedyaoo.agcore.sensitization.SensitizationType;

import java.io.Serializable;

/**
 * (Account)表持久化对象
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Getter(onMethod_ = @JsonGetter)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true, fluent = true)
@Table(value = "account", dataSource = "primary")
public class User extends BaseFiled implements Serializable {

    /**
     * 登录ID，该ID为用户登录使用
     */
    @ColumnNotNull
    @ColumnComment("用户的登录ID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String openId;

    /**
     * 登录密码，使用哈希散列加密存储
     */
    @ColumnNotNull
    @ColumnComment("用户的登录密码")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * 手机号
     */
    @ColumnComment("用户绑定的手机号")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 11)
    @Sensitization(type = SensitizationType.MOBILE_PHONE)
    private String phone;

    /**
     * 邮箱地址
     */
    @ColumnComment("用户绑定的邮箱")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    @Sensitization(type = SensitizationType.EMAIL, prefixLen = 3, suffixLen = 2)
    private String email;

    /**
     * 昵称
     */
    @ColumnComment("用户的昵称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String nickname;

    /**
     * 头像地址
     */
    @ColumnComment("用户的头像地址")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    private String avatarAddress;

}

