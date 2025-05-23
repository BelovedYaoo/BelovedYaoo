package top.belovedyaoo.opencore.cache.strategy;

import top.belovedyaoo.opencore.cache.CacheAccess;
import top.belovedyaoo.opencore.cache.ICacheStrategy;

/**
 * 读写穿透策略
 *
 * @author Celrx
 * @version 1.0
 */
public class PenetrateCacheStrategy<K, V> implements ICacheStrategy<K, V> {

    @Override
    public V execute(CacheAccess<K, V> access) {
        if (access.isWrite()) {
            // 写入缓存
            access.cacheWrite().accept(access.key(), access.value());
            // 写入数据库
            access.dbWrite().accept(access.value());
            return null;
        } else {
            // 尝试获取缓存
            V v = access.cacheRead().apply(access.key());
            // 命中则直接返回缓存
            if (v != null) {
                return v;
            }
            // 从数据库读取
            v = access.dbRead().get();
            // 写入缓存
            if (v != null) {
                access.cacheWrite().accept(access.key(), v);
            }
            return v;
        }
    }

} 