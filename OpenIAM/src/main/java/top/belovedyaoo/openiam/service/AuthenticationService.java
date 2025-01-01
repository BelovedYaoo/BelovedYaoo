package top.belovedyaoo.openiam.service;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.openiam.common.toolkit.JedisOperateUtil;
import top.belovedyaoo.openiam.entity.po.ac.User;
import top.belovedyaoo.openiam.enums.AuthenticationResultEnum;
import top.belovedyaoo.openiam.service.impl.BaseUserServiceImpl;
import top.belovedyaoo.openiam.toolkit.AuthenticationUtil;

import static cn.hutool.core.util.ObjectUtil.isNull;

/**
 * 认证服务实现类
 *
 * @author BelovedYaoo
 * @version 1.4
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    /**
     * 注册验证码前缀
     */
    public static final String VERIFY_CODE_PREFIX = "userRegister";

    private final BaseUserServiceImpl userService;

    /**
     * 认证工具类
     */
    private final AuthenticationUtil authenticationUtil;

    public Result getUser(String openId, String password) {
        Result result = userService.getUserInfo(openId, password);
        if (result.data() instanceof User user) {
            // 封禁逻辑
            StpUtil.checkDisable(user.baseId());
            return Result.success().singleData(user);
        }
        return result;
    }

    /**
     * 账号注册方法
     *
     * @param user       账号数据(登录ID、密码)
     * @param usePhone   是否使用手机号注册
     * @param verifyCode 验证码
     *
     * @return 注册结果
     */
    public Result register(User user, boolean usePhone, String verifyCode) {

        // 账户数据绑定检查
        Result dataBindCheckResult = dataBindCheck(user, usePhone);

        if (!dataBindCheckResult.state()) {
            return dataBindCheckResult.state(null);
        }

        // 应用通过检查后的数据
        user = (User) dataBindCheckResult.data();

        // 验证码检查
        String codeBind = usePhone ? user.phone() : user.email();
        if (isNull(codeBind) || codeBind.isEmpty()) {
            return Result.failed().resultType(AuthenticationResultEnum.MUST_USE_PHONE_OR_EMAIL);
        }
        Result verifyResult = authenticationUtil.codeVerify(VERIFY_CODE_PREFIX, codeBind, verifyCode);

        if (!verifyResult.state()) {
            return verifyResult.state(null).message("注册失败");
        }

        // 数据入库
        userService.register(user);

        // 清除验证码
        JedisOperateUtil.del(VERIFY_CODE_PREFIX + ":" + (usePhone ? user.phone() : user.email()));

        return Result.success().description("注册成功");

    }

    /**
     * 检查账户数据是否可以绑定。
     * 首先检查登录ID是否已被使用，然后根据提供的电话号码或电子邮件地址检查唯一绑定是否已存在。
     * 如果登录ID或唯一绑定已存在，将返回相应的错误结果；如果都不存在，将返回成功结果。
     *
     * @param user     账号数据
     * @param usePhone 是否使用手机号码
     *
     * @return 检查结果
     */
    public Result dataBindCheck(User user, boolean usePhone) {

        // 初始化检查结果为失败
        Result accountDataBindCheck = Result.failed().state(false);

        // 检查登录ID是否已被使用
        boolean accountLoginIdAlreadyUse = userService.userExists(user.openId());
        if (accountLoginIdAlreadyUse) {
            return accountDataBindCheck.resultType(AuthenticationResultEnum.LOGIN_ID_ALREADY_USE);
        }

        // 检查绑定数据是否为空
        if (usePhone ? user.phone().isEmpty() : user.email().isEmpty()) {
            return accountDataBindCheck.resultType(AuthenticationResultEnum.MUST_USE_PHONE_OR_EMAIL);
        }

        // 数据绑定检查
        QueryWrapper uniqueBindQuery = new QueryWrapper();

        // 构造查询条件并置空另一个绑定数据与检查结果
        if (usePhone) {
            uniqueBindQuery.eq("phone", user.phone());
            accountDataBindCheck.resultType(AuthenticationResultEnum.PHONE_NUMBER_ALREADY_USE);
            user.email(null);
        } else {
            uniqueBindQuery.eq("email", user.email());
            accountDataBindCheck.resultType(AuthenticationResultEnum.EMAIL_ALREADY_ALREADY_USE);
            user.phone(null);
        }

        // 查询是否已存在对应条件的数据
        boolean uniqueBindAlreadyUse = userService.exists(uniqueBindQuery);

        if (uniqueBindAlreadyUse) {
            return accountDataBindCheck;
        }

        return Result.success().state(true).singleData(user);

    }

}
