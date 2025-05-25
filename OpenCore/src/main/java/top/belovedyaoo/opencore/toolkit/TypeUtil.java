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
 * 泛型工具类<p>
 * 用于在运行时获取泛型类型信息，支持复杂的泛型场景，包括：<p>
 * 1. 普通泛型类型<p>
 * 2. 参数化类型（如List&lt;String&gt;）<p>
 * 3. 类型变量（如T extends Number）<p>
 * 4. 通配符类型（如? extends Number）<p>
 * 5. 泛型数组类型（如T[]）<p>
 * 该类使用缓存机制提高性能，并支持缓存过期和自动清理。
 *
 * @param <T> 泛型类型
 *
 * @author BelovedYaoo
 * @version 1.3
 */
public class TypeUtil<T> {

    /**
     * 缓存管理器<p>
     * 使用单例模式确保全局只有一个缓存实例<p>
     * 管理两种类型的缓存：<p>
     * 1. 类级别的泛型参数缓存<p>
     * 2. 类型解析结果缓存<p>
     */
    private static class CacheManager {

        // 单例实例
        private static final CacheManager INSTANCE = new CacheManager();

        // 缓存Class和泛型参数的映射关系
        // 结构：Class -> (泛型参数索引 -> 具体类型)
        private final Map<Class<?>, Map<Integer, Class<?>>> typeCache;

        // 缓存Type解析结果
        // 结构：Type -> 解析后的Class
        private final Map<Type, Class<?>> typeResolveCache;

        // 缓存大小限制，防止内存泄漏
        private static final int MAX_CACHE_SIZE = 1000;

        // 缓存过期时间（1小时）
        private static final long CACHE_EXPIRE_TIME = TimeUnit.HOURS.toMillis(1);

        // 缓存创建时间，用于判断缓存是否过期
        private final Map<Object, Long> cacheCreateTime;

        // 缓存最后访问时间，用于实现访问刷新机制
        private final Map<Object, Long> cacheLastAccessTime;

        /**
         * 私有构造函数，初始化所有缓存Map
         */
        private CacheManager() {
            this.typeCache = new ConcurrentHashMap<>();
            this.typeResolveCache = new ConcurrentHashMap<>();
            this.cacheCreateTime = new ConcurrentHashMap<>();
            this.cacheLastAccessTime = new ConcurrentHashMap<>();
        }

        /**
         * 获取缓存管理器实例
         */
        public static CacheManager getInstance() {
            return INSTANCE;
        }

        /**
         * 获取类级别的泛型参数缓存
         *
         * @param clazz 目标类
         * @param index 泛型参数索引
         *
         * @return 缓存的类型，如果不存在或已过期则返回null
         */
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

        /**
         * 获取类型解析结果缓存
         *
         * @param type 要解析的类型
         *
         * @return 缓存的类型，如果不存在或已过期则返回null
         */
        public Class<?> getTypeResolveCache(Type type) {
            Class<?> cachedType = typeResolveCache.get(type);
            if (cachedType != null && !isExpired(type)) {
                // 刷新最后访问时间
                refreshAccessTime(type);
                return cachedType;
            }
            return null;
        }

        /**
         * 缓存类级别的泛型参数
         *
         * @param clazz 目标类
         * @param index 泛型参数索引
         * @param type  要缓存的类型
         */
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

        /**
         * 缓存类型解析结果
         *
         * @param type         原始类型
         * @param resolvedType 解析后的类型
         */
        public void putTypeResolveCache(Type type, Class<?> resolvedType) {
            if (typeResolveCache.size() >= MAX_CACHE_SIZE) {
                clearExpiredCache();
            }
            typeResolveCache.put(type, resolvedType);
            long currentTime = System.currentTimeMillis();
            cacheCreateTime.put(type, currentTime);
            cacheLastAccessTime.put(type, currentTime);
        }

        /**
         * 刷新缓存的最后访问时间
         *
         * @param key 缓存键
         */
        private void refreshAccessTime(Object key) {
            cacheLastAccessTime.put(key, System.currentTimeMillis());
        }

        /**
         * 判断缓存是否过期
         *
         * @param key 缓存键
         *
         * @return 如果缓存不存在或已过期则返回true
         */
        private boolean isExpired(Object key) {
            Long lastAccessTime = cacheLastAccessTime.get(key);
            if (lastAccessTime == null) {
                return true;
            }
            return System.currentTimeMillis() - lastAccessTime > CACHE_EXPIRE_TIME;
        }

        /**
         * 清理过期的缓存
         * 包括类型缓存、解析结果缓存和时间记录
         */
        public void clearExpiredCache() {
            typeCache.entrySet().removeIf(entry -> isExpired(entry.getKey()));
            typeResolveCache.entrySet().removeIf(entry -> isExpired(entry.getKey()));
            cacheCreateTime.entrySet().removeIf(entry -> isExpired(entry.getKey()));
            cacheLastAccessTime.entrySet().removeIf(entry -> isExpired(entry.getKey()));
        }

        /**
         * 清理所有缓存
         */
        public void clearAllCache() {
            typeCache.clear();
            typeResolveCache.clear();
            cacheCreateTime.clear();
            cacheLastAccessTime.clear();
        }
    }

    /**
     * 快速获取泛型的类型的方法
     * 支持获取类继承关系中的泛型参数类型
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

        // 获取父类的泛型信息
        Type genericSuperclass = objectClass.getGenericSuperclass();
        if (genericSuperclass == null) {
            return null;
        }

        Class<T> result;
        try {
            result = switch (genericSuperclass) {
                // 如果父类是普通类，递归获取其泛型信息
                case Class<?> clazz -> getGenericClass(clazz, i);
                // 如果父类是参数化类型，获取其实际类型参数
                case ParameterizedType type -> {
                    Type[] types = type.getActualTypeArguments();
                    if (types.length <= i) {
                        throw new TypeResolutionException(
                                String.format("泛型参数索引 %d 超出范围，类 %s 的泛型参数总数为 %d",
                                        i, objectClass.getName(), types.length));
                    }
                    yield resolveType(types[i]);
                }
                // 其他情况返回null
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
     * 解析Type类型，获取实际的Class对象<p>
     * 支持解析以下类型：<p>
     * 1. Class类型<p>
     * 2. ParameterizedType（参数化类型）<p>
     * 3. TypeVariable（类型变量）<p>
     * 4. WildcardType（通配符类型）<p>
     * 5. GenericArrayType（泛型数组类型）<p>
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
                // 如果是Class类型，直接返回
                case Class<?> clazz -> (Class<T>) clazz;
                // 如果是参数化类型，获取其原始类型
                case ParameterizedType parameterizedType -> {
                    Type rawType = parameterizedType.getRawType();
                    if (rawType instanceof Class<?> rawClass) {
                        yield (Class<T>) rawClass;
                    }
                    throw new TypeResolutionException(
                            String.format("无法解析参数化类型 %s 的原始类型", type));
                }
                // 如果是类型变量，获取其边界类型
                case TypeVariable<?> typeVariable -> {
                    Type[] bounds = typeVariable.getBounds();
                    if (bounds.length == 0) {
                        throw new TypeResolutionException(
                                String.format("类型变量 %s 没有边界", typeVariable.getName()));
                    }
                    // 遍历所有边界，寻找最具体的类型
                    for (Type bound : bounds) {
                        if (bound instanceof Class<?> boundClass) {
                            // 找到第一个具体的类类型边界
                            yield (Class<T>) boundClass;
                        }
                    }
                    // 如果没有找到具体的类类型边界，尝试使用第一个边界
                    Type firstBound = bounds[0];
                    if (firstBound instanceof ParameterizedType parameterizedType) {
                        Type rawType = parameterizedType.getRawType();
                        if (rawType instanceof Class<?> rawClass) {
                            yield (Class<T>) rawClass;
                        }
                    }
                    throw new TypeResolutionException(
                            String.format("无法解析类型变量 %s 的边界类型，所有边界都是接口或通配符类型",
                                    typeVariable.getName()));
                }
                // 如果是通配符类型，获取其边界类型
                case WildcardType wildcardType -> {
                    // 处理上界
                    Type[] upperBounds = wildcardType.getUpperBounds();
                    if (upperBounds.length == 0) {
                        throw new TypeResolutionException("通配符类型没有上界");
                    }
                    // 优先使用上界
                    for (Type upperBound : upperBounds) {
                        if (upperBound instanceof Class<?> upperClass) {
                            // 找到第一个具体的类类型上界
                            yield (Class<T>) upperClass;
                        }
                    }
                    // 如果没有找到具体的类类型上界，尝试处理参数化类型
                    Type firstUpperBound = upperBounds[0];
                    if (firstUpperBound instanceof ParameterizedType parameterizedType) {
                        Type rawType = parameterizedType.getRawType();
                        if (rawType instanceof Class<?> rawClass) {
                            yield (Class<T>) rawClass;
                        }
                    }
                    // 处理下界（通常下界更具体）
                    Type[] lowerBounds = wildcardType.getLowerBounds();
                    if (lowerBounds.length > 0) {
                        Type firstLowerBound = lowerBounds[0];
                        if (firstLowerBound instanceof Class<?> lowerClass) {
                            yield (Class<T>) lowerClass;
                        } else if (firstLowerBound instanceof ParameterizedType parameterizedType) {
                            Type rawType = parameterizedType.getRawType();
                            if (rawType instanceof Class<?> rawClass) {
                                yield (Class<T>) rawClass;
                            }
                        }
                    }
                    throw new TypeResolutionException(
                            String.format("无法解析通配符类型 %s 的边界类型，所有边界都是接口或通配符类型", type));
                }
                // 如果是泛型数组类型，获取其组件类型
                case GenericArrayType genericArrayType -> {
                    Type componentType = genericArrayType.getGenericComponentType();
                    if (componentType instanceof Class<?> componentClass) {
                        yield (Class<T>) componentClass.arrayType();
                    }
                    throw new TypeResolutionException(
                            String.format("无法解析泛型数组类型 %s 的组件类型", type));
                }
                // 不支持的类型
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
     * 用于表示在解析泛型类型时发生的错误
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
