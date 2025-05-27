package top.belovedyaoo.openac.initializer;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.belovedyaoo.openac.core.OperationInfo;
import top.belovedyaoo.openac.model.Operation;
import top.belovedyaoo.opencore.common.OperationMark;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 操作接口信息初始化器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class OperationAcInitializer {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @PostConstruct
    public void operationInfoInit() {
        OperationInfo operationInfoInstance = OperationInfo.getInstance();
        // 获取所有注册的请求映射信息
        requestMappingHandlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            // 获取请求路径
            Set<String> patterns = requestMappingInfo.getPatternValues();
            // 获取HTTP方法
            Set<String> httpMethods = requestMappingInfo.getMethodsCondition().getMethods().stream()
                    .map(Enum::name)
                    .collect(Collectors.toSet());
            // 获取控制器类信息
            String controllerClass = handlerMethod.getBeanType().getName();
            // 获取控制器方法信息
            Method method = handlerMethod.getMethod();
            // 方法名称
            String methodName = method.getName();
            // 记录的信息
            Operation operationInfo = new Operation();
            // 取出方法上的@OperationMark注解中的信息
            // 注意是通过 Spring 框架提供的 AnnotatedElementUtils 类来获取，这样才能获取到完整的注解的信息，否则接口的方法一旦被重写，会丢失部分注解的信息
            OperationMark operationMark = AnnotatedElementUtils.findMergedAnnotation(method, OperationMark.class);
            if (operationMark != null) {
                operationInfo.operationName(operationMark.name());
                operationInfo.operationCode(operationMark.code());
                operationInfo.operationDesc(operationMark.desc());
            }
            operationInfo.requestUrl(patterns.toString());
            operationInfo.requestMethod(httpMethods.toString());
            operationInfo.className(controllerClass);
            operationInfo.operationMethodName(methodName);
            String digestId = calculateDigestId(controllerClass + methodName);
            operationInfo.baseId(digestId);
            if (operationInfoInstance.operationInfoMap.containsKey(controllerClass)) {
                operationInfoInstance.operationInfoMap.get(controllerClass).put(methodName, operationInfo);
            } else {
                operationInfoInstance.operationInfoMap.put(controllerClass, new LinkedHashMap<>(Map.of(methodName, operationInfo)));
            }
        });
    }

    /**
     * 计算唯一标识符<p>
     * 暂时使用MD5，如果存在碰撞则考虑使用其他
     *
     * @param data 需要计算的数据
     *
     * @return 唯一标识符
     */
    public String calculateDigestId(String data) {
        return new Digester(DigestAlgorithm.MD5).digestHex(data);
    }

}
