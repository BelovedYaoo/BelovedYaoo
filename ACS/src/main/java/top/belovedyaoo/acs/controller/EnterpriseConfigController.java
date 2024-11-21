package top.belovedyaoo.acs.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.BaseMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.acs.entity.po.EnterpriseConfig;
import top.belovedyaoo.acs.entity.po.LinearCurriculum;
import top.belovedyaoo.acs.task.TaskManager;
import top.belovedyaoo.agcore.base.BaseController;
import top.belovedyaoo.agcore.result.Result;

import javax.annotation.Resource;

/**
 * 企业配置表控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/enterpriseConfig")
public class EnterpriseConfigController extends BaseController<EnterpriseConfig> {

    @Resource
    TaskManager taskManager;

    public EnterpriseConfigController(BaseMapper<EnterpriseConfig> baseMapper, PlatformTransactionManager platformTransactionManager) {
        super(baseMapper, platformTransactionManager);
    }

    @Override
    public Result update(EnterpriseConfig entity) {
        String baseId = entity.baseId();
        boolean updateResult = baseMapper.update(entity) > 0;
        if (!updateResult) {
            return Result.failed().message("数据更新失败").description("ID为" + baseId + "的数据更新失败,数据可能不存在");
        }
        return Result.success().message("数据更新成功").description("ID为 " + baseId + " 的数据被更新");
    }

}