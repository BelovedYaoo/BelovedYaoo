package top.belovedyaoo.openiam.entity.po;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import top.belovedyaoo.openac.model.BaseUser;

import java.io.Serializable;

/**
 * (User)表持久化对象
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
public class User extends BaseUser implements Serializable {

}

