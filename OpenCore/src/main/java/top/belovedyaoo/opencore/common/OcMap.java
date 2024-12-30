package top.belovedyaoo.opencore.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * LinkedHashMap 增强
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@NoArgsConstructor
public class OcMap extends LinkedHashMap<String, Object> implements Serializable {

    private static final String THIS = "this";

    /**
     * 以下元素会被判断为无效值
     */
    public static final Object[] NULL_ELEMENT_ARRAY = {null, ""};
    public static final Set<Object> NULL_ELEMENT_SET;

    static {
        NULL_ELEMENT_SET = new HashSet<>(Arrays.asList(NULL_ELEMENT_ARRAY));
    }

    // ============================ 构造函数 ============================ //

    /**
     * 构建一个空OcMap并返回
     *
     * @return 空的 OcMap 对象
     */
    public static OcMap build() {
        return new OcMap();
    }

    /**
     * 通过一对键值对构建一个OcMap并返回
     *
     * @param key   键名
     * @param value 值
     *
     * @return 新建的 OcMap 对象
     */
    public static OcMap build(String key, Object value) {
        return new OcMap().set(key, value);
    }

    /**
     * 将一个Map转换为一个OcMap并返回
     *
     * @param map 需要转换的Map
     *
     * @return 转换后的 OcMap 对象
     */
    public static OcMap build(Map<String, ?> map) {
        return new OcMap().setMap(map);
    }

    /**
     * 将Json字符串解析为一个OcMap并返回
     *
     * @param jsonString JSON 格式的字符串
     *
     * @return 解析后的 OcMap 对象
     *
     * @exception IllegalArgumentException 如果 JSON 字符串格式不正确
     */
    public static OcMap build(String jsonString) {
        try {
            return new ObjectMapper().readValue(jsonString, OcMap.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("无效JSON: " + jsonString, e);
        }
    }

    /**
     * 将一个对象解析为一个OcMap并返回
     *
     * @param model 对象
     *
     * @return 解析后的 OcMap 对象
     */
    public static OcMap build(Object model) {
        return OcMap.build().setModel(model);
    }

    /**
     * 将一个对象集合解析成为OcMap集合并返回
     *
     * @param list 对象集合
     *
     * @return 解析后的 OcMap 集合
     */
    public static List<OcMap> build(List<?> list) {
        List<OcMap> listMap = new ArrayList<>();
        for (Object model : list) {
            listMap.add(build(model));
        }
        return listMap;
    }

    // ============================ 读操作函数 ============================ //

    /**
     * 通过键名获取一个值
     *
     * @param key 键名
     *
     * @return 对应的值，如果键名为 "this" 则返回当前对象
     */
    @Override
    public Object get(Object key) {
        if (THIS.equals(key)) {
            return this;
        }
        return super.get(key);
    }

    /**
     * 通过键名获取一个值<br>
     * 如果值无效，则返回默认值
     *
     * @param key          键名
     * @param defaultValue 默认值
     *
     * @return 获取到的有效值或默认值
     */
    public Object get(String key, Object defaultValue) {
        Object value = get(key);
        if (isNotValidValue(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 通过键名获取一个值并转为String返回
     *
     * @param key 键名
     *
     * @return 转换后的字符串值
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * 通过键名获取一个值并转为String返回<br>
     * 如果为空，则返回默认值
     *
     * @param key          键名
     * @param defaultValue 默认值
     *
     * @return 获取到的有效字符串值或默认值
     */
    public String getString(String key, String defaultValue) {
        Object value = get(key);
        if (isNotValidValue(value)) {
            return defaultValue;
        }
        return String.valueOf(value);
    }

    /**
     * 通过键名获取一个值并转为int返回
     *
     * @param key 键名
     *
     * @return 转换后的整数值
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 通过键名获取一个值并转为int返回<br>
     * 如果为空，则返回默认值
     *
     * @param key          键名
     * @param defaultValue 默认值
     *
     * @return 获取到的有效整数值或默认值
     */
    public int getInt(String key, int defaultValue) {
        Object value = get(key);
        if (isNotValidValue(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 通过键名获取一个值并转为long返回
     *
     * @param key 键名
     *
     * @return 转换后的长整数值
     */
    public long getLong(String key) {
        return getLong(key, 0L);
    }

    /**
     * 通过键名获取一个值并转为long返回<br>
     * 如果为空，则返回默认值
     *
     * @param key          键名
     * @param defaultValue 默认值
     *
     * @return 获取到的有效长整数值或默认值
     */
    public long getLong(String key, long defaultValue) {
        Object value = get(key);
        if (isNotValidValue(value)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 通过键名获取一个值并转为double返回
     *
     * @param key 键名
     *
     * @return 转换后的双精度浮点数值
     */
    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }

    /**
     * 通过键名获取一个值并转为double返回<br>
     * 如果为空，则返回默认值
     *
     * @param key          键名
     * @param defaultValue 默认值
     *
     * @return 获取到的有效双精度浮点数值或默认值
     */
    public double getDouble(String key, double defaultValue) {
        Object value = get(key);
        if (isNotValidValue(value)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 通过键名获取一个值并转为boolean返回
     *
     * @param key 键名
     *
     * @return 转换后的布尔值
     */
    public boolean getBoolean(String key) {
        Object value = get(key);
        if (isNotValidValue(value)) {
            return false;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    /**
     * 通过键名获取一个值并根据自定义格式转为Date返回
     *
     * @param key    键名
     * @param format 日期格式
     *
     * @return 转换后的日期对象
     *
     * @exception IllegalArgumentException 如果日期格式不正确或无法解析
     */
    public Date getDateByFormat(String key, String format) {
        String dateString = getString(key);
        if (dateString == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(format).parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("无法解析日期: " + dateString + " 使用格式: " + format, e);
        }
    }

    /**
     * 通过键名获取一个值并根据yyyy-MM-dd格式转为Date返回
     *
     * @param key 键名
     *
     * @return 转换后的日期对象
     *
     * @exception IllegalArgumentException 如果日期格式不正确或无法解析
     */
    public Date getDate(String key) {
        return getDateByFormat(key, "yyyy-MM-dd");
    }

    /**
     * 通过键名获取一个值并根据yyyy-MM-dd HH:mm:ss格式转为Date返回
     *
     * @param key 键名
     *
     * @return 转换后的日期对象
     *
     * @exception IllegalArgumentException 如果日期格式不正确或无法解析
     */
    public Date getDateTime(String key) {
        return getDateByFormat(key, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 通过键名获取一个值并转为OcMap返回
     *
     * @param key 键名
     *
     * @return 转换后的OcMap对象
     *
     * @exception IllegalArgumentException 如果值无法转化为OcMap
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public OcMap getMap(String key) {
        Object value = get(key);
        return switch (value) {
            // 如果为空，则构建一个空OcMap返回
            case null -> OcMap.build();
            // 如果为OcMap，则直接返回
            case OcMap ocMap -> ocMap;
            // 如果为Map，则构建一个OcMap返回
            case Map map -> OcMap.build(map);
            // 如果为String，则以Json构建一个OcMap返回
            case String s -> OcMap.build(s);
            // 否则抛出异常
            default -> throw new IllegalArgumentException("值无法转化为OcMap: " + value);
        };
    }

    /**
     * 将列表中的每个元素转换为指定类型并返回新的列表
     *
     * @param list 列表
     * @param cs   目标类型
     * @param <T>  泛型类型
     *
     * @return 转换后的列表
     *
     * @exception IllegalArgumentException 如果无法将某个元素转换为目标类型
     */
    private <T> List<T> convertList(List<?> list, Class<T> cs) {
        List<T> result = new ArrayList<>();
        for (Object obj : list) {
            T convertedObj = getValueByClass(obj, cs);
            result.add(convertedObj);
        }
        return result;
    }

    /**
     * 通过键名获取一个值并转为集合返回<br>
     * 如果值无效，则创建个新集合返回
     *
     * @param key 键名
     *
     * @return 转换后的列表对象
     */
    @SuppressWarnings("unchecked")
    public List<Object> getList(String key) {
        Object value = get(key);
        if (isNotValidValue(value)) {
            return new ArrayList<>();
        }
        if (value instanceof List) {
            return (List<Object>) value;
        }
        List<Object> list = new ArrayList<>();
        list.add(value);
        return list;
    }

    /**
     * 通过键名与泛型类型获取一个值并转为指定泛型类型的集合返回<br>
     * 如果值无效，则创建个新集合返回
     *
     * @param key 键名
     * @param cs  泛型类型
     * @param <T> 泛型类型
     *
     * @return 转换后的列表对象
     *
     * @exception IllegalArgumentException 如果无法将值转换为目标类型
     */
    public <T> List<T> getList(String key, Class<T> cs) {
        List<Object> list = getList(key);
        return convertList(list, cs);
    }

    /**
     * 通过键名与泛型类型获取一个值并转为字符串<br>
     * 解析该字符串转为指定泛型类型的集合返回<br>
     * 如果值无效，则创建个新集合返回
     *
     * @param key 键名
     * @param cs  泛型类型
     * @param <T> 泛型类型
     *
     * @return 转换后的列表对象
     *
     * @exception IllegalArgumentException 如果无法将值转换为目标类型
     */
    public <T> List<T> getListByComma(String key, Class<T> cs) {
        String listStr = getString(key);
        if (listStr == null || listStr.isEmpty()) {
            return new ArrayList<>();
        }
        String[] arr = listStr.split(",");
        List<String> trimmedArr = Arrays.stream(arr)
                .map(str -> cs == int.class || cs == Integer.class || cs == long.class || cs == Long.class ? str.trim() : str)
                .toList();
        return convertList(trimmedArr, cs);
    }

    /**
     * 通过键名获取一个值并转为指定泛型类型的集合返回<br>
     *
     * @param cs  指定泛型类型
     * @param <T> 指定泛型类型
     *
     * @return 转换后的对象
     */
    public <T> T getModel(Class<T> cs) {
        try {
            T instance = cs.getDeclaredConstructor().newInstance();
            return getModelByObject(instance);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException("无法实例化对象: " + cs.getName(), e);
        }
    }

    /**
     * 通过键名获取一个值并转为指定泛型类型的对象返回
     *
     * @param obj 对象
     * @param <T> 对象类型
     *
     * @return 对象
     */
    public <T> T getModelByObject(T obj) {
        // 获取类型
        Class<?> cs = obj.getClass();
        // 循环复制
        for (Field field : cs.getDeclaredFields()) {
            try {
                // 获取对象
                Object value = this.get(field.getName());
                if (value == null) {
                    continue;
                }
                field.setAccessible(true);
                Object valueConvert = getValueByClass(value, field.getType());
                field.set(obj, valueConvert);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException("属性取值出错：" + field.getName(), e);
            }
        }
        return obj;
    }

    /**
     * 将指定值转化为指定类型并返回
     *
     * @param obj 值
     * @param cs  类型
     * @param <T> 泛型类型
     *
     * @return 转换后的值
     *
     * @exception IllegalArgumentException 如果无法将值转换为目标类型
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValueByClass(Object obj, Class<T> cs) {
        String obj2 = String.valueOf(obj);
        Object obj3;
        if (cs.equals(String.class)) {
            obj3 = obj2;
        } else if (cs.equals(int.class) || cs.equals(Integer.class)) {
            try {
                obj3 = Integer.valueOf(obj2);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无法将 " + obj2 + " 转换为 int", e);
            }
        } else if (cs.equals(long.class) || cs.equals(Long.class)) {
            try {
                obj3 = Long.valueOf(obj2);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无法将 " + obj2 + " 转换为 long", e);
            }
        } else if (cs.equals(short.class) || cs.equals(Short.class)) {
            try {
                obj3 = Short.valueOf(obj2);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无法将 " + obj2 + " 转换为 short", e);
            }
        } else if (cs.equals(byte.class) || cs.equals(Byte.class)) {
            try {
                obj3 = Byte.valueOf(obj2);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无法将 " + obj2 + " 转换为 byte", e);
            }
        } else if (cs.equals(float.class) || cs.equals(Float.class)) {
            try {
                obj3 = Float.valueOf(obj2);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无法将 " + obj2 + " 转换为 float", e);
            }
        } else if (cs.equals(double.class) || cs.equals(Double.class)) {
            try {
                obj3 = Double.valueOf(obj2);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无法将 " + obj2 + " 转换为 double", e);
            }
        } else if (cs.equals(boolean.class) || cs.equals(Boolean.class)) {
            obj3 = Boolean.valueOf(obj2);
        } else {
            throw new IllegalArgumentException("无法将 " + obj2 + " 转换为 " + cs.getName());
        }
        return (T) obj3;
    }

    // ============================ 写操作函数 ============================ //

    /**
     * 写入一对键值对
     *
     * @param key   键名
     * @param value 值
     *
     * @return 当前对象
     */
    public OcMap set(String key, Object value) {
        // 防止敏感key
        if (THIS.equalsIgnoreCase(key)) {
            return this;
        }
        put(key, value);
        return this;
    }

    /**
     * 提取一个Map中所有的键值对并写入当前对象
     *
     * @param map Map对象
     *
     * @return 当前对象
     */
    public OcMap setMap(Map<String, ?> map) {
        if (map != null) {
            for (String key : map.keySet()) {
                this.set(key, map.get(key));
            }
        }
        return this;
    }

    /**
     * 将一个对象解析塞进OcMap
     *
     * @param model 对象
     *
     * @return 当前对象
     */
    public OcMap setModel(Object model) {
        if (model == null) {
            return this;
        }
        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                if (!isStatic) {
                    Object value = field.get(model);
                    this.set(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("无法访问字段: " + field.getName(), e);
            }
        }
        return this;
    }

    // ============================ 删操作函数 ============================ //

    /**
     * 通过键名删除一对键值对
     *
     * @param key 键名
     *
     * @return 当前对象
     */
    public OcMap delete(String key) {
        remove(key);
        return this;
    }

    /**
     * 通过若干键名删除键值对
     *
     * @param keys 键名数组
     *
     * @return 当前对象
     */
    public OcMap clearIn(String... keys) {
        List<String> keysToRemove = Arrays.asList(keys);
        keysToRemove.forEach(keySet()::remove);
        return this;
    }

    /**
     * 清理所有无效值的键值对
     *
     * @return 清理后的当前对象
     */
    public OcMap clearNull() {
        this.keySet().removeIf(key -> isNotValidValue(get(key)));
        return this;
    }

    /**
     * 清理掉不在列表中的 key
     *
     * @param keys 键名数组
     *
     * @return 当前对象
     */
    public OcMap clearNotIn(String... keys) {
        List<String> keysToRetain = Arrays.asList(keys);
        keySet().retainAll(keysToRetain);
        return this;
    }

    /**
     * 清理所有key
     *
     * @return 当前对象
     */
    public OcMap clearAll() {
        clear();
        return this;
    }

    // ============================ 克隆函数 ============================ //

    /**
     * 克隆指定keys，返回一个克隆后的OcMap
     *
     * @param keys 键名数组
     *
     * @return 克隆后的OcMap
     */
    public OcMap cloneKeys(String... keys) {
        OcMap map = build();
        Arrays.stream(keys).forEach(key -> {
            Object value = this.get(key);
            if (value != null) {
                map.set(key, value);
            }
        });
        return map;
    }

    /**
     * 深克隆指定keys，返回一个克隆后的OcMap
     *
     * @param keys 键名数组
     *
     * @return 克隆后的OcMap
     */
    public OcMap deepCloneKeys(String... keys) {
        OcMap map = build();
        Arrays.stream(keys).forEach(key -> {
            Object value = this.get(key);
            if (value != null) {
                map.set(key, deepCopyValue(value));
            }
        });
        return map;
    }

    /**
     * 克隆所有key，返回一个新的OcMap
     *
     * @return 克隆后的OcMap
     */
    public OcMap cloneOcMap() {
        OcMap so = build();
        forEach(so::set);
        return so;
    }

    /**
     * 深克隆所有key，返回一个新的OcMap
     *
     * @return 克隆后的OcMap
     */
    public OcMap deepCloneOcMap() {
        OcMap copy = build();
        for (Map.Entry<String, Object> entry : entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            copy.put(key, deepCopyValue(value));
        }
        return copy;
    }

    private Object deepCopyValue(Object value) {
        if (value instanceof OcMap) {
            return ((OcMap) value).cloneOcMap();
        } else if (value instanceof List<?>) {
            List<Object> newList = new ArrayList<>();
            for (Object item : (List<?>) value) {
                newList.add(deepCopyValue(item));
            }
            return newList;
        } else if (value instanceof Map<?, ?>) {
            Map<Object, Object> newMap = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                newMap.put(deepCopyValue(entry.getKey()), deepCopyValue(entry.getValue()));
            }
            return newMap;
        } else if (value != null && value.getClass().isArray()) {
            int length = Array.getLength(value);
            Object newArray = Array.newInstance(value.getClass().getComponentType(), length);
            for (int i = 0; i < length; i++) {
                Array.set(newArray, i, deepCopyValue(Array.get(value, i)));
            }
            return newArray;
        } else if (value instanceof Enum) {
            // 枚举类型是不可变的，直接返回原对象
            return value;
        } else if (value instanceof Number || value instanceof Boolean || value instanceof Character || value instanceof String) {
            // 基本类型和不可变对象，直接返回原对象
            return value;
        } else {
            // 其他类型可以考虑实现 Cloneable 或 Serializable
            throw new IllegalArgumentException("无法深克隆类型: " + Objects.requireNonNull(value).getClass().getName());
        }
    }

    // ============================ Key重命名函数 ============================ //

    /**
     * 将所有key转为大写
     *
     * @return 当前对象
     */
    public OcMap toUpperCase() {
        return transformKeys(String::toUpperCase);
    }

    /**
     * 将所有key转为小写
     *
     * @return 当前对象
     */
    public OcMap toLowerCase() {
        return transformKeys(String::toLowerCase);
    }

    /**
     * 将所有key中下划线转为中划线模式 (kebab-case风格)
     *
     * @return 当前对象
     */
    public OcMap toKebabCase() {
        return transformKeys(OcMap::wordEachKebabCase);
    }

    /**
     * 将所有key中下划线转为小驼峰模式
     *
     * @return 当前对象
     */
    public OcMap toHumpCase() {
        return transformKeys(OcMap::wordEachBigFs);
    }

    /**
     * 将所有key中小驼峰转为下划线模式
     *
     * @return 当前对象
     */
    public OcMap humpToLineCase() {
        return transformKeys(OcMap::wordHumpToLine);
    }

    /**
     * 通用方法：根据提供的转换函数转换键名
     *
     * @param transformer 键名转换函数
     *
     * @return 当前对象
     */
    private OcMap transformKeys(java.util.function.Function<String, String> transformer) {
        OcMap transformedMap = build();
        forEach((key, value) -> transformedMap.set(transformer.apply(key), value));
        clearAll().putAll(transformedMap);
        return this;
    }

    /**
     * 将字符串中的下划线转换为中划线 (kebab-case)
     *
     * @param key 原始字符串
     *
     * @return 转换后的字符串
     */
    private static String wordEachKebabCase(String key) {
        return key.replace('_', '-');
    }

    /**
     * 将字符串中的下划线转换为小驼峰模式
     *
     * @param key 原始字符串
     *
     * @return 转换后的字符串
     */
    private static String wordEachBigFs(String key) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpper = false;
        for (char c : key.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    sb.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串中的小驼峰模式转换为下划线模式
     *
     * @param key 原始字符串
     *
     * @return 转换后的字符串
     */
    private static String wordHumpToLine(String key) {
        StringBuilder sb = new StringBuilder();
        for (char c : key.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append('_').append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // ============================ 格式转换函数 ============================ //

    /**
     * 转为JSON字符串
     *
     * @return JSON 字符串
     */
    public String toJsonString() {
        return writeValueAsString(false);
    }

    /**
     * 转为格式化的JSON字符串
     *
     * @return 格式化的 JSON 字符串
     */
    public String toJsonFormatString() {
        return writeValueAsString(true);
    }

    /**
     * 通用方法：根据是否格式化将对象转换为 JSON 字符串
     *
     * @param pretty 是否格式化
     *
     * @return JSON 字符串
     */
    private String writeValueAsString(boolean pretty) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (pretty) {
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            } else {
                objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
            }
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("无法转换为 JSON 字符串", e);
        }
    }

    // ============================ 有效性判断函数 ============================ //

    /**
     * 通过一个键名判断一个值是否为有效值
     *
     * @param key 键名
     *
     * @return 是否为有效值
     */
    public boolean isValidKey(String key) {
        return isValidValue(get(key));
    }

    /**
     * 判断一个值是否为有效值
     *
     * @param value 值
     *
     * @return 是否为有效值
     */
    public boolean isValidValue(Object value) {
        return !isNotValidValue(value);
    }

    /**
     * 判断一个值是否为无效值
     *
     * @param value 值
     *
     * @return 是否为无效值
     */
    public boolean isNotValidValue(Object value) {
        return NULL_ELEMENT_SET.contains(value);
    }

}
