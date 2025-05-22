package top.belovedyaoo.opencore.cache.strategy;

import top.belovedyaoo.opencore.cache.CacheAccess;
import top.belovedyaoo.opencore.cache.ICacheStrategy;

/**
 * 旁路缓存策略
 *
 * @author Celrx
 * @version 1.0
 */
public class AsideCacheStrategy<K, V> implements ICacheStrategy<K, V> {

    @Override
    public V execute(CacheAccess<K, V> access) {
        if (access.isWrite()) {
            // 使缓存失效
            access.getCacheEvict().accept(access.getKey());
            // 写入数据库
            access.getDbWrite().accept(access.getValue());
            return null;
        } else {
            // 尝试获取缓存
            V v = access.getCacheRead().apply(access.getKey());
            // 命中则直接返回缓存
            if (v != null) {
                return v;
            }
            // 从数据库读取
            v = access.getDbRead().get();
            // 写入缓存
            if (v != null) {
                access.getCacheWrite().accept(access.getKey(), v);
            }
            return v;
        }
    }

} 