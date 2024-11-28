package top.belovedyaoo.acs.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.tenant.TenantManager;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.belovedyaoo.acs.entity.po.ClassSchedule;
import top.belovedyaoo.acs.entity.po.EnterpriseConfig;
import top.belovedyaoo.acs.entity.po.LinearCurriculum;
import top.belovedyaoo.acs.entity.vo.Weather;
import top.belovedyaoo.acs.enums.PushMode;
import top.belovedyaoo.acs.generateMapper.ClassScheduleMapper;
import top.belovedyaoo.acs.util.ApiUtil;
import top.belovedyaoo.acs.util.CurriculumDataUtil;
import top.belovedyaoo.acs.util.DateUtil;
import top.belovedyaoo.acs.util.LogUtil;
import top.belovedyaoo.acs.util.PushDataUtil;
import top.belovedyaoo.agcore.base.BaseFiled;
import top.belovedyaoo.agcore.base.BaseTenantFiled;
import top.belovedyaoo.agcore.result.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static top.belovedyaoo.acs.util.DateUtil.getNow;

/**
 * 推送服务
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class PushService {

    private final ApiUtil apiUtil;

    private final DateUtil dateUtil;

    private final CurriculumDataUtil curriculumDataUtil;

    private final ClassScheduleMapper classScheduleMapper;

    /**
     * 企业微信消息接口
     */
    private final SendMessageService sendMessageService;

    /**
     * 微信服务返回结果
     */
    private WxCpMessageSendResult wxCpMessageSendResult;

    public Result pushCourse(EnterpriseConfig enterpriseConfig) {

        // 根据当前时间设置标题
        String title = PushDataUtil.getTitleForCurrentTimePeriod();
        String yiyan = apiUtil.getCaiHongPi(enterpriseConfig.tianApiKey());
        int termBegin = DateUtil.daysBetween(enterpriseConfig.dateStarting(), getNow());
        int vocationBegin = DateUtil.daysBetween(getNow(), enterpriseConfig.dateEnding());
        Weather weather = apiUtil.getWeather(enterpriseConfig);

        // 微信小表情网站：https://www.emojiall.com/zh-hans/platform-wechat
        // 创建一个可变字符串类作为容器用以追加字符串到消息序列
        StringBuilder message = new StringBuilder();

        // 根据开学日期进行判断
        if (termBegin >= 0) {
            LogUtil.info("开学天数：" + termBegin);

            // 计算当前推送周期、获取推送时间、计算当前推送星期
            int pushMode = enterpriseConfig.pushMode();
            int period = dateUtil.getPeriod(enterpriseConfig);
            int week = dateUtil.getW(enterpriseConfig);


            // 根据周期与星期获取五大节课程数据
            List<LinearCurriculum> curriculumDataList = curriculumDataUtil.getTodayCurriculumData(enterpriseConfig, period, week);

            // 非空判断五大节课程数据(与逻辑)
            if (curriculumDataList.isEmpty()) {
                // 五大节课程数据都为空，跳过当天的推送
                LogUtil.info("当前没有课程，跳过推送");
                return Result.success().message("触发推送成功").description("当前没有课程，跳过推送");
            }

            // 消息内容
            // 根据推送时间判断天气推送提示
            message.append("\n\uD83D\uDCCD").append(weather.area()).append(PushMode.NIGHT.getValue() == pushMode ? "明日" : "今日").append("天气");
            message.append("\n\uD83C\uDF25气象：").append(weather.weather());
            message.append("\n\uD83C\uDF21温度：").append(weather.lowest()).append("℃~").append(weather.highest()).append("℃\n");

            // 距离开学日天数统计
            if (termBegin == 0) {
                message.append("\n\uD83C\uDF92今天是开学日噢~");
            } else {
                message.append("\n\uD83C\uDF92你已经上了").append(termBegin).append("天的课了~");
            }


            // 距离放假日天数判断
            if (vocationBegin == 1) {
                message.append("\n\n\uD83C\uDF92明天就放假咯~");
            } else {
                message.append("\n\uD83C\uDFC1距离放假还有").append(vocationBegin - pushMode).append("天");
            }

            // 彩虹屁非空判断
            if (StringUtils.isNotEmpty(yiyan)) {
                message.append("\n\n\uD83D\uDC8C").append(yiyan);
            }

            // 循环推送多个用户
            try {
                wxCpMessageSendResult = doPush(title, message,enterpriseConfig);
            } catch (Exception e) {
                return Result.failed().message(e.getMessage());
            }

            // 清空内容
            title = "";
            message.setLength(0);

            // 根据推送时间设置标题
            if (PushMode.NIGHT.getValue() == pushMode) {
                title = "\uD83C\uDF08明天是";
            } else {
                title = "\uD83C\uDF08今天是";
            }

            title = title + weather.date() + " 第" + period + "周 " + dateUtil.getWeek(pushMode);
            // 设置课程信息并统计课程节数
            courseSet(message, curriculumDataList);

            try {
                wxCpMessageSendResult = doPush(title, message,enterpriseConfig);
            } catch (Exception e) {
                return Result.failed().message(e.getMessage());
            }

            return Result.success().message("课程推送成功").description("ID为：" + wxCpMessageSendResult.getMsgId() + "的消息推送成功");

        } else if (termBegin == -1) {
            return startPush(enterpriseConfig, weather, message);
        } else {
            LogUtil.info("开学日期" + termBegin);
            return Result.success().message("没有开学，停止推送");
        }
    }

    /**
     * 五大节课程非空判断、统计早晚课天数与总课程数
     *
     * @param message 传入的message
     *
     * @author PrefersMin
     */
    private void courseSet(StringBuilder message, List<LinearCurriculum> curriculumDataList) {

        String[] selectionStringArray = {"\n1️⃣第一大节：", "\n2️⃣第二大节：", "\n3️⃣第三大节：", "\n4️⃣第四大节：", "\n5️第五大节："};

        curriculumDataList.forEach(curriculumData -> {
            int curriculumSection = curriculumData.curriculumSection() - 1;
            message.append(selectionStringArray[curriculumSection]);
            message.append(curriculumData.courseName());
            message.append("\n\uD83D\uDE8F上课地点：").append(curriculumData.courseVenue()).append("\n");
        });
    }

    /**
     * 开学日推送
     *
     * @param message 传入的message
     *
     * @author PrefersMin
     */
    private Result startPush(EnterpriseConfig enterpriseConfig, Weather weather, StringBuilder message) {
        LogUtil.info("开学日期" + enterpriseConfig.dateStarting());
        // 标题
        String title = "\uD83C\uDF92明天是开学日噢，明晚开始推送课程信息~";

        // 获取所有课程信息，并提取其中的课程名称
        List<ClassSchedule> courseData = TenantManager.withoutTenantCondition(() ->
                classScheduleMapper.selectListByQuery(
                        QueryWrapper.create().select()
                                .from(ClassSchedule.class)
                                .where(BaseTenantFiled.TENANT_ID + " = '" + enterpriseConfig.tenantId())
                                .orderBy(BaseFiled.ORDER_NUM, true)));
        List<String> courseNames = courseData.stream().map(ClassSchedule::courseName).collect(Collectors.toList());
        // 通过TreeSet对课程名称进行去重
        courseNames = new ArrayList<>(new TreeSet<>(courseNames));

        // 计算课程门数，并追加课程名称到消息序列
        message.append("\n\uD83D\uDCDA本学期共有").append(courseNames.size()).append("门课程\n\n");
        for (String courseName : courseNames) {
            message.append(courseName).append("\n\n");
        }

        // 提醒明天的天气与温度
        message.append("\n\uD83C\uDF25明天的气象：").append(weather.weather());
        message.append("\n\uD83C\uDF21温度：").append(weather.lowest()).append("~").append(weather.highest()).append("\n");

        message.append("\n\uD83C\uDFC4\uD83C\uDFFB\u200D♀️新学期马上开始咯\n");

        try {
            doPush(title, message,enterpriseConfig);
        } catch (Exception e) {
            return Result.failed().message(e.getMessage());
        }

        return Result.success().message("开学日推送成功");
    }

    /**
     * 推送课程
     *
     * @param title   推送标题
     * @param message 推送消息
     *
     * @return 返回推送结果
     *
     * @author PrefersMin
     */
    private WxCpMessageSendResult doPush(String title, StringBuilder message, EnterpriseConfig enterpriseConfig) {

        // 循环推送多个用户
        wxCpMessageSendResult = sendMessageService.pushCourse(title, message.toString(),enterpriseConfig);

        if (wxCpMessageSendResult.getErrCode() != 0) {
            System.out.println("课程推送失败");
        }

        return wxCpMessageSendResult;

    }

}
