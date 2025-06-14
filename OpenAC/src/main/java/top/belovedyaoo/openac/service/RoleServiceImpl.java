package top.belovedyaoo.openac.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import top.belovedyaoo.openac.model.Role;
import top.belovedyaoo.openac.model.mapping.MappingDomainUserRole;
import top.belovedyaoo.openac.model.mapping.MappingRolePermission;
import top.belovedyaoo.opencore.ac.AcServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色服务类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends AcServiceImpl<Role> {

    private final PlatformTransactionManager platformTransactionManager;

    private final PermissionServiceImpl permissionService;

    private final BaseMapper<MappingRolePermission> mrpMapper;

    /**
     * 创建角色，并且创建角色的读写权限
     *
     * @param role 角色
     *
     * @return 是否创建成功
     */
    public boolean createRole(Role role) {
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        role.baseId(IdUtil.simpleUUID());
        // 创建角色和权限
        if (save(role)) {
            boolean createPermission = permissionService.createPermissionForRole(role);
            if (createPermission) {
                platformTransactionManager.commit(transactionStatus);
                return true;
            }
        }
        platformTransactionManager.rollback(transactionStatus);
        return false;
    }

    /**
     * 授予一个角色其他角色的读写权限
     *
     * @param role      授权的角色
     * @param otherRole 被授权的角色
     *
     * @return 授权结果
     */
    public boolean authOtherRoleReadAndWritePermission(Role role, Role otherRole) {
        // 找到被授予角色的读权限
        try {
            String readPermissionId = permissionService.getReadAndWritePermissionByRole(otherRole, true).baseId();
            String writePermissionId = permissionService.getReadAndWritePermissionByRole(otherRole, false).baseId();
            MappingRolePermission mrpRead = MappingRolePermission.builder()
                    .roleId(role.baseId())
                    .permissionId(readPermissionId)
                    .build();
            MappingRolePermission mrpWrite = MappingRolePermission.builder()
                    .roleId(role.baseId())
                    .permissionId(writePermissionId)
                    .build();
            return mrpMapper.insert(mrpRead) == 1 && mrpMapper.insert(mrpWrite) == 1;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * 根据用户和域查询角色列表
     * @param userId 用户id
     * @param domainId 域id
     * @return 角色列表
     */
    public List<Role> queryRoleListByUserAndDomain(String userId, String domainId) {
        // SELECT r.*
        // FROM mapping_domain_user_role m
        // INNER JOIN role r ON m.role_id = r.base_id
        // WHERE m.domain_id = ?          -- 替换为传入的 domainId
        //   AND m.user_id = ?
        String roleAlias = "r";
        String mappingAlias = "m";
        QueryWrapper.create()
                .select()
                .from(MappingDomainUserRole.class).as(mappingAlias)
                .innerJoin(Role.class).as(roleAlias)
                .on(new QueryColumn(mappingAlias,Role.ROLE_ID).eq(new QueryColumn(roleAlias,Role.BASE_ID)));
        return new ArrayList<>();
    }

}
