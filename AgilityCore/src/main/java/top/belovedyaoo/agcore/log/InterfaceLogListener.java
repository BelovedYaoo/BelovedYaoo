package top.belovedyaoo.agcore.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.belovedyaoo.agcore.generateMapper.InterfaceLogPOMapper;

/**
 * 注解形式的监听 异步监听日志事件
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InterfaceLogListener {

    private final InterfaceLogPOMapper interfaceLogMapper;

    @Async
    @Order
    @EventListener(InterfaceLogEvent.class)
    public void saveInterfaceLog(InterfaceLogEvent event) {
        InterfaceLogPO interfaceLog = (InterfaceLogPO) event.getSource();
        interfaceLogMapper.insert(interfaceLog);
    }

}
