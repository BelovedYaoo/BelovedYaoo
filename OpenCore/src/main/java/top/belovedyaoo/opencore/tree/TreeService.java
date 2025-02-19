package top.belovedyaoo.opencore.tree;

import com.mybatisflex.core.constant.SqlConsts;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.belovedyaoo.opencore.base.BaseControllerMethod;
import top.belovedyaoo.opencore.base.BaseFiled;
import top.belovedyaoo.opencore.base.BaseIdFiled;

import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.select;

/**
 * 树形数据服务
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Service
public interface TreeService<T extends BaseFiled & Tree<T>> extends BaseControllerMethod<T> {

    String CTE = "CTE";

    String TABLE_ALIAS = "m";

    /**
     * 根据BaseID查询该节点以及所有子节点<br>
     * 返回该节点以及所有子节点组成的树形结构<br>
     * 节点类中需要有类型为节点List的字段，且使用@Relation注解进行关联
     *
     * @param baseId 节点ID
     *
     * @return 由节点以及所有子节点组成的树形结构
     */
    default T findNodeTreeByBaseId(String baseId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .from(getOriginalClass())
                .where(BaseIdFiled.eqBaseId(baseId));
        // 通过 WithRelations 来查询，详见 https://mybatis-flex.com/zh/base/relations-query.html
        return getMapper().selectOneWithRelationsByQuery(queryWrapper);
    }

    /**
     * 根据BaseID查询该节点以及所有子节点<br>
     * 返回该节点以及所有子节点组成的列表
     *
     * @param baseId 节点ID
     *
     * @return 由节点以及所有子节点组成的列表
     */
    default List<T> findAllNodeByBaseId(String baseId) {
        return getMapper().selectListByQuery(buildTreeSelectQuery(baseId));
    }

    /**
     * 根据BaseID删除该节点以及所有子节点<br>
     *
     * @param baseId 节点ID
     *
     * @return 受影响的行数
     */
    default int deleteNodeTreeByBaseId(String baseId) {
        // 先拿到该节点以及所有子节点的BaseID
        List<String> ts = getMapper().selectListByQueryAs(buildTreeSelectQuery(baseId, BaseIdFiled.BASE_ID), String.class);
        return getMapper().deleteBatchByIds(ts);
    }

    /**
     * 构建一个根据BaseID查询该节点以及所有子节点的查询器
     *
     * @param baseId 节点ID
     * @param columns 查询字段(为空则查询所有字段)
     *
     * @return QueryWrapper
     */
    default QueryWrapper buildTreeSelectQuery(String baseId, String... columns) {
        return new QueryWrapper()
                .withRecursive(CTE)
                .asSelect(
                        select().unionAll(
                                select(new QueryColumn(TABLE_ALIAS, SqlConsts.ASTERISK))
                                        .from(getOriginalClass())
                                        .as(TABLE_ALIAS)
                                        .innerJoin(CTE)
                                        .on(
                                                TABLE_ALIAS + SqlConsts.REFERENCE + Tree.TreeNode.PARENT_ID + SqlConsts.EQUALS + CTE + SqlConsts.REFERENCE + BaseIdFiled.BASE_ID
                                                        + SqlConsts.AND
                                                        + TABLE_ALIAS + SqlConsts.REFERENCE + BaseFiled.DELETED_AT + SqlConsts.IS_NULL)
                        ).from(getOriginalClass()).where(BaseIdFiled.eqBaseId(baseId))
                )
                .select(columns)
                .from(CTE);
        /*
         * WITH RECURSIVE CTE AS ((SELECT * FROM `domain` WHERE  base_id = '0b2d398202aa79c77798d0118ce8fc5d' ) UNION ALL (SELECT `m`.* FROM `domain` AS `m` INNER JOIN `CTE` ON  m.parent_id = CTE.base_id AND m.deleted_at IS NULL )) SELECT * FROM `CTE`
         */
    }

}
