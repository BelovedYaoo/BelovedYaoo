package top.belovedyaoo.openiam.service;

import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.openiam.entity.po.User;

/**
 * 认证服务
 *
 * @author BelovedYaoo
 * @version 1.1
 */
public interface AuthenticationService {

    /**
     * 账号登录方法
     *
     * @param user 账号数据(登录ID、密码)
     *
     * @return 登录结果
     */
    Result accountLogin(User user);

    /**
     * 第三方登录方法
     *
     * @param openId   OpenID
     * @param password 密码
     *
     * @return 登录结果
     */
    Result openLogin(String openId, String password);

    /**
     * 账号注册方法
     *
     * @param user 账号数据(登录ID、密码)
     * @param usePhone    是否使用手机号注册
     * @param verifyCode  验证码
     *
     * @return 注册结果
     */
    Result register(User user, boolean usePhone, String verifyCode);

    /**
     * 检查账户数据是否可以绑定。
     * 首先检查登录ID是否已被使用，然后根据提供的电话号码或电子邮件地址检查唯一绑定是否已存在。
     * 如果登录ID或唯一绑定已存在，将返回相应的错误结果；如果都不存在，将返回成功结果。
     *
     * @param user 账号数据
     * @param usePhone    是否使用手机号码
     *
     * @return 检查结果
     */
    Result dataBindCheck(User user, boolean usePhone);

}
