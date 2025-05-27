package top.belovedyaoo.openac.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import top.belovedyaoo.openac.model.Permission;
import top.belovedyaoo.openac.model.Role;
import top.belovedyaoo.openac.model.mapping.MappingRolePermission;
import top.belovedyaoo.opencore.ac.AcServiceImpl;
import top.belovedyaoo.opencore.base.BaseIdFiled;

import static top.belovedyaoo.openac.model.table.PermissionTableDef.PERMISSION;

/**
 * 权限服务类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends AcServiceImpl<Permission> {

    private final BaseMapper<MappingRolePermission> mrpMapper;

    private final PlatformTransactionManager platformTransactionManager;

    /**
     * 为角色创建读写权限
     *
     * @param role 角色
     *
     * @return 是否创建成功
     */
    public boolean createPermissionForRole(Role role) {
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        // 创建角色的读写权限
        String readId = IdUtil.simpleUUID();
        String writeId = IdUtil.simpleUUID();
        Permission roleRead = Permission.builder()
                .baseId(readId)
                .permissionName(role.roleName() + "读权限")
                .permissionCode(role.roleCode() + ".read")
                .permissionDesc("由系统自动创建的权限，" + role.roleName() + "的读权限")
                .build();
        Permission roleWrite = Permission.builder()
                .baseId(writeId)
                .permissionName(role.roleName() + "写权限")
                .permissionCode(role.roleCode() + ".write")
                .permissionDesc("由系统自动创建的权限，" + role.roleName() + "的写权限")
                .build();
        // 赋予自身读写权限
        MappingRolePermission mrpRead = MappingRolePermission.builder()
                .roleId(role.baseId())
                .permissionId(readId)
                .build();
        MappingRolePermission mrpWrite = MappingRolePermission.builder()
                .roleId(role.baseId())
                .permissionId(writeId)
                .build();
        // 提交事务或回滚
        if (save(roleRead) && save(roleWrite) && mrpMapper.insert(mrpRead) == 1 && mrpMapper.insert(mrpWrite) == 1) {
            platformTransactionManager.commit(transactionStatus);
            return true;
        } else {
            platformTransactionManager.rollback(transactionStatus);
            return false;
        }
    }

    /**
     * 根据角色获取读写权限
     * @param role 角色
     * @param getRead 获取的是读权限还是写权限
     * @return 获取到的权限
     */
    public Permission getReadAndWritePermissionByRole(Role role, boolean getRead) {
        return getMapper().selectOneByQuery(QueryWrapper.create()
                .select(BaseIdFiled.BASE_ID)
                .where(PERMISSION.PERMISSION_CODE.eq(role.roleCode() + (getRead ? ".read" : ".write"))));
    }

}