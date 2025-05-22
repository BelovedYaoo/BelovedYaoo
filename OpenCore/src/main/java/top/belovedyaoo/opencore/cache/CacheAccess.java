package top.belovedyaoo.opencore.cache;

import lombok.Getter;
import top.belovedyaoo.opencore.exception.OpenException;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 缓存访问对象
 *
 * @param <K> 缓存键类型
 * @param <V> 数据类型
 *
 * @author Celrx
 * @version 1.0
 */
public class CacheAccess<K, V> {

    /**
     * 缓存访问策略
     */
    private ICacheStrategy<K, V> strategyImpl;

    /**
     * 缓存键
     */
    @Getter
    private K key;

    /**
     * 函数式缓存读取方法
     */
    @Getter
    private Function<K, V> cacheRead = k -> null;

    /**
     * 函数式缓存写入方法
     */
    @Getter
    private BiConsumer<K, V> cacheWrite = (k, v) -> {};

    /**
     * 函数式数据库读取方法
     */
    @Getter
    private Supplier<V> dbRead = () -> null;

    /**
     * 函数式数据库写入方法
     */
    @Getter
    private Consumer<V> dbWrite = v -> {};

    /**
     * 函数式缓存失效方法
     */
    @Getter
    private Consumer<K> cacheEvict = k -> {};

    /**
     * 要写入的数据
     */
    @Getter
    private V value;

    /**
     * 是否为写入操作
     */
    @Getter
    private boolean isWrite = false;

    /**
     * 私有构造函数
     */
    private CacheAccess() {
    }

    /**
     * 开启缓存访问
     */
    public static <K, V> CacheAccess<K, V> start() {
        return new CacheAccess<>();
    }

    /**
     * 指定缓存访问策略，详见 <a href="https://developer.aliyun.com/article/1491925">Redis读写策略：旁路、穿透、异步</a>
     */
    public CacheAccess<K, V> strategy(ICacheStrategy<K, V> strategyImpl) {
        this.strategyImpl = strategyImpl;
        return this;
    }

    /**
     * 指定缓存键，用于缓存层的读写
     */
    public CacheAccess<K, V> key(K key) {
        this.key = key;
        return this;
    }

    /**
     * 要写入的数据，一旦设置，则表示当前为写入操作，否则为读取操作
     */
    public CacheAccess<K, V> value(V value) {
        this.value = value;
        this.isWrite = true;
        return this;
    }

    /**
     * 设置函数式缓存读取方法
     */
    public CacheAccess<K, V> cacheRead(Function<K, V> cacheRead) {
        this.cacheRead = cacheRead;
        return this;
    }

    /**
     * 设置函数式缓存写入方法
     */
    public CacheAccess<K, V> cacheWrite(BiConsumer<K, V> cacheWrite) {
        this.cacheWrite = cacheWrite;
        return this;
    }

    /**
     * 设置函数式数据库读取方法
     */
    public CacheAccess<K, V> dbRead(Supplier<V> dbRead) {
        this.dbRead = dbRead;
        return this;
    }

    /**
     * 设置函数式数据库写入方法
     */
    public CacheAccess<K, V> dbWrite(Consumer<V> dbWrite) {
        this.dbWrite = dbWrite;
        return this;
    }

    /**
     * 设置函数式缓存失效方法
     */
    public CacheAccess<K, V> cacheEvict(Consumer<K> cacheEvict) {
        this.cacheEvict = cacheEvict;
        return this;
    }

    /**
     * 执行
     */
    public V run() {
        if (strategyImpl == null) {
            throw new OpenException("请先指定缓存访问策略实现");
        }
        return strategyImpl.execute(this);
    }

}