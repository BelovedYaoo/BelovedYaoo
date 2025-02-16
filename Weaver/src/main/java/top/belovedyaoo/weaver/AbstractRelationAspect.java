package top.belovedyaoo.weaver;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.relation.AbstractRelation;
import com.mybatisflex.core.table.TableInfo;
import com.mybatisflex.core.table.TableInfoFactory;
import com.mybatisflex.core.util.ArrayUtil;
import com.mybatisflex.core.util.ClassUtil;
import com.mybatisflex.core.util.FieldWrapper;
import com.mybatisflex.core.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.mybatisflex.core.query.QueryMethods.column;
import static top.belovedyaoo.opencore.tree.Tree.TreeNode.isTree;

/**
 * 抽象关联切面类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Aspect
public class AbstractRelationAspect {

    public static AbstractRelationAspect aspectOf() {
        return new AbstractRelationAspect();
    }

    @Around("execution(com.mybatisflex.core.relation.AbstractRelation.new(..))")
    public Object init(ProceedingJoinPoint joinPoint) throws Throwable {
        Class entityClass = (Class) joinPoint.getArgs()[9];
        if (!isTree(entityClass)) {
            return joinPoint.proceed();
        } else {
            System.out.println("拦截到 AbstractRelation 构造方法执行");

            AbstractRelation target = (AbstractRelation) joinPoint.getTarget();
            String selfField = (String) joinPoint.getArgs()[0];
            String targetSchema = (String) joinPoint.getArgs()[1];
            String targetTable = (String) joinPoint.getArgs()[2];
            String targetField = (String) joinPoint.getArgs()[3];
            String valueField = (String) joinPoint.getArgs()[4];
            String joinTable = (String) joinPoint.getArgs()[5];
            String joinSelfColumn = (String) joinPoint.getArgs()[6];
            String joinTargetColumn = (String) joinPoint.getArgs()[7];
            String dataSource = (String) joinPoint.getArgs()[8];

            Field relationField = (Field) joinPoint.getArgs()[10];
            String extraCondition = (String) joinPoint.getArgs()[11];
            String[] selectColumns = (String[]) joinPoint.getArgs()[12];

            Field nameFiled = AbstractRelation.class.getDeclaredField("name");
            nameFiled.setAccessible(true);
            nameFiled.set(target, entityClass.getSimpleName() + "." + relationField.getName());

            Field simpleNameFiled = AbstractRelation.class.getDeclaredField("simpleName");
            simpleNameFiled.setAccessible(true);
            simpleNameFiled.set(target, relationField.getName());

            target.setSelfEntityClass(entityClass);
            target.setRelationField(relationField);
            target.setRelationFieldWrapper(FieldWrapper.of(entityClass, relationField.getName()));
            target.setJoinTable(joinTable);
            target.setJoinSelfColumn(joinSelfColumn);
            target.setJoinTargetColumn(joinTargetColumn);
            target.setDataSource(dataSource);
            target.setSelfField(ClassUtil.getFirstField(entityClass, field -> field.getName().equalsIgnoreCase(selfField)));
            target.setSelfFieldWrapper(FieldWrapper.of(entityClass, selfField));
            TableInfo tableInfo = StringUtil.noText(targetTable) ? TableInfoFactory.ofEntityClass(target.getRelationFieldWrapper().getMappingType()) : TableInfoFactory.ofTableName(targetTable);
            target.setTargetTableInfo(tableInfo);
            target.setTargetSchema(tableInfo != null ? tableInfo.getSchema() : targetSchema);
            target.setTargetTable(tableInfo != null ? tableInfo.getTableName() : targetTable);
            target.setTargetEntityClass((StringUtil.hasText(valueField) && tableInfo != null) ? tableInfo.getEntityClass() : target.getRelationFieldWrapper().getMappingType());
            target.setTargetFieldWrapper(FieldWrapper.of(target.getTargetEntityClass(), targetField));
            target.setValueField(valueField);
            boolean onlyQueryValueField = StringUtil.hasText(valueField);
            target.setOnlyQueryValueField(onlyQueryValueField);

            Field conditionColumnFiled = AbstractRelation.class.getDeclaredField("conditionColumn");
            conditionColumnFiled.setAccessible(true);
            QueryColumn conditionColumn = tableInfo == null ? column(targetTable, StringUtil.camelToUnderline(targetField))
                    : column(targetTable, tableInfo.getColumnByProperty(targetField));
            conditionColumnFiled.set(target, conditionColumn);

            Field selectColumnsFiled = AbstractRelation.class.getDeclaredField("selectColumns");
            selectColumnsFiled.setAccessible(true);
            if (onlyQueryValueField) {
                // 仅绑定字段时只需要查询关联列和该字段列即可
                selectColumnsFiled.set(target, new String[]{conditionColumn.getName(), tableInfo != null ? tableInfo.getColumnByProperty(valueField) : StringUtil.camelToUnderline(valueField)});
            } else {
                if (ArrayUtil.isNotEmpty(selectColumns)) {
                    if (ArrayUtil.contains(selectColumns, conditionColumn.getName())) {
                        selectColumnsFiled.set(target, selectColumns);
                    } else {
                        // 需要追加 conditionColumn，因为进行内存 join 的时候，需要用到这个内容进行对比
                        selectColumnsFiled.set(target, ArrayUtil.concat(selectColumns, new String[]{conditionColumn.getName()}));
                    }
                }
            }

            Method initExtraConditionMethod = AbstractRelation.class.getDeclaredMethod("initExtraCondition", String.class);
            initExtraConditionMethod.setAccessible(true);
            initExtraConditionMethod.invoke(target, extraCondition);

            return target;
        }
    }

}
