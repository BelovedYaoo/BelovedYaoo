package top.belovedyaoo.openiam.controller;

import com.mybatisflex.core.BaseMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.agcore.base.BaseController;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.openiam.entity.po.AuthorizedApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 授权应用控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/authApp")
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
        AuthorizedApplication builder = new AuthorizedApplication().clientName("123").clientId("123").clientSecret("321").allowGrantTypes(list).allowRedirectUris(list);
        System.out.println(builder);
        baseMapper.insert(builder);
        return Result.success();
    }
}
