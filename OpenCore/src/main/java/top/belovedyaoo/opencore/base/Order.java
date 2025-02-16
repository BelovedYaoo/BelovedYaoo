package top.belovedyaoo.opencore.base;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.belovedyaoo.opencore.result.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基础控制器排序接口
 *
 * @param <T> 实体类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface Order<T extends BaseFiled> extends BaseControllerMethod<T> {

    /**
     * 重新排序
     *
     * @param leftTarget  左目标
     * @param rightTarget 右目标
     *
     * @return 排序结果
     */
    @PostMapping("/reorder")
    default Result reorder(@RequestParam(value = "leftTarget") int leftTarget, @RequestParam(value = "rightTarget") int rightTarget) {
        PlatformTransactionManager platformTransactionManager = getPlatformTransactionManager();
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        boolean isAsc = leftTarget > rightTarget;
        QueryWrapper queryWrapper = QueryWrapper.create().where(BaseFiled.ORDER_NUM + " BETWEEN " + Math.min(leftTarget, rightTarget) + " AND " + Math.max(leftTarget, rightTarget)).orderBy(BaseFiled.ORDER_NUM, true);
        List<T> originalList = getMapper().selectListByQuery(queryWrapper);
        // 单独拆出 OrderNum 到一个List
        List<Integer> orderNumList = new ArrayList<>(originalList.stream().map(BaseFiled::orderNum).toList());
        // 平移 OrderNum 列表
        Collections.rotate(orderNumList, isAsc ? -1 : 1);
        for (int i = 0; i < originalList.size(); i++) {
            UpdateChain.of(getOriginalClass())
                    .set(BaseFiled.ORDER_NUM, orderNumList.get(i))
                    .where(BaseFiled.eqBaseId(originalList.get(i).baseId()))
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
    default Result orderSwap(@RequestParam(value = "leftTargetBaseId") String leftTargetBaseId,
                             @RequestParam(value = "leftTargetOrderNum") int leftTargetOrderNum,
                             @RequestParam(value = "rightTargetBaseId") String rightTargetBaseId,
                             @RequestParam(value = "rightTargetOrderNum") int rightTargetOrderNum) {
        PlatformTransactionManager platformTransactionManager = getPlatformTransactionManager();
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        UpdateChain.of(getOriginalClass())
                .set(BaseFiled.ORDER_NUM, rightTargetOrderNum)
                .where(BaseFiled.eqBaseId(leftTargetBaseId))
                .update();
        UpdateChain.of(getOriginalClass())
                .set(BaseFiled.ORDER_NUM, leftTargetOrderNum)
                .where(BaseFiled.eqBaseId(rightTargetBaseId))
                .update();
        platformTransactionManager.commit(transactionStatus);
        return Result.success().message("顺序交换成功");
    }

}
