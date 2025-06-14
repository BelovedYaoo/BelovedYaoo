package top.belovedyaoo.opencore.base;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.transaction.PlatformTransactionManager;
import top.belovedyaoo.opencore.toolkit.TypeUtil;

/**
 * 基础控制器
 *
 * @author BelovedYaoo
 * @version 1.7
 */
@Getter
public abstract class BaseController<T extends BaseFiled> extends TypeUtil<T> {

    /**
     * 事务管理器
     */
    @Resource
    public PlatformTransactionManager platformTransactionManager;

    public BaseMapper<T> getMapper() {
        return Mappers.ofEntityClass(getOriginalClass());
    }

}
