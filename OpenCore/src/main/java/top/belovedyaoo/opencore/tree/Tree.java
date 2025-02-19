package top.belovedyaoo.opencore.tree;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.dromara.autotable.annotation.ColumnComment;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import top.belovedyaoo.opencore.base.BaseFiled;

/**
 * 树形数据接口
 *
 * @author BelovedYaoo
 * @version 1.1
 */
public interface Tree<T extends BaseFiled> {

    String PARENT_ID = "parentId";

    String TREE_PATH = "treePath";

    String IS_LEAF = "isLeaf";

    /**
     * 获取父节点ID
     *
     * @return 父节点ID
     */
    @JsonGetter(PARENT_ID)
    default String parentId() {
        return treeNode().parentId();
    }

    /**
     * 设置父节点ID
     *
     * @param parentId 父节点ID
     *
     * @return TreeNode
     */
    @JsonSetter(PARENT_ID)
    @SuppressWarnings("unchecked")
    default T parentId(String parentId) {
        treeNode().parentId(parentId);
        return (T) this;
    }

    /**
     * 获取完整路径
     *
     * @return 完整路径
     */
    @JsonGetter(TREE_PATH)
    default String treePath() {
        return treeNode().treePath();
    }

    /**
     * 设置完整路径
     *
     * @param treePath 完整路径
     *
     * @return TreeNode
     */
    @SuppressWarnings("unchecked")
    default T treePath(String treePath) {
        treeNode().treePath(treePath);
        return (T) this;
    }

    /**
     * 判断是否为顶级节点
     *
     * @return 是否为顶级节点
     */
    @JsonGetter("isRoot")
    default boolean isRoot() {
        return treeNode().parentId() == null;
    }

    /**
     * 判断是否为叶子节点
     *
     * @return 是否为叶子节点
     */
    @JsonGetter(IS_LEAF)
    default boolean isLeaf() {
        return treeNode().isLeaf();
    }

    /**
     * 设置是否为叶子节点
     *
     * @param isLeaf 是否为叶子节点
     *
     * @return TreeNode
     */
    @SuppressWarnings("unchecked")
    default T isLeaf(boolean isLeaf) {
        treeNode().isLeaf(isLeaf);
        return (T) this;
    }

    /**
     * 返回实例中的树节点
     *
     * @return TreeNode
     */
    TreeNode treeNode();

    /**
     * 树形节点基础字段
     *
     * @author BelovedYaoo
     * @version 1.0
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @Getter(onMethod_ = @JsonGetter)
    @Accessors(fluent = true, chain = true)
    class TreeNode {

        public static final String PARENT_ID = "parent_id";

        public static final String TREE_PATH = "tree_path";

        public static final String IS_LEAF = "is_leaf";

        @ColumnComment("父节点ID,通常为父节点的BaseID")
        @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
        private String parentId;

        @ColumnComment("当前节点的完整路径信息")
        @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 1024)
        private String treePath;

        @ColumnComment("当前节点是否为叶子节点")
        @ColumnType(value = MysqlTypeConstant.BIT, length = 1)
        private boolean isLeaf;

        /**
         * 判断是否为树形结构
         *
         * @param clazz 目标类
         *
         * @return 是否为树形结构
         */
        public static boolean isTree(Class<?> clazz) {
            // 检查当前类的接口
            for (Class<?> i : clazz.getInterfaces()) {
                if (i == Tree.class) {
                    return true;
                }
            }
            // 检查当前类的父类
            Class<?> superclass = clazz.getSuperclass();
            return superclass != null && isTree(superclass);
        }

    }

}
