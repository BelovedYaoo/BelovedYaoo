package top.belovedyaoo.opencore.common;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * Logback 自定义颜色配置
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class LogbackDateColorful extends ForegroundCompositeConverterBase<ILoggingEvent> {

    @Override
    protected String getForegroundColorCode(ILoggingEvent iLoggingEvent) {
        return "38;2;225;230;226";
    }

}
