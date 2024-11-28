package top.belovedyaoo.openiam.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.util.SaResult;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.openauth.core.OpenAuthManager;
import top.belovedyaoo.openauth.core.OpenAuthUtil;
import top.belovedyaoo.openauth.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openiam.entity.po.AuthorizedApplication;
import top.belovedyaoo.openiam.generateMapper.AuthorizedApplicationMapper;
import top.belovedyaoo.openiam.service.AuthenticationService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenAuth 服务端控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/openAuth")
@RequiredArgsConstructor
public class OpenAuthController {

    private final AuthenticationService authenticationService;

    private final AuthorizedApplicationMapper authorizedApplicationMapper;

    @RequestMapping("/getClientModel")
    public Result getClientModel(@RequestParam(value = "client_id") String clientId) {
        AuthorizedApplication authorizedApplication = authorizedApplicationMapper
                .selectOneByQuery(
                        new QueryWrapper()
                                .where("client_id = '" + clientId + "'"));
        OpenAuthClientModel clientModel = authorizedApplication
                .convertToClientModel();
        return Result.success().singleData(clientModel);
    }

    @RequestMapping("/getUser")
    public Result getUser(String openId, String password) {
        return authenticationService.getUser(openId, password);
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
