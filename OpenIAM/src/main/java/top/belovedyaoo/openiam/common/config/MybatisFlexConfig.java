package top.belovedyaoo.openiam.common.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import top.belovedyaoo.opencore.base.LogicDeleteProcessor;
import top.belovedyaoo.opencore.common.SqlCollector;
import top.belovedyaoo.opencore.eo.EntityInsertListener;
import top.belovedyaoo.opencore.eo.EntityUpdateListener;

/**
 * Mybatis-Flex 框架的配置类
 *
 * @author BelovedYaoo
 * @version 1.2
 */
@Configuration
public class MybatisFlexConfig {

    /**
     * Mybatis-Flex 配置初始化
     */
    @PostConstruct
    public void init() {
        FlexGlobalConfig globalConfig = FlexGlobalConfig.getDefaultConfig();
        // 设置逻辑删除处理器
        LogicDeleteManager.setProcessor(new LogicDeleteProcessor());
        // 开启 SQL 审计功能
        AuditManager.setAuditEnable(true);
        // 设置 SQL 审计收集器
        AuditManager.setMessageCollector(new SqlCollector(SqlCollector.PrintType.LOGGER));
        // 注册插入监听器、更新监听器
        globalConfig.registerInsertListener(new EntityInsertListener(), Object.class);
        globalConfig.registerUpdateListener(new EntityUpdateListener(), Object.class);
    }

}
