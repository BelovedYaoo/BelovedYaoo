package top.belovedyaoo.openauth.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import top.belovedyaoo.openauth.core.OpenAuthManager;
import top.belovedyaoo.openauth.service.OpenAuthDataLoaderImpl;

/**
 * OAuth2Server配置
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class Auth2ServerConfigurer {

    private final OpenAuthDataLoaderImpl openAuthDataLoader;

    /**
     * OAuth2Server 配置初始化
     */
    @PostConstruct
    public void openAuthServerInit() {
        configInit();
        dataLoaderInit();
    }

    private void configInit() {
        ServerConfig oauth2Server = new ServerConfig();
        oauth2Server.notLogin = openAuthDataLoader.notLogin();
        oauth2Server.doLogin = openAuthDataLoader.doLogin();
        oauth2Server.confirm = openAuthDataLoader.confirm();
        OpenAuthManager.setServerConfig(oauth2Server);
    }

    private void dataLoaderInit() {
        OpenAuthManager.setDataLoader(openAuthDataLoader);
    }

}
