package top.belovedyaoo.opencore.base;

import com.mybatisflex.core.constant.SqlConsts;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.belovedyaoo.opencore.advice.annotation.OperationMark;
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
 * @version 1.1
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
    @OperationMark(name = "数据排序", desc = "由 OpenCore 自动生成的方法")
    @PostMapping("/reorder")
    default Result reorder(@RequestParam(value = "leftTarget") int leftTarget, @RequestParam(value = "rightTarget") int rightTarget) {
        // 开启事务
        PlatformTransactionManager platformTransactionManager = getPlatformTransactionManager();
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        // 左移 或 右移
        boolean isAsc = leftTarget > rightTarget;
        // 取得所有待排序数据的 BaseID 与 OrderNum
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(BaseIdFiled.BASE_ID, BaseFiled.ORDER_NUM)
                .where(BaseFiled.ORDER_NUM + SqlConsts.BETWEEN + Math.min(leftTarget, rightTarget) + SqlConsts.AND + Math.max(leftTarget, rightTarget))
                .orderBy(BaseFiled.ORDER_NUM, true);
        List<T> originalList = getMapper().selectListByQuery(queryWrapper);
        // 单独拆出 OrderNum 到一个List
        List<Integer> orderNumList = new ArrayList<>(originalList.stream().map(BaseFiled::orderNum).toList());
        // 平移 OrderNum 列表
        Collections.rotate(orderNumList, isAsc ? -1 : 1);
        /*
         * 临时变量加入，避免中间状态重复
         * 为什么要加入一个中间变量？
         * 假如以下是需要更新的数据：ID为4的数据挪到最前
         * |  ID  |  OrderNum  |       |  ID  |  OrderNum  |
         * |  1   |     1      |       |  1   |     2      |
         * |  2   |     2      |   ->  |  2   |     3      |
         * |  3   |     3      |       |  3   |     4      |
         * |  4   |     4      |       |  4   |     1      |
         * 挪移的操作可以近似的看成OrderNum的数组平移
         * -----------------------------------------------------
         * |    ID    |    1    |    2    |    3     |    4    |
         * | OrderNum |    1    |    2    |    3     |    4    |
         * -----------------------------------------------------
         *                        ↓ ↓ ↓
         * ---------------------------------------------------------------
         * |    ID    |         |    1    |    2    |    3     |    4    |
         * | OrderNum |    1    |    2    |    3    |    4     |         |
         * ---------------------------------------------------------------
         *                        ↓ ↓ ↓
         * -----------------------------------------------------
         * |    ID    |    1    |    2    |    3     |    4    |
         * | OrderNum |    2    |    3    |    4     |    1    |
         * -----------------------------------------------------
         * 第一步先把4的数据挪到最前，那么4的OrderNum就变成了1，1的OrderNum就变成了2，以此类推
         * 但是挪4的时候，4的OrderNum就变成了1，和1的OrderNum就重复了
         * 所以，为了避免中间状态重复，我们把4的数据先临时写入一个负值，再把1的数据写入，再把2的数据写入，以此类推
         * 也就是先将第一个需要操作的数据写入临时变量，然后操作剩下的数据，然后回写第一个数据
         */
        Integer orderNumTemp = isAsc ? orderNumList.getLast() : orderNumList.getFirst();
        String baseIdTemp = isAsc ? originalList.getLast().baseId() : originalList.getFirst().baseId();
        // 临时变量写入
        UpdateChain.of(getOriginalClass())
                .set(BaseFiled.ORDER_NUM, -1)
                .where(BaseIdFiled.eqBaseId(baseIdTemp))
                .update();
        if (isAsc) {
            // 从倒数第二个元素开始更新
            for (int i = originalList.size() - 2; i >= 0; i--) {
                UpdateChain.of(getOriginalClass())
                        .set(BaseFiled.ORDER_NUM, orderNumList.get(i))
                        .where(BaseIdFiled.eqBaseId(originalList.get(i).baseId()))
                        .update();
            }
        } else {
            // 从第二个元素开始更新
            for (int i = 1; i < originalList.size(); i++) {
                UpdateChain.of(getOriginalClass())
                        .set(BaseFiled.ORDER_NUM, orderNumList.get(i))
                        .where(BaseIdFiled.eqBaseId(originalList.get(i).baseId()))
                        .update();
            }
        }
        // 回写
        UpdateChain.of(getOriginalClass())
                .set(BaseFiled.ORDER_NUM, orderNumTemp)
                .where(BaseIdFiled.eqBaseId(baseIdTemp))
                .update();
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
    @OperationMark(name = "数据交换顺序", desc = "由 OpenCore 自动生成的方法")
    @PostMapping("/orderSwap")
    default Result orderSwap(@RequestParam(value = "leftTargetBaseId") String leftTargetBaseId,
                             @RequestParam(value = "leftTargetOrderNum") int leftTargetOrderNum,
                             @RequestParam(value = "rightTargetBaseId") String rightTargetBaseId,
                             @RequestParam(value = "rightTargetOrderNum") int rightTargetOrderNum) {
        PlatformTransactionManager platformTransactionManager = getPlatformTransactionManager();
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        // 临时变量写入
        UpdateChain.of(getOriginalClass())
                .set(BaseFiled.ORDER_NUM, "-1")
                .where(BaseIdFiled.eqBaseId(leftTargetBaseId))
                .update();
        UpdateChain.of(getOriginalClass())
                .set(BaseFiled.ORDER_NUM, leftTargetOrderNum)
                .where(BaseIdFiled.eqBaseId(rightTargetBaseId))
                .update();
        UpdateChain.of(getOriginalClass())
                .set(BaseFiled.ORDER_NUM, rightTargetOrderNum)
                .where(BaseIdFiled.eqBaseId(leftTargetBaseId))
                .update();
        platformTransactionManager.commit(transactionStatus);
        return Result.success().message("顺序交换成功");
    }

}
