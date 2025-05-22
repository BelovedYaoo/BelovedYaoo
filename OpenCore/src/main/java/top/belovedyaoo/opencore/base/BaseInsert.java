package top.belovedyaoo.opencore.base;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.belovedyaoo.opencore.common.OperationMark;
import top.belovedyaoo.opencore.result.Result;

/**
 * 基础控制器插入接口
 *
 * @param <T> 实体类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface BaseInsert<T extends BaseFiled> extends BaseControllerMethod<T> {

    /**
     * 新增数据
     *
     * @param entity 需要添加的数据
     *
     * @return 操作结果
     */
    @OperationMark(name = "新增一条数据", desc = "由 OpenCore 自动生成的方法")
    @PostMapping("/add")
    default Result add(@RequestBody T entity) {
        // 防止注入
        entity.baseId(null);
        boolean addResult = save(entity);
        if (addResult) {
            return Result.success().message("数据新增成功");
        }
        return Result.failed().message("数据新增失败");
    }

}
