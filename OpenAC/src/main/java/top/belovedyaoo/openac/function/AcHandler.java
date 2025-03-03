package top.belovedyaoo.openac.function;

import cn.dev33.satoken.stp.StpUtil;
import top.belovedyaoo.openac.model.Role;
import top.belovedyaoo.opencore.ac.AcHandlerFunction;

/**
 * 权限控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class AcHandler {

    public AcHandlerFunction<Role> getReadRoleHandler() {
        return (role) -> StpUtil.getPermissionList().stream()
                .anyMatch(permission -> permission.equals(role.roleCode() + ".read"));
    }

}
