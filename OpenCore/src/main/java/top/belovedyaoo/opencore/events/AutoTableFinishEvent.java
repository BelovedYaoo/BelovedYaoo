package top.belovedyaoo.opencore.events;

import org.springframework.context.ApplicationEvent;

/**
 * AutoTable表维护阶段完成的事件
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class AutoTableFinishEvent extends ApplicationEvent {

    public AutoTableFinishEvent(Object source) {
        super(source);
    }

}
