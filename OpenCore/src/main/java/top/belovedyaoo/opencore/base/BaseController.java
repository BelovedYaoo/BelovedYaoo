package top.belovedyaoo.opencore.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import jakarta.annotation.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.opencore.toolkit.TypeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基础控制器
 *
 * @author BelovedYaoo
 * @version 1.7
 */
public abstract class BaseController<T extends BaseFiled> extends TypeUtil<T> {

    /**
     * 事务管理器
     */
    @Resource
    public PlatformTransactionManager platformTransactionManager;

    /**
     * 查询所有数据
     *
     * @return 查询结果
     */
    @GetMapping("/queryAll")
    public Result queryAll() {
        QueryWrapper queryWrapper = QueryWrapper.create().select().from(getOriginalClass()).orderBy(BaseFiled.ORDER_NUM, true);
        List<T> queryList = baseMapper().selectListByQuery(queryWrapper);
        return Result.success().singleData(queryList);
    }

    /**
     * 更新数据
     *
     * @param entity 要更新的数据
     *
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody T entity) {
        String baseId = entity.baseId();
        boolean updateResult = baseMapper().update(entity) > 0;
        if (!updateResult) {
            return Result.failed().message("数据更新失败").description("ID为" + baseId + "的数据更新失败,数据可能不存在");
        }
        return Result.success().message("数据更新成功").description("ID为 " + baseId + " 的数据被更新");
    }

    /**
     * 删除数据
     *
     * @param idList 需要删除的数据的ID列表
     *
     * @return 操作结果
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody List<String> idList) {
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        // List<T> entityList = baseMapper().selectListByIds(idList);
        boolean deleteResult = baseMapper().deleteBatchByIds(idList) == idList.size();
        if (!deleteResult) {
            platformTransactionManager.rollback(transactionStatus);
            return Result.failed().message("数据删除失败").description("存在未能删除的数据,操作已回滚");
        }
        platformTransactionManager.commit(transactionStatus);
        return Result.success().message("数据删除成功").description(idList.size() + "条数据被删除");
    }

    /**
     * 新增数据
     *
     * @param entity 需要添加的数据
     *
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result add(@RequestBody T entity) {
        // 防止注入
        entity.baseId(null);
        boolean addResult = baseMapper().insert(entity) > 0;
        if (addResult) {
            return Result.success().message("数据新增成功");
        }
        return Result.failed().message("数据新增失败");
    }

    /**
     * 重新排序
     *
     * @param leftTarget  左目标
     * @param rightTarget 右目标
     *
     * @return 排序结果
     */
    @PostMapping("/reorder")
    public Result reorder(@RequestParam(value = "leftTarget") int leftTarget, @RequestParam(value = "rightTarget") int rightTarget) {
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        boolean isAsc = leftTarget > rightTarget;
        QueryWrapper queryWrapper = QueryWrapper.create().where(BaseFiled.ORDER_NUM + " BETWEEN " + Math.min(leftTarget, rightTarget) + " AND " + Math.max(leftTarget, rightTarget)).orderBy(BaseFiled.ORDER_NUM, true);
        List<T> originalList = baseMapper().selectListByQuery(queryWrapper);
        // 单独拆出 OrderNum 到一个List
        List<Integer> orderNumList = new ArrayList<>(originalList.stream().map(BaseFiled::orderNum).toList());
        // 平移 OrderNum 列表
        Collections.rotate(orderNumList, isAsc ? -1 : 1);
        for (int i = 0; i < originalList.size(); i++) {
            UpdateChain.of(getOriginalClass())
                    .set(BaseFiled.ORDER_NUM, orderNumList.get(i))
                    .where(BaseFiled.BASE_ID + " = '" + originalList.get(i).baseId() + "'")
                    .update();
        }
        platformTransactionManager.commit(transactionStatus);
        return Result.success().message("数据排序成功");
    }

    /**
     * 顺序交换
     *
     * @param leftTargetBaseId    左目标ID
     * @param leftTargetOrderNum  左目标OrderNum
     * @param rightTargetBaseId   右目标ID
     * @param rightTargetOrderNum 右目标OrderNum
     *
     * @return 交换结果
     */
    @PostMapping("/orderSwap")
    public Result orderSwap(@RequestParam(value = "leftTargetBaseId") String leftTargetBaseId,
                            @RequestParam(value = "leftTargetOrderNum") int leftTargetOrderNum,
                            @RequestParam(value = "rightTargetBaseId") String rightTargetBaseId,
                            @RequestParam(value = "rightTargetOrderNum") int rightTargetOrderNum) {
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        UpdateChain.of(getOriginalClass())
                .set(BaseFiled.ORDER_NUM, rightTargetOrderNum)
                .where(BaseFiled.BASE_ID + " = '" + leftTargetBaseId + "'")
                .update();
        UpdateChain.of(getOriginalClass())
                .set(BaseFiled.ORDER_NUM, leftTargetOrderNum)
                .where(BaseFiled.BASE_ID + " = '" + rightTargetBaseId + "'")
                .update();
        platformTransactionManager.commit(transactionStatus);
        return Result.success().message("顺序交换成功");
    }

}
