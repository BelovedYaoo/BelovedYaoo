package top.belovedyaoo.openiam.common.exception;

import cn.dev33.satoken.exception.DisableServiceException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.agcore.result.ResultEnum;
import top.belovedyaoo.openiam.enums.AuthenticationResultEnum;
import top.belovedyaoo.openiam.common.toolkit.LogUtil;

/**
 * Sa-Token异常捕捉
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@ControllerAdvice
@RestControllerAdvice
@RequiredArgsConstructor
public class SaTokenExceptionHandler {

    /**
     * Sa-Token登录异常处理
     *
     * @param nle 异常对象
     *
     * @return 统一接口返回值
     */
    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public Result notLoginException(NotLoginException nle) {

        String message = SaTokenExceptionEnum.getDescByType(nle.getType());

        LogUtil.error("Sa-Token登录异常处理："+message);

        return Result.failed().resultType(ResultEnum.SESSION_INVALID).message(message);

    }

    /**
     * Sa-Token封禁异常处理
     *
     * @param dse 异常对象
     *
     * @return 统一接口返回值
     */
    @ExceptionHandler(DisableServiceException.class)
    @ResponseBody
    public Result disableServiceException(DisableServiceException dse) {

        LogUtil.error(dse.getMessage());

        return Result.failed().resultType(AuthenticationResultEnum.ACCOUNT_BANNED)
                .data("disableTime", StpUtil.getDisableTime(dse.getLoginId()));

    }
    
}
