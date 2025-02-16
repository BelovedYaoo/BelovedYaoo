package top.belovedyaoo.openac.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import org.dromara.autotable.annotation.ColumnComment;
import org.dromara.autotable.annotation.ColumnNotNull;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Ignore;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import top.belovedyaoo.opencore.base.BaseFiled;
import top.belovedyaoo.opencore.sensitization.Sensitization;
import top.belovedyaoo.opencore.sensitization.SensitizationType;

import java.io.Serializable;
import java.util.List;

/**
 * 用户实体类
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
@Table(value = "user", dataSource = "primary")
public class User extends BaseFiled implements Serializable {

    public static final String OPEN_ID = "open_id";

    public static final String PASSWORD = "password";

    public static final String PHONE = "phone";

    public static final String EMAIL = "email";

    public static final String NICKNAME = "nickname";

    public static final String AVATAR_ADDRESS = "avatar_address";

    /**
     * 该ID为用户登录使用
     */
    @ColumnNotNull
    @ColumnComment("用户的登录ID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String openId;

    /**
     * 使用哈希散列加密存储
     */
    @ColumnNotNull
    @ColumnComment("用户的登录密码")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ColumnComment("用户绑定的手机号")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 11)
    @Sensitization(type = SensitizationType.MOBILE_PHONE)
    private String phone;

    @ColumnComment("用户绑定的邮箱")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    @Sensitization(type = SensitizationType.EMAIL, prefixLen = 3, suffixLen = 2)
    private String email;

    @ColumnComment("用户的昵称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String nickname;

    @ColumnComment("用户的头像地址")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 50)
    private String avatarAddress;

    @Ignore
    @RelationManyToMany(
            joinTable = "mapping_user_role",
            joinSelfColumn = "user_id",
            joinTargetColumn = "role_id"
    )
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Role> roles;

}

