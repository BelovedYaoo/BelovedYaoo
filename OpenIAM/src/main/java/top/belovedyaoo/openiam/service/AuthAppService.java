package top.belovedyaoo.openiam.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.belovedyaoo.openac.model.Domain;
import top.belovedyaoo.openac.service.DomainServiceImpl;
import top.belovedyaoo.openiam.entity.po.AuthApp;

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
                .domainDesc("该域为"+app.clientName()+"的应用域")
                .build()
                .parentId(top.baseId());
        domainService.save(appDomain);
        // 创建应用，并将应用指向该应用域
        app.domainId(appDomainBaseId);
        authAppMapper.insert(app);
        // 视情况在该应用域下创建一个子域
        return appDomainBaseId;
    }

}
