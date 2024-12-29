package top.belovedyaoo.acs.init;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.tenant.TenantManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.belovedyaoo.acs.entity.po.EnterpriseConfig;
import top.belovedyaoo.acs.generateMapper.EnterpriseConfigMapper;
import top.belovedyaoo.acs.service.PushService;
import top.belovedyaoo.acs.task.TaskManager;
import top.belovedyaoo.opencore.base.BaseFiled;

import java.util.List;

/**
 * 定时任务初始化
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskInitializer {

    private final EnterpriseConfigMapper enterpriseConfigMapper;

    private final TaskManager taskManager;

    private final PushService pushService;

    /**
     * 在应用启动后10秒执行一次初始化操作
     */
    @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
    public void initTasks() {
        System.out.println("开始初始化");

        QueryWrapper queryWrapper = QueryWrapper.create().select().from(EnterpriseConfig.class).orderBy(BaseFiled.ORDER_NUM, true);
        List<EnterpriseConfig> configList = TenantManager.withoutTenantCondition(() -> enterpriseConfigMapper.selectListByQuery(queryWrapper));

        // for (EnterpriseConfig config : configList) {
        //     if (!config.isEnabledPush().equals("1")) {
        //         continue;
        //     }
        //     String cronExpression = config.pushCorn();
        //     String tenantId = config.tenantId();
        //
        //     // 注册定时任务
        //     taskManager.addTask(() -> {
        //         System.out.println("Hello " + tenantId);
        //         pushService.pushCourse(config);
        //     }, cronExpression, tenantId);
        // }
    }
}
