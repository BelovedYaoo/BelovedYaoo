package top.belovedyaoo.logs.event;

import org.springframework.context.ApplicationEvent;
import top.belovedyaoo.logs.model.po.InterfaceLogPO;

/**
 * 接口日志事件
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class InterfaceLogEvent extends ApplicationEvent {

    public InterfaceLogEvent(InterfaceLogPO source) {
        super(source);
    }

}
