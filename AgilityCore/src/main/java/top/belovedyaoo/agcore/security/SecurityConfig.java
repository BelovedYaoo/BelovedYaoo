package top.belovedyaoo.agcore.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 安全配置类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityConfig {

    private String publicKey;

    private String privateKey;

}
