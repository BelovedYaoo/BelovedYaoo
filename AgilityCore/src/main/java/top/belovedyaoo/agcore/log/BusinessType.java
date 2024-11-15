package top.belovedyaoo.agcore.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 业务类型枚举
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum BusinessType {

    /**
     * 新增
     */
    INSERT("INSERT"),

    /**
     * 删除
     */
    DELETE("DELETE"),

    /**
     * 修改
     */
    UPDATE("UPDATE"),

    /**
     * 查看
     */
    SELECT("SELECT"),

    /**
     * 其它
     */
    OTHER("OTHER");

    private String description;

}
