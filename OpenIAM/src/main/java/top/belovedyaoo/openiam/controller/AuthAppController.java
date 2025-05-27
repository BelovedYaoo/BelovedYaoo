package top.belovedyaoo.openiam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.logs.annotation.InterfaceLog;
import top.belovedyaoo.openac.initializer.AcDataCacheLoader;
import top.belovedyaoo.openiam.model.AuthApp;
import top.belovedyaoo.openiam.service.AuthAppService;
import top.belovedyaoo.opencore.base.DefaultController;
import top.belovedyaoo.opencore.result.Result;

import java.util.List;

/**
 * 授权应用控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/authApp")
@RequiredArgsConstructor
public class AuthAppController extends DefaultController<AuthApp> {

    private final AuthAppService authAppService;

    private final AcDataCacheLoader acDataCacheLoader;

    /**
     * 新增数据
     *
     * @param entity 需要添加的数据
     *
     * @return 操作结果
     */
    @Override
    public Result add(AuthApp entity) {
        entity.clientSecret("default")
                .contractScopes(List.of("oidc"))
                .allowRedirectUris(List.of("*"))
                .allowGrantTypes(List.of("authorization_code"));
        authAppService.createAuthApp(entity, false);
        return Result.success();
    }

    @RequestMapping("/test")
    @InterfaceLog(identifierCode = "test", interfaceName = "测试接口", interfaceDesc = "测试接口描述")
    public Result test() {
        return Result.success().singleData(acDataCacheLoader.getMappingOperationPermissionMap());
    }

}
