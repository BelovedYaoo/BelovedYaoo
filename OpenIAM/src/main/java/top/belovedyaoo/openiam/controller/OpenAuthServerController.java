package top.belovedyaoo.openiam.controller;

import cn.dev33.satoken.context.SaHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.openiam.processor.OpenAuthServerProcessor;

/**
 * OpenAuth 服务端控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
public class OpenAuthServerController {

    /**
     * OAuth2-Server 端：处理所有 OAuth2 相关请求
     * @return 处理结果
     */
    @RequestMapping("/oauth2/*")
    public Object request() {
        System.out.println("------- 进入请求: " + SaHolder.getRequest().getUrl());
        return OpenAuthServerProcessor.instance.distribute();
    }

}
