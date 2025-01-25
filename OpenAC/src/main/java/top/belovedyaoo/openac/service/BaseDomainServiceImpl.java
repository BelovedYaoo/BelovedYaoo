package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.service.IService;
import top.belovedyaoo.openac.model.BaseDomain;

/**
 * 域服务类基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class BaseDomainServiceImpl implements IService<BaseDomain> {

    @Override
    public BaseMapper<BaseDomain> getMapper() {
        return Mappers.ofEntityClass(BaseDomain.class);
    }

}
