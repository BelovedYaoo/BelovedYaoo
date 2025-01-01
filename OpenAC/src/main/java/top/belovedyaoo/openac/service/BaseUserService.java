package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import top.belovedyaoo.openac.enums.UserOperationEnum;
import top.belovedyaoo.openac.model.BaseUser;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.opencore.toolkit.TypeUtil;

/**
 * 用户服务类基类
 *
 * @author BelovedYaoo
 * @version 1.1
 */
public abstract class BaseUserService<U extends BaseUser> extends TypeUtil<U> implements IService<U> {

    @Override
    public BaseMapper<U> getMapper() {
        return baseMapper();
    }

    /**
     * 根据OpenID获取用户信息
     *
     * @param openId OpenID
     *
     * @return 获取结果
     */
    public Result getUserInfo(String openId) {
        U user = baseMapper().selectOneByQuery(new QueryWrapper().eq(U.OPEN_ID, openId));
        // 如果数据不为NULL，则返回用户数据，否则返回用户不存在结果
        if (user != null) {
            return Result.success().singleData(user);
        } else {
            return Result.failed().resultType(UserOperationEnum.NOT_EXIST_OPEN_ID);
        }
    }

    /**
     * 根据OpenID和密码获取用户信息
     *
     * @param openId   OpenID
     * @param password 密码
     *
     * @return 获取结果
     */
    public Result getUserInfo(String openId, String password) {
        Result result = getUserInfo(openId);
        // 如果用户存在，则判断密码是否正确，否则直接返回
        if (result.data() instanceof BaseUser user) {
            // 如果密码正确，则返回用户数据，否则返回密码错误结果
            if (user.password().equals(password)) {
                return Result.success().singleData(user);
            } else {
                return Result.failed().resultType(UserOperationEnum.UNCONFORMITY_PASSWORD);
            }
        } else {
            return result;
        }
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
