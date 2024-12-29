package top.belovedyaoo.openiam.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.opencore.base.BaseController;
import top.belovedyaoo.opencore.result.Result;
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

    @PostMapping("/test")
    public Result test() {
        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("test2");
        list.add("test3");
        AuthorizedApplication builder = new AuthorizedApplication().clientName("123").clientId("123").clientSecret("321").allowGrantTypes(list).allowRedirectUris(list);
        System.out.println(builder);
        baseMapper().insert(builder);
        return Result.success();
    }
}
