package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import top.belovedyaoo.openac.model.BaseUser;
import top.belovedyaoo.opencore.toolkit.TypeUtil;

/**
 * 用户服务类基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public abstract class BaseUserService<U extends BaseUser> extends TypeUtil<U> implements IService<U> {

    @Override
    public BaseMapper<U> getMapper() {
        return baseMapper();
    }

    /**
     * 根据openId和密码获取用户信息
     *
     * @param openId   openId
     * @param password 密码
     *
     * @return 用户信息
     */
    public U getUserInfo(String openId, String password) {
        return baseMapper().selectOneByQuery(new QueryWrapper()
                .eq(U.OPEN_ID, openId));
    }

    /**
     * 根据openId获取用户信息
     *
     * @param openId openId
     *
     * @return 用户信息
     */
    public U getUserInfo(String openId) {
        return baseMapper().selectOneByQuery(new QueryWrapper().eq(U.OPEN_ID, openId));
    }

    /**
     * 注册一个用户
     *
     * @param user 用户信息
     *
     * @return 注册结果
     */
    public int register(U user) {
        return baseMapper().insert(user);
    }

    /**
     * 通过OpenID判断用户是否存在
     *
     * @param openId openId
     *
     * @return 用户是否存在
     */
    public boolean userExists(String openId) {
        return baseMapper().selectCountByQuery(new QueryWrapper().eq(U.OPEN_ID, openId)) > 0;
    }

}
