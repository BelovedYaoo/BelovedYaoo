package top.belovedyaoo.openiam.oauth2.annotation.handler;

import cn.dev33.satoken.annotation.handler.SaAnnotationHandlerInterface;
import top.belovedyaoo.openiam.oauth2.annotation.CheckClientIdSecret;
import top.belovedyaoo.openiam.oauth2.processor.OpenAuthServerProcessor;

import java.lang.reflect.Method;

/**
 * 注解 CheckClientSecret 的处理器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class CheckClientIdSecretHandler implements SaAnnotationHandlerInterface<CheckClientIdSecret> {

    @Override
    public Class<CheckClientIdSecret> getHandlerAnnotationClass() {
        return CheckClientIdSecret.class;
    }

    @Override
    public void checkMethod(CheckClientIdSecret at, Method method) {
        _checkMethod();
    }

    public static void _checkMethod() {
        OpenAuthServerProcessor.instance.checkCurrClientSecret();
    }

}