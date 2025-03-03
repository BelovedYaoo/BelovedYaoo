package top.belovedyaoo.openiam.interceptor;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.BaseMapper;
import lombok.RequiredArgsConstructor;
import org.dromara.autotable.core.callback.AutoTableFinishCallback;
import org.dromara.autotable.core.utils.TableBeanUtils;
import org.springframework.stereotype.Component;
import top.belovedyaoo.openac.model.Domain;
import top.belovedyaoo.openac.model.Permission;
import top.belovedyaoo.openac.model.Role;
import top.belovedyaoo.openac.model.User;
import top.belovedyaoo.openac.model.mapping.MappingDomainUserRole;
import top.belovedyaoo.openac.model.mapping.MappingRolePermission;
import top.belovedyaoo.openac.service.DomainServiceImpl;
import top.belovedyaoo.openac.service.PermissionServiceImpl;
import top.belovedyaoo.openac.service.RoleServiceImpl;
import top.belovedyaoo.openac.service.UserServiceImpl;
import top.belovedyaoo.openiam.entity.po.AuthApp;
import top.belovedyaoo.openiam.service.AuthAppService;

import java.util.List;
import java.util.Set;

import static top.belovedyaoo.openiam.interceptor.CreateTableFinish.TABLE_METADATA_MAP;

/**
 * AutoTable 表结构自动维护完成后的回调<br>
 * 用于初始化数据
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class AutoTableFinish implements AutoTableFinishCallback {

    private final UserServiceImpl userService;

    private final DomainServiceImpl domainService;

    private final AuthAppService authAppService;

    private final RoleServiceImpl roleService;

    private final PermissionServiceImpl permissionService;

    private final BaseMapper<MappingRolePermission> mrpMapper;

    private final BaseMapper<MappingDomainUserRole> mdurMapper;

    String topDomainId;

    String userId;

    @Override
    public void finish(Set<Class<?>> tableClasses) {
        if (TABLE_METADATA_MAP.isNotValidKey(TableBeanUtils.getTableName(User.class))
                || TABLE_METADATA_MAP.isNotValidKey(TableBeanUtils.getTableName(Domain.class))
                || TABLE_METADATA_MAP.isNotValidKey(TableBeanUtils.getTableName(Role.class))
                || TABLE_METADATA_MAP.isNotValidKey(TableBeanUtils.getTableName(AuthApp.class))) {
            return;
        }
        // 提取分配ID，方便写入关系表
        topDomainId = IdUtil.simpleUUID();
        userId = IdUtil.simpleUUID();
        userId = "0b2d398202aa79c77798d0108ce8fc5d";
        String authAppId = IdUtil.simpleUUID();
        // 域初始化
        Domain topDomain = Domain.builder()
                .baseId(topDomainId)
                .domainName("BelovedYaoo")
                .domainCode("0")
                .domainType(Domain.DomainType.NORMAL)
                .domainDesc("系统默认的顶级域，其他所有域均为该域的子域")
                .build();
        domainService.save(topDomain);
        // 用户初始化
        User user = User.builder()
                .baseId(userId)
                .openId("ovo")
                .password("ZjZlMGExZTJhYzQxOTQ1YTlhYTdmZjhhOGFhYTBjZWJjMTJhM2JjYzk4MWE5MjlhZDVjZjgxMGEwOTBlMTFhZQ==")
                .nickname("BelovedYaoo")
                .build();
        userService.save(user);
        // 角色初始化
        initRole();
        // 创建上帝
        String godRoleId = createGod();
        AuthApp app = AuthApp.builder()
                .baseId(authAppId)
                .clientName("OpenIAM")
                .clientId("1000")
                .clientSecret("openiam")
                .contractScopes(List.of("oidc"))
                .allowRedirectUris(List.of("*"))
                .allowGrantTypes(List.of("authorization_code"))
                .build();
        String authAppDomainId = authAppService.createAuthApp(app, false);
        mdurMapper.insert(MappingDomainUserRole.builder()
                .domainId(authAppDomainId)
                .userId(userId)
                .roleId(godRoleId)
                .build());
    }

    /**
     * 初始化角色
     */
    private void initRole() {
        // 创建超级管理员
        Role superRole = Role.builder()
                .domainId(topDomainId)
                .roleName("超级管理员")
                .roleCode("super")
                .roleDesc("拥有指定系统内的所有权限")
                .isVisibleToSubDomain(true)
                .isAssignableToSubDomain(true)
                .isOverridableToSubDomain(false)
                .build();
        roleService.createRole(superRole);
        // 创建管理员
        Role adminRole = Role.builder()
                .domainId(topDomainId)
                .roleName("管理员")
                .roleCode("admin")
                .roleDesc("拥有指定系统内的部分管理权限")
                .isVisibleToSubDomain(true)
                .isAssignableToSubDomain(true)
                .isOverridableToSubDomain(true)
                .build();
        roleService.createRole(adminRole);
        // 创建用户
        Role userRole = Role.builder()
                .domainId(topDomainId)
                .roleName("用户")
                .roleCode("user")
                .roleDesc("拥有指定系统内的用户权限")
                .isVisibleToSubDomain(true)
                .isAssignableToSubDomain(true)
                .isOverridableToSubDomain(true)
                .build();
        roleService.createRole(userRole);
        roleService.authOtherRoleReadAndWritePermission(superRole,adminRole);
        roleService.authOtherRoleReadAndWritePermission(superRole,userRole);
        roleService.authOtherRoleReadAndWritePermission(adminRole,userRole);
    }

    private String createGod() {
        String godRoleId = IdUtil.simpleUUID();
        // 创建上帝？
        Role godRole = Role.builder()
                .baseId(godRoleId)
                .domainId(topDomainId)
                .roleName("上帝")
                .roleCode("god")
                .roleDesc("拥有所有系统的所有权限")
                .isVisibleToSubDomain(false)
                .isAssignableToSubDomain(false)
                .isOverridableToSubDomain(false)
                .build();
        roleService.save(godRole);
        Permission godPermission = Permission.builder()
                .permissionName("上帝权限")
                .permissionCode("*")
                .permissionDesc("上帝权限")
                .build();
        permissionService.save(godPermission);
        MappingRolePermission mrp = MappingRolePermission.builder()
                .roleId(godRole.baseId())
                .permissionId(godPermission.baseId())
                .build();
        mrpMapper.insert(mrp);
        return godRoleId;
    }

}