package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.service.IService;
import top.belovedyaoo.openac.model.BaseRole;

/**
 * 角色服务类基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class BaseRoleServiceImpl implements IService<BaseRole> {

    @Override
    public BaseMapper<BaseRole> getMapper() {
        return Mappers.ofEntityClass(BaseRole.class);
    }

}
