package top.belovedyaoo.acs.util;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.tenant.TenantManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import top.belovedyaoo.acs.entity.po.ClassSchedule;
import top.belovedyaoo.acs.entity.po.EnterpriseConfig;
import top.belovedyaoo.acs.entity.po.LinearCurriculum;
import top.belovedyaoo.acs.generateMapper.ClassScheduleMapper;
import top.belovedyaoo.acs.generateMapper.LinearCurriculumMapper;
import top.belovedyaoo.opencore.base.BaseFiled;
import top.belovedyaoo.opencore.tenant.TenantFiled;
import top.belovedyaoo.opencore.result.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * 线性课程表数据工具类
 *
 * @author PrefersMin
 * @version 1.6
 */
@Component
@RequiredArgsConstructor
public class CurriculumDataUtil {

    /**
     * 线性课程表数据接口
     */
    private final LinearCurriculumMapper linearCurriculumMapper;

    /**
     * 课表详细数据视图表接口
     */
    private final ClassScheduleMapper classScheduleMapper;

    /**
     * 事务管理器
     */
    private final PlatformTransactionManager platformTransactionManager;

    /**
     * 重置线性课程表数据
     *
     * @author PrefersMin
     */
    public Result resetCurriculumData(String tenantId) {

        // 开始事务
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            TenantManager.ignoreTenantCondition();
            linearCurriculumMapper.deleteByQuery(QueryWrapper.create().where(TenantFiled.TENANT_ID + " = '" + tenantId + "'"));

            List<ClassSchedule> classScheduleList = classScheduleMapper.selectListByQuery(QueryWrapper.create().select().from(ClassSchedule.class).where(TenantFiled.TENANT_ID + " = '" + tenantId + "'").orderBy(BaseFiled.ORDER_NUM, true));
            List<LinearCurriculum> linearCurriculum = new ArrayList<>();
            for (ClassSchedule classSchedule : classScheduleList) {
                linearCurriculum.addAll(getCurriculumData(classSchedule));
            }
            for (LinearCurriculum lc : linearCurriculum) {
                lc.tenantId(tenantId);
            }
            // 写入至线性课程表
            linearCurriculumMapper.insertBatch(linearCurriculum);
            // 提交事务
            platformTransactionManager.commit(transactionStatus);
            return Result.success().message("重置线性课程表成功");

        } catch (Exception e) {
            // 回滚事务
            platformTransactionManager.rollback(transactionStatus);
            return Result.failed().message("重置线性课程表失败");
        } finally {
            TenantManager.restoreTenantCondition();
        }

    }

    /**
     * 返回课程数据
     *
     * @return 课程数据
     */
    private List<LinearCurriculum> getCurriculumData(ClassSchedule classSchedule) {
        List<LinearCurriculum> linearCurriculumList = new ArrayList<>();
        // 读取每个课表数据中的开始时间和结束时间
        int[] periodArray = getClassTime(classSchedule.schedulePeriod());
        int[] sectionArray = getClassTime(classSchedule.scheduleSection());
        // 读取每个课表数据中的星期
        int week = Integer.parseInt(classSchedule.scheduleWeek());
        // 以周期开始第一层循环
        for (int period = periodArray[0]; period <= periodArray[1]; period++) {
            // 以节次开始第二层循环
            for (int section = sectionArray[0]; section <= sectionArray[1]; section++) {
                // 将对应周期、星期、节次的课程数据写入数组
                linearCurriculumList.add(new LinearCurriculum()
                        .courseName(classSchedule.courseName())
                        .courseVenue(classSchedule.courseVenue())
                        .courseType(classSchedule.courseType())
                        .curriculumPeriod(period)
                        .curriculumWeek(week)
                        .curriculumSection(section));
            }

        }
        return linearCurriculumList;
    }

    /**
     * 通过split方法分割开始时间与结束时间
     *
     * @param classStringTime 上课时间数据
     *
     * @return 返回两个int型的数据作为开始时间与结束时间
     *
     * @author PrefersMin
     */
    public static int[] getClassTime(String classStringTime) {

        // 开始时间与结束时间实体类
        int[] time = new int[2];

        // 分割字符
        String splitString = "-";

        // 判断是否属于时间段
        if (classStringTime.contains(splitString)) {

            // 以 "-" 符号分割开始时间与结束时间
            String[] temp = classStringTime.split("-");

            // 分别设置开始时间与结束时间
            time[0] = Integer.parseInt(temp[0]);
            time[1] = Integer.parseInt(temp[1]);

            // 返回封装好的开始时间与结束时间
            return time;

        }

        // 直接返回数据，并将其同时作为开始时间与结束时间
        time[0] = Integer.parseInt(classStringTime);
        time[1] = Integer.parseInt(classStringTime);

        return time;

    }

    /**
     * 返回今日课表信息
     *
     * @param period 周数
     * @param week   星期
     *
     * @return 今日课表信息
     *
     * @author PrefersMin
     */
    public List<LinearCurriculum> getTodayCurriculumData(EnterpriseConfig enterpriseConfig, int period, int week) {

        List<LinearCurriculum> todayList = TenantManager.withoutTenantCondition(() -> linearCurriculumMapper
                .selectListByQuery(QueryWrapper.create()
                        .select().from(LinearCurriculum.class)
                        .where("curriculum_period = '" + period +
                                "' AND curriculum_week = '" + week +
                                "' AND " + TenantFiled.TENANT_ID +
                                " = '" + enterpriseConfig.tenantId() + "'")
                        .orderBy(BaseFiled.ORDER_NUM, true)));
        return todayList;

    }

}
