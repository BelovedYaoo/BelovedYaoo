package top.belovedyaoo.opencore.tree;

import org.dromara.autotable.core.interceptor.BuildTableMetadataInterceptor;
import org.dromara.autotable.core.strategy.TableMetadata;
import org.dromara.autotable.core.strategy.mysql.builder.MysqlColumnMetadataBuilder;
import org.dromara.autotable.core.strategy.mysql.data.MysqlColumnMetadata;
import org.dromara.autotable.core.strategy.mysql.data.MysqlTableMetadata;
import org.dromara.autotable.core.utils.BeanClassUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

import static top.belovedyaoo.opencore.tree.Tree.TreeNode.isTree;

/**
 * 树形数据表信息拦截器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
public class TreeTableBuildInterceptor implements BuildTableMetadataInterceptor {

    /**
     * 树形数据的表元信息
     */
    private static List<MysqlColumnMetadata> treeColumnMetadataList;

    /**
     * 拦截器
     *
     * @param databaseDialect 数据库方言
     * @param tableMetadata   表元数据
     */
    @Override
    public void intercept(String databaseDialect, TableMetadata tableMetadata) {
        if (tableMetadata instanceof MysqlTableMetadata mysqlTableMetadata) {
            // 如果这个表是树形数据
            if (isTree(mysqlTableMetadata.getEntityClass())) {
                // 获取表元信息
                List<MysqlColumnMetadata> originalColumns = mysqlTableMetadata.getColumnMetadataList();
                // 在最后一个元素的位置添加树形数据的表元信息
                getTreeColumnMetadataList().forEach(columnMetadata -> {
                    int lastColumnPosition = originalColumns.getLast().getPosition();
                    columnMetadata.setPosition(++lastColumnPosition);
                    originalColumns.add(columnMetadata);
                });
            }
        }
    }

    /**
     * 获取树形数据的表元信息
     * @return 树形数据的表元信息
     */
    private static List<MysqlColumnMetadata> getTreeColumnMetadataList() {
        // 如果树形数据的表元信息为空，则构建后写入，否则直接返回已有的表元信息
        if (treeColumnMetadataList == null) {
            List<Field> fields = BeanClassUtil.listAllFieldForColumn(Tree.TreeNode.class);
            treeColumnMetadataList = new MysqlColumnMetadataBuilder().buildList(Tree.TreeNode.class, fields);
        }
        return treeColumnMetadataList;
    }

}
