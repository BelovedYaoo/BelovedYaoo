package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.service.IService;
import top.belovedyaoo.openac.model.Role;

/**
 * 角色服务类基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class RoleServiceImpl implements IService<Role> {

    @Override
    public BaseMapper<Role> getMapper() {
        return Mappers.ofEntityClass(Role.class);
    }

}
