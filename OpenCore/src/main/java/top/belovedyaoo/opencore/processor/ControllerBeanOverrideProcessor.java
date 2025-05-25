package top.belovedyaoo.opencore.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.opencore.advice.annotation.CoverRouter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 路由覆盖处理器
 *
 * @author Celrx
 * @version 1.0
 */
@Component
public class ControllerBeanOverrideProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        // 收集所有Controller
        Map<String, Class<?>> controllerBeans = new HashMap<>(beanDefinitionNames.length);
        for (String beanName : beanDefinitionNames) {
            BeanDefinition bd = registry.getBeanDefinition(beanName);
            String className = bd.getBeanClassName();
            if (className == null && bd.getSource() instanceof StandardMethodMetadata meta) {
                className = meta.getIntrospectedMethod().getReturnType().getName();
            }
            if (className == null) {
                continue;
            }
            try {
                Class<?> clazz = Class.forName(className);
                if (isController(clazz)) {
                    controllerBeans.put(beanName, clazz);
                }
            } catch (ClassNotFoundException ignored) {
            }
        }

        Set<String> toRemove = getToRemove(controllerBeans);
        for (String beanName : toRemove) {
            registry.removeBeanDefinition(beanName);
        }
    }

    /**
     * 获取需要移除的Controller bean名称集合
     *
     * @param controllerBeans Controller的bean名称与Class的映射
     *
     * @return 需要移除的bean名称集合
     */
    private static Set<String> getToRemove(Map<String, Class<?>> controllerBeans) {
        Set<String> toRemove = new HashSet<>();
        for (Map.Entry<String, Class<?>> entry : controllerBeans.entrySet()) {
            Class<?> clazz = entry.getValue();
            if (clazz.isAnnotationPresent(CoverRouter.class)) {
                // 沿继承链查找父类中被@RestController或@Controller注解的类，并将其beanName加入toRemove
                Class<?> superClass = clazz.getSuperclass();
                while (superClass != null && !superClass.equals(Object.class)) {
                    if (isController(superClass)) {
                        for (Map.Entry<String, Class<?>> parentEntry : controllerBeans.entrySet()) {
                            if (parentEntry.getValue().equals(superClass)) {
                                toRemove.add(parentEntry.getKey());
                            }
                        }
                    }
                    superClass = superClass.getSuperclass();
                }
            }
        }
        return toRemove;
    }

    /**
     * 判断是否为Controller
     *
     * @param clazz Class
     *
     * @return 是否为Controller
     */
    private static boolean isController(Class<?> clazz) {
        return clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(Controller.class);
    }

}
