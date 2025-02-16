package top.belovedyaoo.openiam.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.belovedyaoo.openac.model.User;
import top.belovedyaoo.openac.service.UserServiceImpl;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.opencore.toolkit.JedisOperateUtil;
import top.belovedyaoo.openiam.enums.AuthenticationResultEnum;
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

    private final UserServiceImpl userService;

    /**
     * 认证工具类
     */
    private final AuthenticationUtil authenticationUtil;

    public Result getUser(String openId, String password) {
        Result result = userService.getUserInfo(openId, password);
        if (result.data() instanceof User user) {
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
        String codeBind;
        if (usePhone) {
            user.email(null);
            codeBind = user.phone();
        } else {
            user.phone(null);
            codeBind = user.email();
        }
        // 验证码检查
        if (isNull(codeBind) || codeBind.isEmpty()) {
            return Result.failed().resultType(AuthenticationResultEnum.MUST_USE_PHONE_OR_EMAIL);
        }
        Result verifyResult = authenticationUtil.codeVerify(VERIFY_CODE_PREFIX, codeBind, verifyCode);
        // 如果验证码校验失败，返回错误信息
        if (!verifyResult.state()) {
            return verifyResult.state(null).message("注册失败");
        }
        // 数据入库
        Result result = userService.register(user);
        if (!result.state()) {
            return result;
        }
        // 清除验证码
        JedisOperateUtil.del(VERIFY_CODE_PREFIX + ":" + (usePhone ? user.phone() : user.email()));
        return Result.success().description("注册成功");

    }

}
