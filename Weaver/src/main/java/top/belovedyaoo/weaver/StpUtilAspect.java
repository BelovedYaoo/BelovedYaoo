package top.belovedyaoo.weaver;

import cn.dev33.satoken.spring.SpringMVCUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * StpUtil 鉴权工具切面类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Aspect
public class StpUtilAspect {

    public static StpUtilAspect aspectOf() {
        return new StpUtilAspect();
    }

    @Around("execution(public * cn.dev33.satoken.stp.StpUtil.getLoginId(..))")
    public Object getGetInvoker(ProceedingJoinPoint joinPoint) throws Throwable {
        if (SpringMVCUtil.isWeb()) {
            return joinPoint.proceed(joinPoint.getArgs());
        } else {
            return "";
        }
    }

}
