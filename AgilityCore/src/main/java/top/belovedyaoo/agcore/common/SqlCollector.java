package top.belovedyaoo.agcore.common;

import com.mybatisflex.core.audit.AuditMessage;
import com.mybatisflex.core.audit.MessageCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.belovedyaoo.agcore.toolkit.TimeUtil;

/**
 * 控制台消息格式化
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class SqlCollector implements MessageCollector {

    Logger log = LoggerFactory.getLogger(SqlCollector.class);

    /**
     * 定义ANSI转义码常量
     */
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private ConsolePrinter console = (message) -> {
        System.out.println(
                ANSI_CYAN + TimeUtil.getFullCurrentTime() +
                        ANSI_PURPLE + " INFO " +
                        ANSI_GREEN + " [flex] " +
                        ANSI_BLUE + "[" + String.format("%-30.30s", (message.getPlatform() + " exec SQL took " + message.getElapsedTime() + " ms")) + "]" +
                        ANSI_RESET + " -> " +
                        ANSI_WHITE + message.getFullSql());
    };

    private LoggerPrinter logger = (message) -> {
        String sql = message.getFullSql();
        Long tookTimeMillis = message.getElapsedTime();
        log.info("{} exec SQL took {} ms", message.getDsName(), tookTimeMillis);
        log.info(ANSI_WHITE + "{}", sql);
    };

    public SqlCollector() {
    }

    public SqlCollector(ConsolePrinter console) {
        this.console = console;
    }

    public SqlCollector(LoggerPrinter logger) {
        this.logger = logger;
    }

    @Override
    public void collect(AuditMessage message) {
        // this.console.console(message);
        this.logger.logger(message);
    }

    public interface ConsolePrinter {
        /**
         * 通过控制台打印消息
         *
         * @param message 消息
         */
        void console(AuditMessage message);
    }

    public interface LoggerPrinter {
        /**
         * 通过日志打印消息
         * @param message 消息
         */
        void logger(AuditMessage message);
    }

}
