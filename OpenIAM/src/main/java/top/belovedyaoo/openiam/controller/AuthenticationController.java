package top.belovedyaoo.openiam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.logs.annotation.InterfaceLog;
import top.belovedyaoo.openac.generateMapper.UserMapper;
import top.belovedyaoo.openac.model.Domain;
import top.belovedyaoo.openac.model.User;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.opencore.tree.Tree;
import top.belovedyaoo.opencore.tree.TreeService;
import top.belovedyaoo.openiam.service.AuthenticationService;
import top.belovedyaoo.openiam.toolkit.AuthenticationUtil;

import java.util.List;

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

    private final UserMapper userMapper;

    private final TreeService<Domain> domainService;

    @PostMapping("/test")
    public List<? extends Tree> test(@RequestParam(value = "parentId") String parentId) {
        return domainService.findAllNodeByBaseId(parentId);
    }

    @PostMapping("/test2")
    @InterfaceLog(identifierCode = "abc", interfaceDesc = "测试接口日志注解", interfaceName = "test2")
    public User test2() {
        User account = User.builder()
                .baseId("111")
                .openId("222")
                .password("YTY2NWE0NTkyMDQyMmY5ZDQxN2U0ODY3ZWZkYzRmYjhhMDRhMWYzZmZmMWZhMDdlOTk4ZTg2ZjdmN2EyN2FlMw==")
                .build();
        // System.out.println(account);
        userMapper.insert(account);
        // userMapper.insert(User.builder()
        //         .openId("123")
        //         .password("YTY2NWE0NTkyMDQyMmY5ZDQxN2U0ODY3ZWZkYzRmYjhhMDRhMWYzZmZmMWZhMDdlOTk4ZTg2ZjdmN2EyN2FlMw==")
        //         .email("belovedyaoo@qq.com")
        //         .build());
        return account;
    }

    @PostMapping("/test3")
    @InterfaceLog(identifierCode = "abc333", interfaceDesc = "测试接口日志注解3", interfaceName = "test3")
    public Result test3(@RequestBody User user) {
        // System.out.println(user);
        userMapper.insert(user);
        return Result.success().singleData(user);
    }

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
