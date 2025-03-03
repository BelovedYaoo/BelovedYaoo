package top.belovedyaoo.openiam.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import top.belovedyaoo.openac.function.AcHandler;
import top.belovedyaoo.openac.model.Role;
import top.belovedyaoo.opencore.ac.AcServiceStrategy;

/**
 * OpenAC权限处理策略初始化
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Configuration
public class AcStrategyInitializer {

    @PostConstruct
    public void acStrategyInit() {
        AcHandler acHandler = new AcHandler();
        AcServiceStrategy.INSTANCE.registerHandler(Role.class, acHandler.getReadRoleHandler(), true);
    }

}
