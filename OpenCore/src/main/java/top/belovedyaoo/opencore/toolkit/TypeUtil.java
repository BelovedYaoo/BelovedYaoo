package top.belovedyaoo.opencore.toolkit;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型工具类
 * @param <T> 泛型类型
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class TypeUtil<T> {

    /**
     * 快速获取泛型的类型的方法
     *
     * @param objectClass 要取泛型的对应的Class
     * @param i           要取第几个泛型(0开始)
     * @param <T>         泛型类型
     *
     * @return 对应的泛型
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericClass(Class<?> objectClass, int i) {
        ParameterizedType type = (ParameterizedType) objectClass.getGenericSuperclass();
        if (type == null) {
            return null;
        }
        Type[] types = type.getActualTypeArguments();
        if (types.length > i) {
            return (Class<T>) types[i];
        }
        return null;
    }

    /**
     * 获取泛型类型对应的Class对象
     *
     * @return 具体Class对象
     */
    public Class<T> getOriginalClass() {
        return getGenericClass(this.getClass(), 0);
    }

}
