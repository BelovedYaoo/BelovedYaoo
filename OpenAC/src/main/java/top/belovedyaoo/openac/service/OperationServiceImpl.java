package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.service.IService;
import top.belovedyaoo.openac.model.Operation;

/**
 * 操作服务类基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class OperationServiceImpl implements IService<Operation> {

    @Override
    public BaseMapper<Operation> getMapper() {
        return Mappers.ofEntityClass(Operation.class);
    }

}
