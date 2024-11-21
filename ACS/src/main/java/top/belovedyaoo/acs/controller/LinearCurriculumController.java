package top.belovedyaoo.acs.controller;

import com.mybatisflex.core.BaseMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.acs.entity.po.LinearCurriculum;
import top.belovedyaoo.agcore.base.BaseController;

/**
 * 线性课程表控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/linearCurriculum")
public class LinearCurriculumController extends BaseController<LinearCurriculum> {

    public LinearCurriculumController(BaseMapper<LinearCurriculum> baseMapper, PlatformTransactionManager platformTransactionManager) {
        super(baseMapper, platformTransactionManager);
    }

}
