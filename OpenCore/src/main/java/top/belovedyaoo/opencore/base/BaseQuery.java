package top.belovedyaoo.opencore.base;

import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import top.belovedyaoo.opencore.result.Result;

import java.util.List;

/**
 * 基础控制器查询接口
 *
 * @param <T> 实体类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface BaseQuery<T extends BaseFiled> extends BaseControllerMethod<T> {

    /**
     * 查询所有数据
     *
     * @return 查询结果
     */
    @GetMapping("/queryAll")
    default Result queryAll() {
        QueryWrapper queryWrapper = QueryWrapper.create().select().from(getOriginalClass()).orderBy(BaseFiled.ORDER_NUM, true);
        List<T> queryList = baseMapper().selectListByQuery(queryWrapper);
        return Result.success().singleData(queryList);
    }

}
