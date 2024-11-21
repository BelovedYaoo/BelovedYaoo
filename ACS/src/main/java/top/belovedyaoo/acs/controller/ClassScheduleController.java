package top.belovedyaoo.acs.controller;

import com.mybatisflex.core.BaseMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.acs.entity.po.ClassSchedule;
import top.belovedyaoo.agcore.base.BaseController;

/**
 * 课程表控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/classSchedule")
public class ClassScheduleController extends BaseController<ClassSchedule> {

    public ClassScheduleController(BaseMapper<ClassSchedule> baseMapper, PlatformTransactionManager platformTransactionManager) {
        super(baseMapper, platformTransactionManager);
    }

}
