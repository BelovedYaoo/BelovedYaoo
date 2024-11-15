package top.belovedyaoo.openiam.permission.controller;

import com.mybatisflex.core.BaseMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.agcore.base.BaseController;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.openiam.permission.entity.AuthorizedApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 授权应用控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/auth_app")
public class AuthorizedApplicationController extends BaseController<AuthorizedApplication> {

    public AuthorizedApplicationController(BaseMapper<AuthorizedApplication> baseMapper, PlatformTransactionManager platformTransactionManager) {
        super(baseMapper, platformTransactionManager);
    }


    @PostMapping("/test")
    public Result test() {
        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("test2");
        list.add("test3");
        baseMapper.insert(new AuthorizedApplication().clientName("123").clientId("123").clientSecret("321").allowGrantTypes(list).allowRedirectUris(list));
        return Result.success();
    }
}
