package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.service.IService;
import top.belovedyaoo.openac.model.Domain;

/**
 * 域服务类基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class DomainServiceImpl implements IService<Domain> {

    @Override
    public BaseMapper<Domain> getMapper() {
        return Mappers.ofEntityClass(Domain.class);
    }

}
