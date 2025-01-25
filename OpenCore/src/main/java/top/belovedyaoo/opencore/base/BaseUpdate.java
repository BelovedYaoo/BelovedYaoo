package top.belovedyaoo.opencore.base;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.belovedyaoo.opencore.result.Result;

/**
 * 基础控制器更新接口
 *
 * @param <T> 实体类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface BaseUpdate<T extends BaseFiled> extends BaseControllerMethod<T> {

    /**
     * 更新数据
     *
     * @param entity 要更新的数据
     *
     * @return 操作结果
     */
    @PostMapping("/update")
    default Result update(@RequestBody T entity) {
        String baseId = entity.baseId();
        boolean updateResult = baseMapper().update(entity) > 0;
        if (!updateResult) {
            return Result.failed().message("数据更新失败").description("ID为" + baseId + "的数据更新失败,数据可能不存在");
        }
        return Result.success().message("数据更新成功").description("ID为 " + baseId + " 的数据被更新");
    }

}
