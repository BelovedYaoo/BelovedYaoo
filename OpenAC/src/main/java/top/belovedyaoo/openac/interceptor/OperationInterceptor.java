package top.belovedyaoo.openac.interceptor;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.fun.SaParamFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import top.belovedyaoo.openac.core.OperationInfo;
import top.belovedyaoo.openac.enums.OperationAccessEnum;
import top.belovedyaoo.openac.initializer.AcDataCacheLoader;
import top.belovedyaoo.openac.model.Operation;
import top.belovedyaoo.opencore.result.Result;

import java.util.List;

/**
 * 接口拦截器
 *
 * @author Celrx
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class OperationInterceptor extends SaInterceptor {

    private final AcDataCacheLoader acDataCacheLoader;

    public SaParamFunction<Object> auth() {
        return handler -> {
            if (handler instanceof HandlerMethod handlerMethod) {
                Class<?> controllerClass = handlerMethod.getBeanType();
                String methodName = handlerMethod.getMethod().getName();
                // 获取 Operation 对象
                Operation operation = OperationInfo.getInstance().operationInfoMap.get(controllerClass.getName()).get(methodName);
                // 获取接口所需权限列表
                List<String> permissionCodeList = acDataCacheLoader.getMappingOperationPermissionMap().get(operation.baseId());
                try {
                    permissionCodeList.forEach(StpUtil::checkPermission);
                } catch (NotPermissionException e) {
                    SaRouter.back(Result.failed().resultType(OperationAccessEnum.NO_PERMISSION));
                }
            }
        };
    }

}