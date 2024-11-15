package top.belovedyaoo.openiam.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.belovedyaoo.openiam.oauth2.OpenAuthManager;
import top.belovedyaoo.openiam.oauth2.config.ServerConfig;
import top.belovedyaoo.openiam.oauth2.controller.SaOAuth2DataLoaderImpl;

/**
 * OAuth2Server配置
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class Auth2ServerConfigurer {

    private final SaOAuth2DataLoaderImpl saOAuth2DataLoader;

    /**
     * OAuth2Server 配置初始化
     */
    @Bean
    public void openAuthServerInit() {
        configInit();
        dataLoaderInit();
    }

    private void configInit() {
        ServerConfig oauth2Server = new ServerConfig();
        oauth2Server.notLogin = saOAuth2DataLoader.notLogin();
        oauth2Server.doLogin = saOAuth2DataLoader.doLogin();
        oauth2Server.confirm = saOAuth2DataLoader.confirm();
        OpenAuthManager.setServerConfig(oauth2Server);
    }

    private void dataLoaderInit() {
        OpenAuthManager.setDataLoader(saOAuth2DataLoader);
    }

}
