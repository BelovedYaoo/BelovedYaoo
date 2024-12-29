package top.belovedyaoo.opencore.toolkit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class TimeUtil {

    /**
     * 获取当前时间，并格式化
     * @param pattern 格式化参数
     * @return 格式化后的时间
     */
    public static String getCurrentTimeOfPattern(String pattern) {
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        // 返回格式化后的日期时间
        return now.format(formatter);
    }

    /**
     * 获取当前时间，完整格式
     * @return 完整格式的时间
     */
    public static String getFullCurrentTime() {
        return getCurrentTimeOfPattern("yyyy-MM-dd HH:mm:ss.SSS");
    }

}
