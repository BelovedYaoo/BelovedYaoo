package top.belovedyaoo.openiam.toolkit;

import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.belovedyaoo.openiam.enums.AuthenticationResultEnum;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.opencore.toolkit.JedisOperateUtil;
import top.belovedyaoo.logs.toolkit.LogUtil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 认证工具类
 *
 * @author BelovedYaoo
 * @version 1.2
 */
@Component
@RequiredArgsConstructor
public class AuthenticationUtil {

    private static final int VERIFY_CODE_EXPIRE_TIME = 5 * 60;

    private static final String VERIFY_CODE_SUFFIX = "verifyCode";

    /**
     * 验证码生成方法
     *
     * @param codeSuffix 验证码前缀
     * @param codeKey    验证码Key
     * @param codeTime   验证码过期时间
     *
     * @return 验证结果
     */
    public Result codeVerify(@NotBlank String codeSuffix, @NotBlank String codeKey, @NotNull int codeTime) {
        String verifiedCode = RandomUtil.randomNumbers(6);
        JedisOperateUtil.setEx(verifiedCode, codeTime, VERIFY_CODE_SUFFIX, codeSuffix, codeKey);
        LogUtil.info(codeKey + "的验证码是：" + verifiedCode);
        return Result.success().description("验证码生成成功");
    }

    /**
     * 验证码生成方法
     * 使用默认验证码过期时间
     *
     * @param codeSuffix 验证码前缀
     * @param codeKey    验证码Key
     *
     * @return 生成结果
     */
    public Result codeVerify(@NotBlank String codeSuffix, @NotBlank String codeKey) {
        return codeVerify(codeSuffix, codeKey, VERIFY_CODE_EXPIRE_TIME);
    }

    /**
     * 验证码验证方法
     *
     * @param codeSuffix 验证码前缀
     * @param codeKey    验证码Key
     * @param verifyCode 验证码
     *
     * @return 验证结果
     */
    public Result codeVerify(@NotBlank String codeSuffix, @NotBlank String codeKey, @NotBlank String verifyCode) {
        String verifiedCode = JedisOperateUtil.get(VERIFY_CODE_SUFFIX, codeSuffix, codeKey);
        if (verifiedCode != null && verifiedCode.equals(verifyCode)) {
            return Result.success().state(true);
        }
        return Result.failed().state(false).resultType(AuthenticationResultEnum.CODE_VERIFY_ERROR);
    }

}
