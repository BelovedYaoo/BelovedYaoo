package top.belovedyaoo.acs.task;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务业务封装对象
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public final class ScheduledTaskBo {
 
    public volatile ScheduledFuture<?> future;
 
    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> scheduledFuture = this.future;
        if (Objects.nonNull(scheduledFuture)) {
            scheduledFuture.cancel(true);
        }
    }
}