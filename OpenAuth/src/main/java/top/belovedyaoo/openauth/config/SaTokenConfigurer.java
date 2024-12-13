package top.belovedyaoo.openauth.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.belovedyaoo.openauth.consts.OpenAuthConst;

/**
 * Sa-Token 配置类
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Configuration
public class SaTokenConfigurer implements WebMvcConfigurer {

    /**
     * Sa-Token 整合 jwt (Simple 简单模式)
     *
     * @return 整合规则
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    /**
     * 注册 [Sa-Token 全局过滤器]
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                // 指定 [拦截路由] 与 [放行路由]
                .addInclude("/**")
                .addExclude(OpenAuthConst.Api.doLogin)
                // 认证函数: 每次请求执行
                .setAuth(obj -> {
                    StpUtil.checkLogin();
                    System.out.println(STR."----- 请求path=\{SaHolder.getRequest().getRequestPath()}  提交token=\{StpUtil.getTokenValue()}");
                })
                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    return SaResult.error(e.getMessage());
                })
                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    SaHolder.getResponse()
                            // 设置跨域响应头
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "*")
                            // 允许携带 Cookie
                            .setHeader("Access-Control-Allow-Credentials", "true")
                            // 允许的 Header 参数
                            .setHeader("Access-Control-Allow-Headers", SaManager.getConfig().getTokenName().toLowerCase())
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600");
                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> {
                                System.out.println("--------OPTIONS预检请求，不做处理--------");
                            })
                            .back();
                });
    }

}
