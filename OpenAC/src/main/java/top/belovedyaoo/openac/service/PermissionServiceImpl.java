package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.service.IService;
import top.belovedyaoo.openac.model.Permission;

/**
 * 权限服务类基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class PermissionServiceImpl implements IService<Permission> {

    @Override
    public BaseMapper<Permission> getMapper() {
        return Mappers.ofEntityClass(Permission.class);
    }

}
