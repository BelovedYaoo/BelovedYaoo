package top.belovedyaoo.openac.service;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.belovedyaoo.openac.enums.UserOperationEnum;
import top.belovedyaoo.openac.model.Domain;
import top.belovedyaoo.openac.model.User;
import top.belovedyaoo.opencore.base.BaseController;
import top.belovedyaoo.opencore.result.Result;

/**
 * 用户服务类
 *
 * @author BelovedYaoo
 * @version 1.2
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseController<User> implements IService<User> {

    /**
     * 根据OpenID获取用户信息
     *
     * @param openId OpenID
     *
     * @return 获取结果
     */
    public Result getUserInfo(String openId) {
        User user = getMapper().selectOneByQuery(new QueryWrapper().eq(User.OPEN_ID, openId));
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
        if (result.data() instanceof User user) {
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
    public Result register(User user) {
        // 检查OpenID是否已被注册
        if (userExists(user.openId())) {
            return Result.failed().resultType(UserOperationEnum.ALREADY_USED_OPEN_ID);
        }
        // 检查邮箱是否已被注册
        if (user.email() != null) {
            if (exists(new QueryWrapper().eq(User.EMAIL, user.email()))) {
                return Result.failed().resultType(UserOperationEnum.ALREADY_USED_EMAIL);
            }
        }
        // 检查手机号是否已被注册
        if (user.phone() != null) {
            if (exists(new QueryWrapper().eq(User.PHONE, user.phone()))) {
                return Result.failed().resultType(UserOperationEnum.ALREADY_USED_PHONE);
            }
        }
        // 用户数据入库
        user.baseId(null);
        if (getMapper().insert(user) == 1) {
            return Result.success().state(true);
        } else {
            return Result.failed().resultType(UserOperationEnum.UNKNOWN_ERROR);
        }
    }

    /**
     * 通过OpenID判断用户是否存在
     *
     * @param openId openId
     *
     * @return 用户是否存在
     */
    public boolean userExists(String openId) {
        return getMapper().selectCountByQuery(new QueryWrapper().eq(User.OPEN_ID, openId)) > 0;
    }

}
