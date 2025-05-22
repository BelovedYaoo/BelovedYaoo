package top.belovedyaoo.opencore.cache;

/**
 * 缓存访问策略接口
 *
 * @param <K> 缓存键类型
 * @param <V> 数据类型
 *
 * @author Celrx
 * @version 1.0
 */
public interface ICacheStrategy<K, V> {

    /**
     * 执行缓存访问
     *
     * @param access 缓存访问对象，用于获取其中的函数式接口
     *
     * @return 数据
     */
    V execute(CacheAccess<K, V> access);

} 