package top.belovedyaoo.opencore.ac;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 权限控制策略
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public final class AcServiceStrategy {

    private AcServiceStrategy() {
    }

    /**
     * 全局单例引用
     */
    public static final AcServiceStrategy INSTANCE = new AcServiceStrategy();

    /**
     * 读权限控制器集合
     */
    public Map<Class<?>, AcHandlerFunction> readHandlerMap = new LinkedHashMap<>();

    /**
     * 写权限控制器集合
     */
    public Map<Class<?>, AcHandlerFunction> writeHandlerMap = new LinkedHashMap<>();

    /**
     * 注册一个权限控制器
     *
     * @param clazz   实体类
     * @param handler 权限控制器
     * @param isRead  是否为读权限控制器
     */
    public void registerHandler(Class<?> clazz, AcHandlerFunction<?> handler, boolean isRead) {
        if (isRead) {
            readHandlerMap.put(clazz, handler);
        } else {
            writeHandlerMap.put(clazz, handler);
        }
    }

    public void removeHandler(Class<?> clazz, boolean isRead) {
        if (isRead) {
            readHandlerMap.remove(clazz);
        } else {
            writeHandlerMap.remove(clazz);
        }
    }

    public <T> AcHandlerFunction<T> getHandler(Class<?> clazz, boolean isRead) {
        if (isRead) {
            return readHandlerMap.get(clazz);
        } else {
            return writeHandlerMap.get(clazz);
        }
    }

}
