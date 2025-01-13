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

/**
 * 树形数据接口
 *
 * @author BelovedYaoo
 * @version 1.1
 */
public interface Tree {

    /**
     * 获取父节点ID
     *
     * @return 父节点ID
     */
    @JsonGetter(TreeNode.PARENT_ID)
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
    @JsonSetter(TreeNode.PARENT_ID)
    default TreeNode parentId(String parentId) {
        return treeNode().parentId(parentId);
    }

    /**
     * 获取完整路径
     *
     * @return 完整路径
     */
    @JsonGetter(TreeNode.TREE_PATH)
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
    @JsonSetter(TreeNode.TREE_PATH)
    default TreeNode treePath(String treePath) {
        return treeNode().treePath(treePath);
    }

    /**
     * 判断是否为顶级节点
     *
     * @return 是否为顶级节点
     */
    @JsonGetter(TreeNode.IS_ROOT)
    default boolean isRoot() {
        return treeNode().isRoot();
    }

    /**
     * 设置是否为顶级节点
     *
     * @param isRoot 是否为顶级节点
     *
     * @return TreeNode
     */
    @JsonSetter(TreeNode.IS_ROOT)
    default TreeNode isRoot(boolean isRoot) {
        return treeNode().isRoot(isRoot);
    }

    /**
     * 判断是否为叶子节点
     *
     * @return 是否为叶子节点
     */
    @JsonGetter(TreeNode.IS_LEAF)
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
    @JsonSetter(TreeNode.IS_LEAF)
    default TreeNode isLeaf(boolean isLeaf) {
        return treeNode().isLeaf(isLeaf);
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

        public static final String PARENT_ID = "parentId";

        public static final String TREE_PATH = "treePath";

        public static final String IS_ROOT = "isRoot";

        public static final String IS_LEAF = "isLeaf";

        @ColumnComment("父节点ID,通常为父节点的BaseID")
        @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
        private String parentId;

        @ColumnComment("当前节点的完整路径信息")
        @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 1024)
        private String treePath;

        @ColumnComment("当前节点是否为顶级节点")
        @ColumnType(value = MysqlTypeConstant.BIT, length = 1)
        private boolean isRoot;

        @ColumnComment("当前节点是否为叶子节点")
        @ColumnType(value = MysqlTypeConstant.BIT, length = 1)
        private boolean isLeaf;

        /**
         * 判断是否为树形结构
         * @param clazz 目标类
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
