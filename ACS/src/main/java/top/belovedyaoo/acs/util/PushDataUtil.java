package top.belovedyaoo.acs.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * 推送数据的工具类
 *
 * @author PrefersMin
 * @version 1.9
 */
@Component
@RequiredArgsConstructor
public class PushDataUtil {

    /**
     * 最大周数、星期、节数
     */
    public static final int PERIOD_MAX = 23;
    public static final int WEEK_MAX = 8;
    public static final int SECTION_MAX = 6;


    public static String getTitleForCurrentTimePeriod() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        if (hour >= 6 && hour < 12) {
            return "\uD83C\uDF1E早上好~";
        } else if (hour >= 12 && hour < 18) {
            return "🕜️下午好~";
        } else if (hour >= 18) {
            return "\uD83C\uDF1F晚上好~";
        } else {
            return "🌙凌晨了,早点休息~";
        }
    }

}
