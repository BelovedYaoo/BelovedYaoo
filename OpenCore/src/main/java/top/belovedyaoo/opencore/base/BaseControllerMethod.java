package top.belovedyaoo.opencore.base;

import com.mybatisflex.core.BaseMapper;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 控制器方法接口
 *
 * @param <T> 实体类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface BaseControllerMethod<T extends BaseFiled> {

    /**
     * 获取泛型类型对应的Class对象
     *
     * @return 具体Class对象
     */
    Class<T> getOriginalClass();

    /**
     * 获取事务管理器
     *
     * @return 事务管理器
     */
    PlatformTransactionManager getPlatformTransactionManager();

    /**
     * 获取泛型的Mapper
     * @return Mapper
     */
    BaseMapper<T> baseMapper();

}
