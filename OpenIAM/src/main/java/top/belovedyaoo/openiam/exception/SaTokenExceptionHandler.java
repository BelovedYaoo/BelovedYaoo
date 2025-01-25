package top.belovedyaoo.openiam.exception;

import cn.dev33.satoken.exception.DisableServiceException;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.openiam.enums.AuthenticationResultEnum;
import top.belovedyaoo.logs.toolkit.LogUtil;

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
