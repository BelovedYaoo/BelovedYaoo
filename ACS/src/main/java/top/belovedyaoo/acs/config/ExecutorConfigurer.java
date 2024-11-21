package top.belovedyaoo.acs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 线程池配置
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Configuration
public class ExecutorConfigurer {

    @Bean
    public TaskScheduler taskScheduler() {
        // 获取系统处理器个数, 作为线程池数量
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 定时任务执行线程池核心线程数
        taskScheduler.setPoolSize(corePoolSize);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("AntiFraudSchedulerThreadPool-");
        return taskScheduler;
    }

}
