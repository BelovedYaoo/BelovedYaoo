package top.belovedyaoo.openiam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.openac.model.User;
import top.belovedyaoo.openiam.service.AuthenticationService;
import top.belovedyaoo.openiam.toolkit.AuthenticationUtil;
import top.belovedyaoo.opencore.result.Result;

import static top.belovedyaoo.openiam.service.AuthenticationService.VERIFY_CODE_PREFIX;

/**
 * 认证控制层
 *
 * @author BelovedYaoo
 * @version 1.5
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    /**
     * 认证工具类
     */
    private final AuthenticationUtil authenticationUtil;

    private final AuthenticationService authenticationService;

    /**
     * 账号注册方法
     *
     * @param user 账号数据(登录ID、密码)
     *
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user, @RequestParam(value = "usePhone") boolean usePhone, @RequestParam(value = "verifyCode") String verifyCode) {
        return authenticationService.register(user, usePhone, verifyCode);
    }

    /**
     * 验证码生成方法
     *
     * @param user 账号数据
     *
     * @return 生成结果
     */
    @PostMapping("/getVerifyCode")
    public Result getVerifyCode(@RequestBody User user, @RequestParam(value = "usePhone") boolean usePhone) {
        return authenticationUtil.codeVerify(VERIFY_CODE_PREFIX, usePhone ? user.phone() : user.email());
    }

}
