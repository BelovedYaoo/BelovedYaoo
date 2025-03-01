package top.belovedyaoo.opencore.tree;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.query.QueryWrapper;
import top.belovedyaoo.opencore.base.BaseFiled;
import top.belovedyaoo.opencore.base.BaseIdFiled;

/**
 * 树操作监听器
 *
 * @author BelovedYaoo
 * @version 1.1
 */
public class TreeOperateListener implements InsertListener, UpdateListener {

    @Override
    public void onInsert(Object o) {
        if (o instanceof Tree<?> tree) {
            operate(tree, false);
        }
    }

    @Override
    public void onUpdate(Object o) {
        if (o instanceof Tree<?> tree) {
            operate(tree, true);
        }
    }

    /**
     * 树操作
     *
     * @param tree     需要操作的树节点
     * @param isUpdate 是否属于更新操作
     * @param <T>      树节点类型
     */
    private <T extends Tree<? extends BaseFiled>> void operate(T tree, boolean isUpdate) {
        operateTreePath(tree, isUpdate);
        operateIsLeaf(tree, isUpdate);
    }

    /**
     * 树路径操作
     *
     * @param tree     需要操作的树节点
     * @param isUpdate 是否属于更新操作
     * @param <T>      树节点类型
     */
    public <T extends Tree<? extends BaseFiled>> void operateTreePath(T tree, boolean isUpdate) {
        // 拼接树路径
        StringBuilder treePath = new StringBuilder();
        // 如果存在父节点，则拼接父节点的树路径
        if (tree.parentId() != null) {
            @SuppressWarnings("rawtypes")
            BaseMapper<? extends Tree> treeMapper = Mappers.ofEntityClass(tree.getClass());
            String parentTreePath = treeMapper.selectOneByQueryAs(QueryWrapper.create()
                    .select(Tree.TreeNode.TREE_PATH)
                    .where(BaseIdFiled.eqBaseId(tree.parentId())), String.class);
            treePath.append(parentTreePath);
        }
        treePath.append("/");
        if (isUpdate) {
            treePath.append(tree.readBaseId());
        } else {
            String id = tree.readBaseId();
            String treeBaseId;
            if (id != null && !id.isBlank()) {
                treeBaseId = id;
            } else {
                treeBaseId = IdUtil.simpleUUID();
            }
            treePath.append(treeBaseId);
        }
        tree.treePath(treePath.toString());
    }

    /**
     * 是否叶子节点操作
     *
     * @param tree     需要操作的树节点
     * @param isUpdate 是否属于更新操作
     * @param <T>      树节点类型
     */
    @SuppressWarnings("unchecked")
    public <T extends Tree<? extends BaseFiled>> void operateIsLeaf(T tree, boolean isUpdate) {
        // 自身判断
        if (isUpdate) {
            BaseMapper<T> treeMapper = (BaseMapper<T>) Mappers.ofEntityClass(tree.getClass());
            boolean existChild = treeMapper.selectCountByQuery(QueryWrapper.create().where(Tree.TreeNode.eqParentId(tree.readBaseId()))) > 0;
            tree.isLeaf(!tree.treeNode().state() && !existChild);
        } else {
            tree.isLeaf(true);
        }
        // 父节点判断
        if (tree.parentId() != null) {
            BaseMapper<T> treeMapper = (BaseMapper<T>) Mappers.ofEntityClass(tree.getClass());
            T tree1 = treeMapper.selectOneByQuery(QueryWrapper.create().where(BaseIdFiled.eqBaseId(tree.parentId())));
            if (tree1 != null) {
                tree1.treeNode().state(true);
                treeMapper.update(tree1);
            }
        }
    }

}
