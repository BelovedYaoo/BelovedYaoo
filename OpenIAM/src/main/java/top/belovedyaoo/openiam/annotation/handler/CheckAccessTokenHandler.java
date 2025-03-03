package top.belovedyaoo.openiam.annotation.handler;

import cn.dev33.satoken.annotation.handler.SaAnnotationHandlerInterface;
import cn.dev33.satoken.context.SaHolder;
import top.belovedyaoo.openiam.annotation.CheckAccessToken;
import top.belovedyaoo.openiam.core.OpenAuthManager;

import java.lang.reflect.Method;

/**
 * 注解 CheckAccessToken 的处理器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class CheckAccessTokenHandler implements SaAnnotationHandlerInterface<CheckAccessToken> {

    @Override
    public Class<CheckAccessToken> getHandlerAnnotationClass() {
        return CheckAccessToken.class;
    }

    @Override
    public void checkMethod(CheckAccessToken at, Method method) {
        _checkMethod(at.scope());
    }

    public static void _checkMethod(String[] scope) {
        String accessToken = OpenAuthManager.getDataResolver().readAccessToken(SaHolder.getRequest());
        OpenAuthManager.getTemplate().checkAccessTokenScope(accessToken, scope);
    }

}