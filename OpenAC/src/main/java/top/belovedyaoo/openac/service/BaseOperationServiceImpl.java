package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.service.IService;
import top.belovedyaoo.openac.model.BaseOperation;

/**
 * 操作服务类基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class BaseOperationServiceImpl implements IService<BaseOperation> {

    @Override
    public BaseMapper<BaseOperation> getMapper() {
        return Mappers.ofEntityClass(BaseOperation.class);
    }

}
