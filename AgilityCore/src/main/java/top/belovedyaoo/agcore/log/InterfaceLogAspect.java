package top.belovedyaoo.agcore.log;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.belovedyaoo.agcore.base.BaseController;
import top.belovedyaoo.agcore.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * 接口日志切面类<br>
 * 切面注解得到请求数据 -> 发布监听事件 -> 异步监听日志入库
 *
 * @author BelovedYaoo
 * @version 1.2
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class InterfaceLogAspect {

    /**
     * 事件发布是由ApplicationContext对象管控的<br>
     * 发布事件前需要注入ApplicationContext对象调用publishEvent方法完成事件发布
     **/
    private final ApplicationContext applicationContext;

    /**
     * 线程局部变量，存储日志对象
     */
    private static final ThreadLocal<InterfaceLogPO> INTERFACE_LOG_PO_THREAD_LOCAL = new ThreadLocal<>();

    /***
     * 拦截规则，拦截InterfaceLog注解的方法
     */
    @Pointcut("@annotation(top.belovedyaoo.agcore.log.InterfaceLog)")
    public void interfaceLogAspect() {
    }

    /**
     * 拦截控制层的操作日志
     */
    @Before(value = "interfaceLogAspect()")
    public void recordLog(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        // 判断是否继承了BaseController
        if (BaseController.class.isAssignableFrom(targetClass)) {
            // 取得具体继承类的泛型类型
            Class<Object> entityClass = BaseController.getGenericClass(targetClass, 0);
        }

        InterfaceLogPO interfaceLogEntity = new InterfaceLogPO();
        // 保存到线程局部变量
        INTERFACE_LOG_PO_THREAD_LOCAL.set(interfaceLogEntity);
        interfaceLogEntity.requestUrl(URLUtil.getPath(request.getRequestURI()))
                .requestIp(ServletUtil.getClientIP(request))
                .methodName(joinPoint.getSignature().getName())
                .startTime(new Date())
                .params(Arrays.toString(joinPoint.getArgs()));
        // 从JoinPoint获取方法签名
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature methodSignature) {
            // 从方法签名中获取到方法对象
            Method method = methodSignature.getMethod();
            // 取出执行方法上的InterfaceLog注解中的信息
            InterfaceLog interfaceLog = method.getAnnotation(InterfaceLog.class);
            if (interfaceLog != null) {
                interfaceLogEntity.businessTypes(Arrays.toString(interfaceLog.businessType()));
                interfaceLogEntity.description(interfaceLog.interfaceDesc());
            }
        }
    }

    /**
     * 返回通知
     */
    @AfterReturning(returning = "ret", pointcut = "interfaceLogAspect()")
    public void doAfterReturning(Object ret) {
        InterfaceLogPO interfaceLogEntity = INTERFACE_LOG_PO_THREAD_LOCAL.get();
        Result result = Result.tryConvert(ret);
        interfaceLogEntity.result(result.getLogString())
                .finishTime(new Date());
        // 发布事件
        applicationContext.publishEvent(new InterfaceLogEvent(interfaceLogEntity));
        // 清空线程局部变量
        INTERFACE_LOG_PO_THREAD_LOCAL.remove();
    }

    /**
     * 异常通知
     */
    @AfterThrowing(pointcut = "interfaceLogAspect()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        InterfaceLogPO interfaceLogEntity = INTERFACE_LOG_PO_THREAD_LOCAL.get();
        // 异常
        interfaceLogEntity.exceptionMessage(e.toString());
        // 发布事件
        applicationContext.publishEvent(new InterfaceLogEvent(interfaceLogEntity));
        // 清空线程局部变量
        INTERFACE_LOG_PO_THREAD_LOCAL.remove();

    }

}