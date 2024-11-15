package top.belovedyaoo.openiam.oauth2.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.openiam.oauth2.OpenAuthManager;
import top.belovedyaoo.openiam.oauth2.OpenAuthUtil;
import top.belovedyaoo.openiam.oauth2.processor.OpenAuthServerProcessor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenAuth Server端 Controller
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
public class OpenAuthServerController {

    // OAuth2-Server 端：处理所有 OAuth2 相关请求
    @RequestMapping("/oauth2/*")
    public Object request() {
        System.out.println("------- 进入请求: " + SaHolder.getRequest().getUrl());
        return OpenAuthServerProcessor.instance.distribute();
    }

    // ---------- 开放相关资源接口： Client端根据 Access-Token ，置换相关资源 ------------

    // 获取 userinfo 信息：昵称、头像、性别等等
    @RequestMapping("/oauth2/userinfo")
    public SaResult userinfo() {
        // 获取 Access-Token 对应的账号id
        String accessToken = OpenAuthManager.getDataResolver().readAccessToken(SaHolder.getRequest());
        Object loginId = OpenAuthUtil.getLoginIdByAccessToken(accessToken);
        System.out.println("-------- 此Access-Token对应的账号id: " + loginId);

        // 校验 Access-Token 是否具有权限: userinfo
        OpenAuthUtil.checkAccessTokenScope(accessToken, "userinfo");

        // 模拟账号信息 （真实环境需要查询数据库获取信息）
        Map<String, Object> map = new LinkedHashMap<>();
        // map.put("userId", loginId);  一般原则下，oauth2-server 不能把 userId 返回给 oauth2-client
        map.put("nickname", "林小林");
        map.put("avatar", "http://xxx.com/1.jpg");
        map.put("age", "18");
        map.put("sex", "男");
        map.put("address", "山东省 青岛市 城阳区");
        return SaResult.ok().setMap(map);
    }

}
