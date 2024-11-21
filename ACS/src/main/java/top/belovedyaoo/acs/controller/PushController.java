package top.belovedyaoo.acs.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.acs.entity.po.EnterpriseConfig;
import top.belovedyaoo.acs.generateMapper.EnterpriseConfigMapper;
import top.belovedyaoo.acs.service.PushService;
import top.belovedyaoo.acs.util.CurriculumDataUtil;
import top.belovedyaoo.agcore.result.Result;

/**
 * 推送服务
 *
 * @author PrefersMin
 * @version 2.0
 */
@RestController
@RequiredArgsConstructor
public class PushController {

    private final CurriculumDataUtil curriculumDataUtil;

    private final EnterpriseConfigMapper enterpriseConfigMapper;

    private final PushService pushService;

    /**
     * 课程推送主方法
     *
     * @author PrefersMin
     */
    @GetMapping("/pushCourse")
    public Result pushCourse() {
        EnterpriseConfig ec = enterpriseConfigMapper.selectOneByQuery(QueryWrapper.create().from(EnterpriseConfig.class).where(EnterpriseConfig.TENANT_ID + " = '" + StpUtil.getLoginIdAsString() + "'"));
        pushService.pushCourse(ec);
        return Result.success();

    }

    /**
     * 重置线性课程表
     *
     * @author PrefersMin
     *
     * @return 返回重置结果
     */
    @GetMapping("/resetCurriculumData")
    public Result resetCurriculumData() {

        boolean resetResult = curriculumDataUtil.resetCurriculumData(StpUtil.getLoginIdAsString()).code() == 200;
        if (!resetResult) {
            return Result.failed().message("重置失败").description("课程推送队列重置失败");
        }
        return Result.success().message("重置成功").description("课程推送队列重置成功");

    }

}
