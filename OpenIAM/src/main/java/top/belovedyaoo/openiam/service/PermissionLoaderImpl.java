package top.belovedyaoo.openiam.service;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.belovedyaoo.openac.generateMapper.MappingDomainUserRoleMapper;
import top.belovedyaoo.openac.generateMapper.MappingRolePermissionMapper;
import top.belovedyaoo.openac.generateMapper.PermissionMapper;
import top.belovedyaoo.openac.generateMapper.RoleMapper;
import top.belovedyaoo.openac.model.Permission;
import top.belovedyaoo.openac.model.Role;
import top.belovedyaoo.openac.model.mapping.MappingDomainUserRole;
import top.belovedyaoo.openac.model.mapping.MappingRolePermission;
import top.belovedyaoo.openac.permisson.PermissionLoader;
import top.belovedyaoo.opencore.base.BaseIdFiled;
import top.belovedyaoo.opencore.cache.CacheAccess;
import top.belovedyaoo.opencore.cache.strategy.PenetrateCacheStrategy;
import top.belovedyaoo.opencore.toolkit.JedisOperateUtil;

import java.util.List;

import static top.belovedyaoo.openac.model.User.USER_ID;

/**
 * 权限加载器实现类
 *
 * @author Celrx
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class PermissionLoaderImpl implements PermissionLoader {

    private final MappingDomainUserRoleMapper mappingDomainUserRoleMapper;

    private final MappingRolePermissionMapper mappingRolePermissionMapper;

    private final RoleMapper roleMapper;

    private final PermissionMapper permissionMapper;

    /**
     * Redis缓存时间（秒）
     */
    private static final int CACHE_TIMEOUT = 60 * 60;

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID
     *
     * @return 角色列表
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Role> getRoleListByUserId(String userId) {
        String redisKey = "user:role:" + userId;
        return CacheAccess.<String, List<Role>>start()
                .strategy(new PenetrateCacheStrategy<>())
                .key(redisKey)
                .cacheRead(key -> (List<Role>) JedisOperateUtil.get(key, List.class))
                .dbRead(() -> {
                    List<MappingDomainUserRole> mappingList = mappingDomainUserRoleMapper.selectListByQuery(
                            QueryWrapper.create().where(new QueryColumn(USER_ID).eq(userId)));
                    if (mappingList == null || mappingList.isEmpty()) {
                        return List.of();
                    }
                    List<String> roleIds = mappingList.stream().map(MappingDomainUserRole::roleId).toList();
                    return roleMapper.selectListByIds(roleIds);
                })
                .cacheWrite((key, value) -> JedisOperateUtil.setEx(key, value, CACHE_TIMEOUT))
                .cacheEvict(JedisOperateUtil::del)
                .run();
    }

    /**
     * 根据用户ID获取权限列表
     *
     * @param userId 用户ID
     *
     * @return 权限列表
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Permission> getPermissionListByUserId(String userId) {
        String redisKey = "user:permission:" + userId;
        return CacheAccess.<String, List<Permission>>start()
                .strategy(new PenetrateCacheStrategy<>())
                .key(redisKey)
                .cacheRead(key -> (List<Permission>) JedisOperateUtil.get(key, List.class))
                .dbRead(() -> {
                    List<Role> roleList = getRoleListByUserId(userId);
                    if (roleList == null || roleList.isEmpty()) {
                        return List.of();
                    }
                    List<String> roleIds = roleList.stream().map(BaseIdFiled::baseId).toList();
                    List<MappingRolePermission> mappingList = mappingRolePermissionMapper.selectListByQuery(
                            QueryWrapper.create().where(new QueryColumn(Role.ROLE_ID).in(roleIds)));
                    if (mappingList == null || mappingList.isEmpty()) {
                        return List.of();
                    }
                    List<String> permissionIds = mappingList.stream().map(MappingRolePermission::permissionId).toList();
                    return permissionMapper.selectListByIds(permissionIds);
                })
                .cacheWrite((key, value) -> JedisOperateUtil.setEx(key, value, CACHE_TIMEOUT))
                .cacheEvict(JedisOperateUtil::del)
                .run();
    }

    /**
     * 根据角色获取权限列表
     *
     * @param role 角色
     *
     * @return 权限列表
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Permission> getPermissionListByRole(Role role) {
        if (role == null || role.baseId() == null) {
            return List.of();
        }
        String redisKey = "role:permission:" + role.baseId();
        return CacheAccess.<String, List<Permission>>start()
                .strategy(new PenetrateCacheStrategy<>())
                .key(redisKey)
                .cacheRead(key -> (List<Permission>) JedisOperateUtil.get(key, List.class))
                .dbRead(() -> {
                    List<MappingRolePermission> mappingList = mappingRolePermissionMapper.selectListByQuery(
                            QueryWrapper.create().where(new QueryColumn(Role.ROLE_ID).eq(role.baseId())));
                    if (mappingList == null || mappingList.isEmpty()) {
                        return List.of();
                    }
                    List<String> permissionIds = mappingList.stream().map(MappingRolePermission::permissionId).toList();
                    return permissionMapper.selectListByIds(permissionIds);
                })
                .cacheWrite((key, value) -> JedisOperateUtil.setEx(key, value, CACHE_TIMEOUT))
                .cacheEvict(JedisOperateUtil::del)
                .run();
    }

}
