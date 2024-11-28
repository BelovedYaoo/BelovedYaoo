package top.belovedyaoo.openauth.annotation.handler;

import cn.dev33.satoken.annotation.handler.SaAnnotationHandlerInterface;
import cn.dev33.satoken.context.SaHolder;
import top.belovedyaoo.openauth.annotation.CheckClientToken;
import top.belovedyaoo.openauth.core.OpenAuthManager;

import java.lang.reflect.Method;

/**
 * 注解 CheckAccessToken 的处理器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class CheckClientTokenHandler implements SaAnnotationHandlerInterface<CheckClientToken> {

    @Override
    public Class<CheckClientToken> getHandlerAnnotationClass() {
        return CheckClientToken.class;
    }

    @Override
    public void checkMethod(CheckClientToken at, Method method) {
        _checkMethod(at.scope());
    }

    public static void _checkMethod(String[] scope) {
        String clientToken = OpenAuthManager.getDataResolver().readClientToken(SaHolder.getRequest());
        OpenAuthManager.getTemplate().checkClientTokenScope(clientToken, scope);
    }

}