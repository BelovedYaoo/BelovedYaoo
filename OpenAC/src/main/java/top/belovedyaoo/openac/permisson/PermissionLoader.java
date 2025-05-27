package top.belovedyaoo.openac.permisson;

import top.belovedyaoo.openac.model.Permission;
import top.belovedyaoo.openac.model.Role;

import java.util.List;

/**
 * 权限加载器接口
 *
 * @author Celrx
 * @version 1.0
 */
public interface PermissionLoader {

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID
     *
     * @return 角色列表
     */
    List<Role> getRoleListByUserId(String userId);

    /**
     * 根据用户ID获取权限列表
     *
     * @param userId 用户ID
     *
     * @return 权限列表
     */
    List<Permission> getPermissionListByUserId(String userId);

    /**
     * 根据角色获取权限列表
     *
     * @param role 角色
     *
     * @return 权限列表
     */
    List<Permission> getPermissionListByRole(Role role);

}
