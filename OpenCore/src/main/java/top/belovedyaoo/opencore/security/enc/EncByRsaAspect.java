package top.belovedyaoo.opencore.security.enc;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.opencore.security.SecurityConfig;

import java.lang.reflect.Method;

/**
 * RSA 加密切面类
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Slf4j
@Aspect
@Component
public class EncByRsaAspect {

    @Resource
    SecurityConfig securityConfig;

    /***
     * 拦截具有InterfaceLog注解的方法
     */
    @Pointcut("@annotation(top.belovedyaoo.opencore.security.enc.EncByRsa)")
    public void pointcut() {
    }

    @Around(value = "@annotation(top.belovedyaoo.opencore.security.enc.EncByRsa)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 通过切入点获取拦截类对象
        Class<?> targetClass = point.getTarget().getClass();
        // 通过切入点获取方法签名对象
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 从方法签名中获取到拦截方法对象
        Method targetMethod = signature.getMethod();
        // 从方法对象取出EncByRSA注解
        EncByRsa annotation = targetMethod.getAnnotation(EncByRsa.class);
        // 获取拦截类类名
        String className = targetClass.getName();
        // 获取拦截方法名称
        String methodName = point.getSignature().getName();
        // 获取拦截方法实参
        Object[] args = point.getArgs();
        // 获取拦截方法实参
        Object[] encArgs = point.getArgs();
        // 取得RSA加密对象
        RSA rsa = new RSA(securityConfig.getPrivateKey(), securityConfig.getPublicKey());
        // 判断是否需要解密实参以及实参数量是否大于0
        if (annotation.decParams() && args.length > 0) {
            // 拿到方法的入参
            log.info("\n===解密前===," +
                    "类名[{}]," +
                    "方法名[{}]" +
                    "参数:[{}]", className, methodName, args);
            for (int i = 0; i < args.length; i++) {
                encArgs[i] = StrUtil.str(rsa.decrypt(Hex.decodeHex((String)args[i]), KeyType.PublicKey), CharsetUtil.CHARSET_UTF_8);
            }
            log.info("\n===解密后===," +
                    "类名[{}]," +
                    "方法名[{}]" +
                    "参数:[{}]", className, methodName, args);
            args = encArgs;
        }
        // 运行拦截方法
        Object proceed = point.proceed(args);
        System.out.println(proceed);
        // 判断是否需要加密方法结果
        if (annotation.encResult()) {
            log.info("\n===加密前===" +
                    "类名[{}]," +
                    "方法名[{}]" +
                    "返回参数:[{}ms]", className, methodName, proceed.toString());
            Result result = (Result) proceed;
            String proceedMap = Hex.encodeHexString(rsa.encrypt(new ObjectMapper().writeValueAsString(result.data()), KeyType.PrivateKey));
            log.info("\n===加密后===," +
                    "类名[{}]," +
                    "方法名[{}]" +
                    "返回参数:[{}]", className, methodName, proceedMap);
            result.singleData(proceedMap);
            return result;
        } else {
            return proceed;
        }
    }
}


