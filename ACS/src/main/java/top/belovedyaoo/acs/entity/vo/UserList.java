package top.belovedyaoo.acs.entity.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 企业微信部门成员对象实体类
 *
 * @author BelovedYaoo
 * @version 1.3
 */
@Data
@Builder
@Getter(onMethod_ = @JsonGetter)
@Accessors(chain = true, fluent = true)
public class UserList {

    /**
     * 推送用户列表
     */
    private String name;
    private String userid;

}
