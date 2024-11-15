package top.belovedyaoo.openiam.common.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 配置类
 *
 * @author BelovedYaoo
 * @version 1.0
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
     * 注册 Sa-Token 的路由拦截器，自定义认证规则
     *
     * @param registry 路由拦截器注册器
     */
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(new SaInterceptor((handle) -> {
    //                 // 检查是否登录
    //                 StpUtil.checkLogin();
    //             }))
    //             // 所有路径拦截
    //             .addPathPatterns("/**")
    //             // 认证路径排除
    //             .excludePathPatterns("/oauth2/**")
    //             .excludePathPatterns("/auth/**");
    // }

}
