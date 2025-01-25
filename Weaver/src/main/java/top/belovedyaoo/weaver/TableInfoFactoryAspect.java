package top.belovedyaoo.weaver;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.BaseReflectorFactory;
import com.mybatisflex.core.table.ColumnInfo;
import com.mybatisflex.core.table.TableInfo;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.invoker.GetFieldInvoker;
import org.apache.ibatis.reflection.invoker.SetFieldInvoker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import top.belovedyaoo.opencore.base.BaseFiled;
import top.belovedyaoo.opencore.tree.Tree;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.belovedyaoo.opencore.tree.Tree.TreeNode.isTree;

/**
 * 表信息工厂切面类
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Aspect
public class TableInfoFactoryAspect {

    /**
     * 树形数据表定义
     */
    private static TableInfo treeTableInfo;

    /**
     * 类与表定义缓存<br>
     * 树形数据类每合并一次表定义都需要大量的反射操作，因此有必要弄一个缓存
     */
    private static final Map<Class<?>, TableInfo> CACHE = Collections.synchronizedMap(new HashMap<>());

    public static TableInfoFactoryAspect aspectOf() {
        return new TableInfoFactoryAspect();
    }

    @Around("execution(* com.mybatisflex.core.table.TableInfoFactory.createTableInfo(..))")
    public Object createTableInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> clazz = (Class<?>) joinPoint.getArgs()[0];
        // 如果不是树形结构，则直接返回原方法结果
        if (!isTree(clazz)) {
            return joinPoint.proceed();
        }
        // 如果缓存命中，则直接返回缓存中的结果，否则执行原方法并缓存结果
        if (CACHE.containsKey(clazz)) {
            return CACHE.get(clazz);
        } else {
            TableInfo originalTableInfo = (TableInfo) joinPoint.proceed();
            mergeTableInfo(originalTableInfo, getTreeTableInfo());
            CACHE.put(clazz, originalTableInfo);
            return originalTableInfo;
        }
    }

    /**
     * 获取树形数据的表定义
     *
     * @return 树形数据的表定义
     *
     * @exception Throwable 基本不会抛出异常
     */
    private TableInfo getTreeTableInfo() throws Throwable {
        if (treeTableInfo == null) {
            Class<?> factoryClass = Class.forName("com.mybatisflex.core.table.TableInfoFactory");
            Method createTableInfoMethod = factoryClass.getDeclaredMethod("createTableInfo", Class.class);
            createTableInfoMethod.setAccessible(true);
            treeTableInfo = (TableInfo) createTableInfoMethod.invoke(null, Tree.TreeNode.class);
        }
        return treeTableInfo;
    }

    /**
     * 将右目标的表信息合并到左目标
     *
     * @param left  左目标表信息
     * @param right 右目标表信息
     *
     * @exception Throwable 基本不会抛出异常
     */
    private void mergeTableInfo(TableInfo left, TableInfo right) throws Throwable {
        // 列信息映射处理
        columnInfoMapping(left, right);
        // 列属性信息处理
        propertyColumnMapping(left, right);
        // 列信息列表处理
        columnInfoList(left, right);
        // 列查询映射处理
        columnQueryMapping(left, right);
        // 所有列定义处理
        allColumns(left, right);
        // 默认查询列定义处理
        defaultQueryColumns(left, right);
        // 列定义处理
        columns(left, right);
        // ================================================ 反射重写开始 ================================================ //
        Field readablePropertyNamesFiled = Reflector.class.getDeclaredField("readablePropertyNames");
        readablePropertyNamesFiled.setAccessible(true);
        Field writablePropertyNamesFiled = Reflector.class.getDeclaredField("writablePropertyNames");
        writablePropertyNamesFiled.setAccessible(true);
        Field setMethodsFiled = Reflector.class.getDeclaredField("setMethods");
        setMethodsFiled.setAccessible(true);
        Field geMethodsFiled = Reflector.class.getDeclaredField("getMethods");
        geMethodsFiled.setAccessible(true);
        Field setTypesFiled = Reflector.class.getDeclaredField("setTypes");
        setTypesFiled.setAccessible(true);
        Field getTypesFiled = Reflector.class.getDeclaredField("getTypes");
        getTypesFiled.setAccessible(true);
        Field caseInsensitivePropertyMapFiled = Reflector.class.getDeclaredField("caseInsensitivePropertyMap");
        caseInsensitivePropertyMapFiled.setAccessible(true);
        // ================================================ 合并反射信息 ================================================ //
        Reflector leftReflector = left.getReflector();
        Reflector rightReflector = right.getReflector();

        // 合并反射中的 readablePropertyNames
        List<String> leftReadablePropertyNames = new ArrayList<>(Arrays.asList((String[]) readablePropertyNamesFiled.get(leftReflector)));
        leftReadablePropertyNames.addAll(Arrays.asList((String[]) readablePropertyNamesFiled.get(rightReflector)));
        // 写入 readablePropertyNames
        readablePropertyNamesFiled.set(leftReflector, leftReadablePropertyNames.toArray(new String[0]));

        // 合并反射中的 writablePropertyNames
        List<String> leftWritablePropertyNames = new ArrayList<>(Arrays.asList((String[]) writablePropertyNamesFiled.get(leftReflector)));
        leftWritablePropertyNames.addAll(Arrays.asList((String[]) writablePropertyNamesFiled.get(rightReflector)));
        // 写入 writablePropertyNames
        writablePropertyNamesFiled.set(leftReflector, leftWritablePropertyNames.toArray(new String[0]));

        // 合并反射中的 setMethods
        @SuppressWarnings("unchecked")
        Map<String, SetFieldInvoker> leftSetMethods = (HashMap<String, SetFieldInvoker>) setMethodsFiled.get(leftReflector);
        @SuppressWarnings("unchecked")
        Map<String, SetFieldInvoker> rightSetMethods = (HashMap<String, SetFieldInvoker>) setMethodsFiled.get(rightReflector);
        leftSetMethods.putAll(rightSetMethods);
        // 写入 setMethods
        setMethodsFiled.set(leftReflector, leftSetMethods);

        // 合并反射中的 getMethods
        @SuppressWarnings("unchecked")
        Map<String, GetFieldInvoker> leftGetMethods = (HashMap<String, GetFieldInvoker>) geMethodsFiled.get(leftReflector);
        @SuppressWarnings("unchecked")
        Map<String, GetFieldInvoker> rightGetMethods = (HashMap<String, GetFieldInvoker>) geMethodsFiled.get(rightReflector);
        leftGetMethods.putAll(rightGetMethods);
        // 写入 getMethods
        geMethodsFiled.set(leftReflector, leftGetMethods);

        // 合并反射中的 setTypes
        @SuppressWarnings("unchecked")
        Map<String, Class<?>> leftSetTypes = (HashMap<String, Class<?>>) setTypesFiled.get(leftReflector);
        @SuppressWarnings("unchecked")
        Map<String, Class<?>> rightSetTypes = (HashMap<String, Class<?>>) setTypesFiled.get(rightReflector);
        leftSetTypes.putAll(rightSetTypes);
        // 写入 setTypes
        setTypesFiled.set(leftReflector, leftSetTypes);

        // 合并反射中的 getTypes
        @SuppressWarnings("unchecked")
        Map<String, Class<?>> leftGetTypes = (HashMap<String, Class<?>>) getTypesFiled.get(leftReflector);
        @SuppressWarnings("unchecked")
        Map<String, Class<?>> rightGetTypes = (HashMap<String, Class<?>>) getTypesFiled.get(rightReflector);
        leftGetTypes.putAll(rightGetTypes);
        // 写入 getTypes
        getTypesFiled.set(leftReflector, leftGetTypes);

        // 合并 caseInsensitivePropertyMap
        @SuppressWarnings("unchecked")
        Map<String, String> leftCaseInsensitivePropertyMap = (HashMap<String, String>) caseInsensitivePropertyMapFiled.get(leftReflector);
        @SuppressWarnings("unchecked")
        Map<String, String> rightCaseInsensitivePropertyMap = (HashMap<String, String>) caseInsensitivePropertyMapFiled.get(rightReflector);
        leftCaseInsensitivePropertyMap.putAll(rightCaseInsensitivePropertyMap);
        // 写入 caseInsensitivePropertyMap
        caseInsensitivePropertyMapFiled.set(leftReflector, leftCaseInsensitivePropertyMap);
        // ================================================ 反射重写结束 ================================================ //
        // 反射工厂
        Field reflectorFactoryField = TableInfo.class.getDeclaredField("reflectorFactory");
        reflectorFactoryField.setAccessible(true);
        // 重写反射工厂
        ReflectorFactory leftReflectorFactory = new BaseReflectorFactory() {
            @Override
            public Reflector findForClass(Class<?> type) {
                return left.getReflector();
            }
        };
        // 反射工厂写入
        reflectorFactoryField.set(left, leftReflectorFactory);
    }

    /**
     * 列定义处理
     * @param left 左目标表
     * @param right 右目标表
     */
    private static void columns(TableInfo left, TableInfo right) {
        // 取得左目标表的列定义
        List<String> leftColumnsList = new ArrayList<>(Arrays.asList(left.getColumns()));
        Collections.addAll(leftColumnsList, right.getColumns());
        String[] mergedColumnsArray = leftColumnsList.toArray(new String[0]);
        // 列定义写入
        left.setColumns(mergedColumnsArray);
    }

    /**
     * 默认查询列定义处理
     * @param left 左目标表
     * @param right 右目标表
     */
    private static void defaultQueryColumns(TableInfo left, TableInfo right) {
        // 取得左目标表的默认查询列定义
        List<String> leftDefaultQueryColumnsList = new ArrayList<>(Arrays.asList(left.getDefaultQueryColumns()));
        // 合并
        Collections.addAll(leftDefaultQueryColumnsList, right.getDefaultQueryColumns());
        String[] mergedDefaultQueryArray = leftDefaultQueryColumnsList.toArray(new String[0]);
        // 默认查询列定义写入
        left.setDefaultQueryColumns(mergedDefaultQueryArray);
    }

    /**
     * 所有列定义处理
     * @param left 左目标表
     * @param right 右目标表
     */
    private static void allColumns(TableInfo left, TableInfo right) {
        // 取得左目标表的所有列定义
        List<String> leftAllColumnsList = new ArrayList<>(Arrays.asList(left.getAllColumns()));
        // 合并
        Collections.addAll(leftAllColumnsList, right.getAllColumns());
        String[] mergedAllColumnsArray = leftAllColumnsList.toArray(new String[0]);
        // 所有列定义写入
        left.setAllColumns(mergedAllColumnsArray);
    }

    /**
     * 列查询映射处理
     * @param left 左目标表
     * @param right 右目标表
     * @throws Throwable 基本不会抛出异常
     */
    private static void columnQueryMapping(TableInfo left, TableInfo right) throws Throwable {
        // 反射处理
        Field columnQueryMappingField = TableInfo.class.getDeclaredField("columnQueryMapping");
        columnQueryMappingField.setAccessible(true);
        // 获取左目标和右目标的列查询映射
        @SuppressWarnings("unchecked")
        Map<String, QueryColumn> leftColumnQueryMapping = (Map<String, QueryColumn>) columnQueryMappingField.get(left);
        @SuppressWarnings("unchecked")
        Map<String, QueryColumn> rightColumnQueryMapping = (Map<String, QueryColumn>) columnQueryMappingField.get(right);
        // 创建一个新的 Map 来存储合并后的列查询映射
        Map<String, QueryColumn> mergedColumnQueryMapping = new HashMap<>(leftColumnQueryMapping);
        // 将右目标的列查询映射中的表名替换成左目标的表名，并合并到新的 Map 中
        rightColumnQueryMapping.forEach((key, value) -> {
            // 创建一个新的 QueryColumn 对象，避免后续修改影响
            QueryColumn newQueryColumn = new QueryColumn();
            newQueryColumn.setName(value.getName());
            newQueryColumn.setAlias(value.getAlias());
            // 通过 BaseID 获取左目标的表名，并设置到新的 QueryColumn 中
            newQueryColumn.setTable(leftColumnQueryMapping.get(BaseFiled.BASE_ID).getTable());
            // 将新的 QueryColumn 添加到合并后的 Map 中
            mergedColumnQueryMapping.put(key, newQueryColumn);
        });
        // 将合并后的 Map 设置为左目标的列查询映射
        columnQueryMappingField.set(left, mergedColumnQueryMapping);
    }

    /**
     * 列信息列表处理
     * @param left 左目标表
     * @param right 右目标表
     * @throws Throwable 基本不会抛出异常
     */
    private static void columnInfoList(TableInfo left, TableInfo right) throws Throwable {
        // 反射处理
        Field columnInfoListField = TableInfo.class.getDeclaredField("columnInfoList");
        columnInfoListField.setAccessible(true);
        // 合并列信息列表
        List<ColumnInfo> mergedColumnInfoList = left.getColumnInfoList();
        mergedColumnInfoList.addAll(right.getColumnInfoList());
        // 列信息列表写入
        columnInfoListField.set(left, mergedColumnInfoList);
    }

    /**
     * 列属性信息处理
     * @param left 左目标表
     * @param right 右目标表
     * @throws Throwable 基本不会抛出异常
     */
    private static void propertyColumnMapping(TableInfo left, TableInfo right) throws Throwable {
        // 反射处理
        Field propertyColumnMapping = TableInfo.class.getDeclaredField("propertyColumnMapping");
        propertyColumnMapping.setAccessible(true);
        // 合并列属性信息
        Map<String, String> mergedPropertyColumnMapping = left.getPropertyColumnMapping();
        mergedPropertyColumnMapping.putAll(right.getPropertyColumnMapping());
        // 列属性信息写入
        propertyColumnMapping.set(left, mergedPropertyColumnMapping);
    }

    /**
     * 列信息映射处理
     * @param left 左目标表
     * @param right 右目标表
     * @throws Throwable 基本不会抛出异常
     */
    private static void columnInfoMapping(TableInfo left, TableInfo right) throws Throwable {
        // 反射处理
        Field columnInfoMapping = TableInfo.class.getDeclaredField("columnInfoMapping");
        columnInfoMapping.setAccessible(true);
        // 合并列信息映射
        @SuppressWarnings("unchecked")
        HashMap<String, ColumnInfo> leftColumnInfoMapping = (HashMap<String, ColumnInfo>) columnInfoMapping.get(left);
        @SuppressWarnings("unchecked")
        HashMap<String, ColumnInfo> rightColumnInfoMapping = (HashMap<String, ColumnInfo>) columnInfoMapping.get(right);
        leftColumnInfoMapping.putAll(rightColumnInfoMapping);
        // 列信息映射写入
        columnInfoMapping.set(left, leftColumnInfoMapping);
    }

}
