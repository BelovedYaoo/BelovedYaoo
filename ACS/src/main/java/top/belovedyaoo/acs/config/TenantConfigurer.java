package top.belovedyaoo.acs.config;

import com.mybatisflex.core.tenant.TenantFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.belovedyaoo.opencore.base.BaseTenantFactory;

/**
 * 多租户配置
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Configuration
public class TenantConfigurer {

    @Bean
    public TenantFactory tenantFactory(){
        return new BaseTenantFactory();
    }

}
