package top.belovedyaoo.openiam.service.impl;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.openiam.generateMapper.AccountMapper;
import top.belovedyaoo.openiam.entity.po.Account;
import top.belovedyaoo.openiam.toolkit.AuthenticationUtil;
import top.belovedyaoo.openiam.enums.AuthenticationResultEnum;
import top.belovedyaoo.openiam.common.toolkit.JedisOperateUtil;
import top.belovedyaoo.openiam.service.AuthenticationService;

import static cn.hutool.core.util.ObjectUtil.isNull;

/**
 * 认证服务实现类
 *
 * @author BelovedYaoo
 * @version 1.3
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * 注册验证码前缀
     */
    public static final String VERIFY_CODE_PREFIX = "accountRegister";

    private final AccountMapper accountMapper;

    /**
     * 认证工具类
     */
    private final AuthenticationUtil authenticationUtil;

    /**
     * 账号登录方法
     *
     * @param accountData 账号数据(登录ID、密码)
     *
     * @return 登录结果
     */
    @Override
    public Result accountLogin(Account accountData) {

        Account account = accountMapper.selectOneByQuery(new QueryWrapper()
                .eq("open_id", accountData.openId()));

        // 账号不存在
        if (account == null) {
            return Result.failed().resultType(AuthenticationResultEnum.ACCOUNT_LOGIN_ID_INVALID);
        }

        // 密码错误
        if (!account.password().equals(accountData.password())) {
            return Result.failed().resultType(AuthenticationResultEnum.ACCOUNT_PASSWORD_ERROR);
        }

        // 封禁逻辑
        StpUtil.checkDisable(account.baseId());

        // Sa-Token登录
        StpUtil.login(accountData.openId(), SaLoginConfig.setExtra("openId", accountData.openId()).setDevice("Default"));

        return Result.success().description("登录成功")
                .data("account", account)
                .data("tokenValue", StpUtil.getTokenValue());

    }

    @Override
    public Result openLogin(String openId, String password) {
        Account account = accountMapper.selectOneByQuery(new QueryWrapper()
                .eq("open_id", openId));

        // 账号不存在
        if (account == null) {
            return Result.failed().resultType(AuthenticationResultEnum.ACCOUNT_LOGIN_ID_INVALID);
        }

        // 密码错误
        if (!account.password().equals(password)) {
            return Result.failed().resultType(AuthenticationResultEnum.ACCOUNT_PASSWORD_ERROR);
        }

        // 封禁逻辑
        StpUtil.checkDisable(account.baseId());

        // Sa-Token登录
        StpUtil.login(account.baseId());

        return Result.success().description("登录成功")
                .data("account", account)
                .data("tokenValue", StpUtil.getTokenValue());
    }

    /**
     * 账号注册方法
     *
     * @param accountData 账号数据(登录ID、密码)
     * @param usePhone    是否使用手机号注册
     * @param verifyCode  验证码
     *
     * @return 注册结果
     */
    @Override
    public Result accountRegister(Account accountData, boolean usePhone, String verifyCode) {

        // 账户数据绑定检查
        Result accountDataBindCheckResult = accountDataBindCheck(accountData, usePhone);

        if (!accountDataBindCheckResult.state()) {
            return accountDataBindCheckResult.state(null);
        }

        // 应用通过检查后的数据
        accountData = (Account) accountDataBindCheckResult.singleData();

        // 验证码检查
        String codeBind = usePhone ? accountData.phone() : accountData.email();
        if (isNull(codeBind) || codeBind.isEmpty()) {
            return Result.failed().resultType(AuthenticationResultEnum.MUST_USE_PHONE_OR_EMAIL);
        }
        Result verifyResult = authenticationUtil.codeVerify(VERIFY_CODE_PREFIX, codeBind, verifyCode);

        if (!verifyResult.state()) {
            return verifyResult.state(null).message("注册失败");
        }

        // 数据入库
        accountMapper.insert(accountData);

        // 清除验证码
        JedisOperateUtil.del(VERIFY_CODE_PREFIX + ":" + (usePhone ? accountData.phone() : accountData.email()));

        return Result.success().description("注册成功");

    }

    /**
     * 检查账户数据是否可以绑定。
     * 首先检查登录ID是否已被使用，然后根据提供的电话号码或电子邮件地址检查唯一绑定是否已存在。
     * 如果登录ID或唯一绑定已存在，将返回相应的错误结果；如果都不存在，将返回成功结果。
     *
     * @param accountData 账号数据
     * @param usePhone    是否使用手机号码
     *
     * @return 检查结果
     */
    @Override
    public Result accountDataBindCheck(Account accountData, boolean usePhone) {

        // 初始化检查结果为失败
        Result accountDataBindCheck = Result.failed().state(false);

        // 检查登录ID是否已被使用
        boolean accountLoginIdAlreadyUse = accountMapper.selectCountByQuery(new QueryWrapper().eq("open_id", accountData.openId())) == 1;
        if (accountLoginIdAlreadyUse) {
            return accountDataBindCheck.resultType(AuthenticationResultEnum.LOGIN_ID_ALREADY_USE);
        }

        // 检查绑定数据是否为空
        if (usePhone ? accountData.phone().isEmpty() : accountData.email().isEmpty()) {
            return accountDataBindCheck.resultType(AuthenticationResultEnum.MUST_USE_PHONE_OR_EMAIL);
        }

        // 数据绑定检查
        QueryWrapper uniqueBindQuery = new QueryWrapper();

        // 构造查询条件并置空另一个绑定数据与检查结果
        if (usePhone) {
            uniqueBindQuery.eq("phone", accountData.phone());
            accountDataBindCheck.resultType(AuthenticationResultEnum.PHONE_NUMBER_ALREADY_USE);
            accountData.email(null);
        } else {
            uniqueBindQuery.eq("email", accountData.email());
            accountDataBindCheck.resultType(AuthenticationResultEnum.EMAIL_ALREADY_ALREADY_USE);
            accountData.phone(null);
        }

        // 查询是否已存在对应条件的数据
        boolean uniqueBindAlreadyUse = accountMapper.selectCountByQuery(uniqueBindQuery) == 1;

        if (uniqueBindAlreadyUse) {
            return accountDataBindCheck;
        }

        return Result.success().state(true).singleData(accountData);

    }

}
