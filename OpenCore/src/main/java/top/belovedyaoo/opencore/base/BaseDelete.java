package top.belovedyaoo.opencore.base;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.belovedyaoo.opencore.result.Result;

import java.util.List;

/**
 * 基础控制器删除接口
 *
 * @param <T> 实体类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface BaseDelete<T extends BaseFiled> extends BaseControllerMethod<T> {

    /**
     * 删除数据
     *
     * @param idList 需要删除的数据的ID列表
     *
     * @return 操作结果
     */
    @PostMapping("/delete")
    default Result delete(@RequestBody List<String> idList) {
        PlatformTransactionManager platformTransactionManager = getPlatformTransactionManager();
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

}
