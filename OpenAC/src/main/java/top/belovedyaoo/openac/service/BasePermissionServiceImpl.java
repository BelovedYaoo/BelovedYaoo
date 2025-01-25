package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.service.IService;
import top.belovedyaoo.openac.model.BasePermission;

/**
 * 权限服务类基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class BasePermissionServiceImpl implements IService<BasePermission> {

    @Override
    public BaseMapper<BasePermission> getMapper() {
        return Mappers.ofEntityClass(BasePermission.class);
    }

}
