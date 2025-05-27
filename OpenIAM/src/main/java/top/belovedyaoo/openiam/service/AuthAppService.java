package top.belovedyaoo.openiam.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.belovedyaoo.openac.model.Domain;
import top.belovedyaoo.openac.service.DomainServiceImpl;
import top.belovedyaoo.openiam.model.AuthApp;
import top.belovedyaoo.openiam.model.mapping.MappingAuthAppDomain;
import top.belovedyaoo.openiam.model.mapping.MappingAuthAppUser;
import top.belovedyaoo.openiam.model.mapping.MappingAuthAppUserGroup;

import static top.belovedyaoo.openac.model.table.DomainTableDef.DOMAIN;

/**
 * 授权应用服务类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthAppService {

    private final DomainServiceImpl domainService;

    private final BaseMapper<AuthApp> authAppMapper;

    private final BaseMapper<MappingAuthAppDomain> mappingAuthAppDomainMapper;

    private final BaseMapper<MappingAuthAppUser> mappingAuthAppUserMapper;

    private final BaseMapper<MappingAuthAppUserGroup> mappingAuthAppUserGroupMapper;

    /**
     * 创建一个应用
     * @param app 应用信息
     * @param enableSub 是否创建子域
     * @return 应用域的BaseID
     */
    public String createAuthApp(AuthApp app, boolean enableSub) {
        // 拿到顶级域
        Domain top = domainService.getOne(new QueryWrapper().where(DOMAIN.DOMAIN_CODE.eq("0")));
        String appDomainBaseId = IdUtil.simpleUUID();
        // 为该应用创建一个应用域
        Domain appDomain = Domain.builder()
                .baseId(appDomainBaseId)
                .domainName(app.clientName()+ "应用域")
                .domainCode(app.clientId())
                .domainType(Domain.DomainType.APP)
                .domainDesc("该域为"+app.clientName()+"的默认应用域")
                .build()
                .parentId(top.baseId());
        domainService.save(appDomain);
        // 创建应用，并将应用指向该应用域
        mappingAuthAppDomainMapper.insert(MappingAuthAppDomain.builder()
                .appId(app.baseId())
                .domainId(appDomainBaseId)
                .build());
        authAppMapper.insert(app);
        // 视情况在该应用域下创建一个子域
        return appDomainBaseId;
    }

    /**
     * 授予一个用户访问应用的权限
     * @param userId 用户的BaseID
     * @param appId 应用的BaseID
     * @return 授权结果
     */
    public boolean grantUserAccess(String userId, String appId) {
        MappingAuthAppUser mappingAuthAppUser = MappingAuthAppUser.builder()
                .appId(appId)
                .userId(userId)
                .build();
        return mappingAuthAppUserMapper.insert(mappingAuthAppUser) == 1;
    }

    /**
     * 授予一个用户组访问应用的权限
     * @param groupId 用户组的BaseID
     * @param appId 应用的BaseID
     * @return 授权结果
     */
    public boolean grantGroupAccess(String groupId, String appId) {
        MappingAuthAppUserGroup mappingAuthAppUserGroup = MappingAuthAppUserGroup.builder()
                .appId(appId)
                .userGroupId(groupId)
                .build();
        return mappingAuthAppUserGroupMapper.insert(mappingAuthAppUserGroup) == 1;
    }

}
