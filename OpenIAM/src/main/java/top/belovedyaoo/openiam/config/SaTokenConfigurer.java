package top.belovedyaoo.openiam.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.belovedyaoo.logs.toolkit.LogUtil;
import top.belovedyaoo.openac.interceptor.OperationInterceptor;
import top.belovedyaoo.openiam.consts.OpenAuthConst;
import top.belovedyaoo.opencore.constants.enums.exception.SaTokenExceptionEnum;
import top.belovedyaoo.opencore.constants.enums.result.AuthEnum;
import top.belovedyaoo.opencore.result.Result;

/**
 * Sa-Token 配置类
 *
 * @author BelovedYaoo
 * @version 1.2
 */
@Configuration
@RequiredArgsConstructor
public class SaTokenConfigurer implements WebMvcConfigurer {

    private final OperationInterceptor operationInterceptor;

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
                .addExclude(OpenAuthConst.Api.token)
                // 认证函数: 每次请求执行
                .setAuth(obj -> {
                    StpUtil.checkLogin();
                })
                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    if (e instanceof NotLoginException nle) {
                        String message = SaTokenExceptionEnum.getDescByType(nle.getType());
                        LogUtil.error("Sa-Token登录异常处理："+ message);
                        return Result.failed().resultType(AuthEnum.SESSION_INVALID).message(message);
                    }
                    return Result.failed().message(e.getMessage());
                })
                // 前置函数：在每次认证函数之前执行，且不受 [拦截路由] 与 [放行路由] 的限制，所有路由的请求都会进入前置函数
                .setBeforeAuth(obj -> {
                    SaHolder.getResponse()
                            // 设置跨域响应头
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "*")
                            // 不允许携带 Cookie
                            .setHeader("Access-Control-Allow-Credentials", "false")
                            // 允许的 Header 参数
                            .setHeader("Access-Control-Allow-Headers", SaManager.getConfig().getTokenName().toLowerCase() + ",Content-Type")
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(operationInterceptor.auth()))
                .addPathPatterns("/**")
                .excludePathPatterns(OpenAuthConst.Api.doLogin)
                .excludePathPatterns(OpenAuthConst.Api.token);
    }

}
