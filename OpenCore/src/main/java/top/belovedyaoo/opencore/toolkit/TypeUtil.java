package top.belovedyaoo.opencore.toolkit;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import top.belovedyaoo.opencore.exception.OpenException;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 泛型工具类
 *
 * @param <T> 泛型类型
 *
 * @author BelovedYaoo
 * @version 1.2
 */
public class TypeUtil<T> {
    
    /**
     * 缓存管理器
     */
    private static class CacheManager {

        private static final CacheManager INSTANCE = new CacheManager();
        
        // 缓存Class和泛型参数的映射关系
        private final Map<Class<?>, Map<Integer, Class<?>>> typeCache;
        // 缓存Type解析结果
        private final Map<Type, Class<?>> typeResolveCache;
        // 缓存大小限制
        private static final int MAX_CACHE_SIZE = 1000;
        // 缓存过期时间（毫秒）
        private static final long CACHE_EXPIRE_TIME = TimeUnit.HOURS.toMillis(1);
        // 缓存创建时间
        private final Map<Object, Long> cacheCreateTime;
        // 缓存最后访问时间
        private final Map<Object, Long> cacheLastAccessTime;

        private CacheManager() {
            this.typeCache = new ConcurrentHashMap<>();
            this.typeResolveCache = new ConcurrentHashMap<>();
            this.cacheCreateTime = new ConcurrentHashMap<>();
            this.cacheLastAccessTime = new ConcurrentHashMap<>();
        }

        public static CacheManager getInstance() {
            return INSTANCE;
        }

        public Class<?> getTypeCache(Class<?> clazz, int index) {
            Map<Integer, Class<?>> classCache = typeCache.get(clazz);
            if (classCache != null) {
                Class<?> cachedType = classCache.get(index);
                if (cachedType != null && !isExpired(clazz)) {
                    // 刷新最后访问时间
                    refreshAccessTime(clazz);
                    return cachedType;
                }
            }
            return null;
        }

        public Class<?> getTypeResolveCache(Type type) {
            Class<?> cachedType = typeResolveCache.get(type);
            if (cachedType != null && !isExpired(type)) {
                // 刷新最后访问时间
                refreshAccessTime(type);
                return cachedType;
            }
            return null;
        }

        public void putTypeCache(Class<?> clazz, int index, Class<?> type) {
            if (typeCache.size() >= MAX_CACHE_SIZE) {
                clearExpiredCache();
            }
            typeCache.computeIfAbsent(clazz, _ -> new ConcurrentHashMap<>())
                    .put(index, type);
            long currentTime = System.currentTimeMillis();
            cacheCreateTime.put(clazz, currentTime);
            cacheLastAccessTime.put(clazz, currentTime);
        }

        public void putTypeResolveCache(Type type, Class<?> resolvedType) {
            if (typeResolveCache.size() >= MAX_CACHE_SIZE) {
                clearExpiredCache();
            }
            typeResolveCache.put(type, resolvedType);
            long currentTime = System.currentTimeMillis();
            cacheCreateTime.put(type, currentTime);
            cacheLastAccessTime.put(type, currentTime);
        }

        private void refreshAccessTime(Object key) {
            cacheLastAccessTime.put(key, System.currentTimeMillis());
        }

        private boolean isExpired(Object key) {
            Long lastAccessTime = cacheLastAccessTime.get(key);
            if (lastAccessTime == null) {
                return true;
            }
            return System.currentTimeMillis() - lastAccessTime > CACHE_EXPIRE_TIME;
        }

        public void clearExpiredCache() {
            typeCache.entrySet().removeIf(entry -> isExpired(entry.getKey()));
            typeResolveCache.entrySet().removeIf(entry -> isExpired(entry.getKey()));
            cacheCreateTime.entrySet().removeIf(entry -> isExpired(entry.getKey()));
            cacheLastAccessTime.entrySet().removeIf(entry -> isExpired(entry.getKey()));
        }

        public void clearAllCache() {
            typeCache.clear();
            typeResolveCache.clear();
            cacheCreateTime.clear();
            cacheLastAccessTime.clear();
        }

    }

    /**
     * 快速获取泛型的类型的方法
     *
     * @param objectClass 要取泛型的对应的Class
     * @param i           要取第几个泛型(0开始)
     * @param <T>         泛型类型
     *
     * @return 对应的泛型
     *
     * @exception TypeResolutionException 如果类型解析失败
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericClass(
            @NotNull(message = "类对象不能为空") Class<?> objectClass,
            @Min(value = 0, message = "泛型参数索引不能为负数") int i) {
        
        // 检查缓存
        Class<?> cachedType = CacheManager.getInstance().getTypeCache(objectClass, i);
        if (cachedType != null) {
            return (Class<T>) cachedType;
        }

        // 如果是接口，直接返回null
        if (objectClass.isInterface()) {
            return null;
        }

        Type genericSuperclass = objectClass.getGenericSuperclass();
        if (genericSuperclass == null) {
            return null;
        }

        Class<T> result;
        try {
            result = switch (genericSuperclass) {
                case Class<?> clazz -> getGenericClass(clazz, i);
                case ParameterizedType type -> {
                    Type[] types = type.getActualTypeArguments();
                    if (types.length <= i) {
                        throw new TypeResolutionException(
                            String.format("泛型参数索引 %d 超出范围，类 %s 的泛型参数总数为 %d",
                                i, objectClass.getName(), types.length));
                    }
                    yield resolveType(types[i]);
                }
                default -> null;
            };

            // 缓存结果
            if (result != null) {
                CacheManager.getInstance().putTypeCache(objectClass, i, result);
            }
        } catch (Exception e) {
            throw new TypeResolutionException(
                String.format("解析类 %s 的第 %d 个泛型参数失败: %s",
                    objectClass.getName(), i, e.getMessage()), e);
        }

        return result;
    }

    /**
     * 解析Type类型，获取实际的Class对象
     *
     * @param type 要解析的Type
     * @param <T>  泛型类型
     *
     * @return 解析后的Class对象，如果无法解析则返回null
     *
     * @exception TypeResolutionException 如果类型解析失败
     */
    @SuppressWarnings("unchecked")
    private static <T> Class<T> resolveType(Type type) {
        if (type == null) {
            return null;
        }
        // 检查缓存
        Class<?> cachedType = CacheManager.getInstance().getTypeResolveCache(type);
        if (cachedType != null) {
            return (Class<T>) cachedType;
        }
        try {
            Class<T> result = switch (type) {
                case Class<?> clazz -> (Class<T>) clazz;
                case ParameterizedType parameterizedType -> {
                    Type rawType = parameterizedType.getRawType();
                    if (rawType instanceof Class<?> rawClass) {
                        yield (Class<T>) rawClass;
                    }
                    throw new TypeResolutionException(
                        String.format("无法解析参数化类型 %s 的原始类型", type));
                }
                case TypeVariable<?> typeVariable -> {
                    Type[] bounds = typeVariable.getBounds();
                    if (bounds.length == 0) {
                        throw new TypeResolutionException(
                            String.format("类型变量 %s 没有边界", typeVariable.getName()));
                    }
                    if (bounds[0] instanceof Class<?> boundClass) {
                        yield (Class<T>) boundClass;
                    }
                    throw new TypeResolutionException(
                        String.format("无法解析类型变量 %s 的边界类型", typeVariable.getName()));
                }
                case WildcardType wildcardType -> {
                    Type[] upperBounds = wildcardType.getUpperBounds();
                    if (upperBounds.length == 0) {
                        throw new TypeResolutionException("通配符类型没有上界");
                    }
                    if (upperBounds[0] instanceof Class<?> upperClass) {
                        yield (Class<T>) upperClass;
                    }
                    throw new TypeResolutionException(
                        String.format("无法解析通配符类型 %s 的上界", type));
                }
                case GenericArrayType genericArrayType -> {
                    Type componentType = genericArrayType.getGenericComponentType();
                    if (componentType instanceof Class<?> componentClass) {
                        yield (Class<T>) componentClass.arrayType();
                    }
                    throw new TypeResolutionException(
                        String.format("无法解析泛型数组类型 %s 的组件类型", type));
                }
                default -> throw new TypeResolutionException(
                    String.format("不支持的类型: %s", type.getClass().getName()));
            };
            // 缓存结果
            CacheManager.getInstance().putTypeResolveCache(type, result);
            return result;
        } catch (TypeResolutionException e) {
            throw e;
        } catch (Exception e) {
            throw new TypeResolutionException(
                String.format("解析类型 %s 失败: %s", type, e.getMessage()), e);
        }
    }

    /**
     * 获取泛型类型对应的Class对象
     *
     * @return 具体Class对象
     *
     * @exception TypeResolutionException 如果类型解析失败
     */
    public Class<T> getOriginalClass() {
        return getGenericClass(this.getClass(), 0);
    }

    /**
     * 清除所有缓存
     */
    public static void clearCache() {
        CacheManager.getInstance().clearAllCache();
    }

    /**
     * 清除过期缓存
     */
    public static void clearExpiredCache() {
        CacheManager.getInstance().clearExpiredCache();
    }

    /**
     * 类型解析异常
     */
    private static class TypeResolutionException extends OpenException {

        public TypeResolutionException(String message) {
            super(message);
        }

        public TypeResolutionException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
