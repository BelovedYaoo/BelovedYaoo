package top.belovedyaoo.wxapp.controller;

import com.mybatisflex.core.BaseMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.agcore.base.BaseController;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.wxapp.entity.SwiperList;

/**
 * 轮播列表控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/swiperList")
public class SwiperListController extends BaseController<SwiperList> {

    public SwiperListController(BaseMapper<SwiperList> baseMapper, PlatformTransactionManager platformTransactionManager) {
        super(baseMapper, platformTransactionManager);
    }

}
