package top.belovedyaoo.acs.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.belovedyaoo.acs.entity.po.EnterpriseConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;

/**
 * 日期工具类
 *
 * @author PrefersMin
 * @version 1.9
 */
@Component
@RequiredArgsConstructor
public class DateUtil {

    /**
     * 格式化
     */
    private static final ThreadLocal<SimpleDateFormat> LOCAL_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    /**
     * 推送数据的工具类
     */
    private final PushDataUtil pushDataUtil;

    /**
     * 计算两个日期(String类型)之间相差多少天
     *
     * @param startDateString 开始日期(String类型)
     * @param endDateString   结束日期(String类型)
     *
     * @return 返回相差的天数(String类型)
     *
     * @author PrefersMin
     */
    public static int daysBetween(String startDateString, String endDateString) {
        long nd = 1000 * 24 * 60 * 60;

        // 转换时间格式
        Date startDate, endDate;
        try {
            startDate = LOCAL_FORMAT.get().parse(startDateString);
            endDate = LOCAL_FORMAT.get().parse(endDateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 计算差多少天
        long diff = getDiff(startDate, endDate);

        return (int) (diff / nd);
    }

    /**
     * 计算两个日期(Date类型)之间相差多少天
     *
     * @param startDate 开始日期(String类型)
     * @param endDate   结束日期(String类型)
     *
     * @return 返回相差的天数(long类型)
     *
     * @author PrefersMin
     */
    public static long getDiff(Date startDate, Date endDate) {
        return (endDate.getTime()) - (startDate.getTime());
    }

    /**
     * 获取当前日期(String类型)
     *
     * @return 当前日期(String类型)
     *
     * @author PrefersMin
     */
    public static String getNow() {
        Date now = new Date(System.currentTimeMillis());
        return LOCAL_FORMAT.get().format(now);
    }

    /**
     * 获取当前的星期
     *
     * @return 当前的星期
     *
     * @author PrefersMin
     */
    public int getW(EnterpriseConfig enterpriseConfig) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }

        w = w % 7;

        // 根据推送时间偏移星期
        w = (w + enterpriseConfig.pushMode()) % 8;

        return w;
    }

    /**
     * 获取当前周数(int类型)
     *
     * @return 当前周数(int类型)
     *
     * @author PrefersMin
     */
    public int getPeriod(EnterpriseConfig enterpriseConfig) {

        int period, periods;
        String date = LOCAL_FORMAT.get().format(new Date());

        periods = DateUtil.daysBetween(enterpriseConfig.dateStarting(), date);

        period = ((periods + enterpriseConfig.pushMode()) / 7) + 1;

        // 周数校验
        if (abs(period) > PushDataUtil.PERIOD_MAX) {
            period = 0;
            LogUtil.error("错误：周数超过课表最大周数限制，将会引发数组越界错误，已归零");
        } else if (period < 0) {
            period = abs(period);
            LogUtil.error("错误：周数为负数将会引发数组越界错误，已取绝对值");
        }

        return period;

    }

    /**
     * 根据日期判断当前星期
     *
     * @param pushMode 当前日期
     *
     * @return 返回当前星期
     *
     * @author PrefersMin
     */
    public String getWeek(int pushMode) {

        String[] week = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1 + pushMode;
        if (w < 0) {
            w = 0;
        }
        w = w % 7;
        return week[w];

    }

}
