package top.belovedyaoo.openiam.service;

import cn.dev33.satoken.stp.StpInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.belovedyaoo.openac.model.Permission;
import top.belovedyaoo.openac.model.Role;

import java.util.List;

/**
 * 权限数据加载源接口实现
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final PermissionLoaderImpl permissionLoader;

    /**
     * 返回指定账号id所拥有的权限码集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     *
     * @return 该账号id具有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return permissionLoader.getPermissionListByUserId(loginId.toString()).stream().map(Permission::permissionCode).toList();
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     *
     * @return 该账号id具有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return permissionLoader.getRoleListByUserId(loginId.toString()).stream().map(Role::roleCode).toList();
    }

}
