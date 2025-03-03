package top.belovedyaoo.openiam.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import top.belovedyaoo.opencore.ac.EntitySetListener;
import top.belovedyaoo.opencore.base.BaseIdFiled;
import top.belovedyaoo.opencore.processor.LogicDeleteProcessor;
import top.belovedyaoo.opencore.common.SqlCollector;
import top.belovedyaoo.opencore.eo.EntityInsertListener;
import top.belovedyaoo.opencore.eo.EntityUpdateListener;
import top.belovedyaoo.opencore.tree.Tree;
import top.belovedyaoo.opencore.tree.TreeOperateListener;

/**
 * Mybatis-Flex 框架的配置类
 *
 * @author BelovedYaoo
 * @version 1.3
 */
@Configuration
public class MybatisFlexConfigurer {

    /**
     * Mybatis-Flex 配置初始化
     */
    @PostConstruct
    public void flexConfigInit() {
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
        globalConfig.registerInsertListener(new TreeOperateListener(), Tree.class);
        globalConfig.registerUpdateListener(new TreeOperateListener(), Tree.class);
        globalConfig.registerSetListener(new EntitySetListener(), BaseIdFiled.class);
    }

}
