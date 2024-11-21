package top.belovedyaoo.acs.task;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;
import top.belovedyaoo.acs.entity.po.EnterpriseConfig;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class TaskManager implements DisposableBean {

    private final TaskScheduler taskScheduler;

    /**
     * 保存任务Id和定时任务
     */
    private final Map<String, ScheduledTaskBo> scheduledTaskMap = new ConcurrentHashMap<>(64);

    /**
     * 添加任务
     *
     * @param task
     * @param cronExpression
     * @param jobId
     */
    public void addTask(Runnable task, String cronExpression, String jobId) {
        addTask(new CronTask(task, cronExpression), jobId);
    }

    public void addTask(CronTask cronTask, String jobId) {
        if (Objects.nonNull(cronTask)) {
            Runnable task = cronTask.getRunnable();
            if (this.scheduledTaskMap.containsKey(task)) {
                System.out.println("has key!");
                System.out.println("key:" + task);
                removeTask(jobId);
            }
            // 保存任务Id和定时任务
            this.scheduledTaskMap.put(jobId, scheduleTask(cronTask));
        }
    }

    public void classPushTask(EnterpriseConfig enterpriseConfig) {
        System.out.println(enterpriseConfig.tenantId());
    }

    /**
     * 通过任务Id，取消定时任务
     *
     * @param jobId
     */
    public void removeTask(String jobId) {
        ScheduledTaskBo scheduledTask = this.scheduledTaskMap.remove(jobId);
        if (Objects.nonNull(scheduledTask)) {
            scheduledTask.cancel();
        }
    }

    public ScheduledTaskBo scheduleTask(CronTask cronTask) {
        ScheduledTaskBo scheduledTask = new ScheduledTaskBo();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduledTask;
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        this.scheduledTaskMap.values().forEach(ScheduledTaskBo::cancel);
        this.scheduledTaskMap.clear();
    }

}