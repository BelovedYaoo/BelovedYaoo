package top.belovedyaoo.opencore.base;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.opencore.tree.Tree;
import top.belovedyaoo.opencore.tree.TreeService;

/**
 * 树形数据接口
 *
 * @param <T> 实体类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface BaseTree<T extends Tree> extends TreeService<T> {

    /**
     * 根据父节点ID查询所有子节点<br>
     * 返回树形结构
     *
     * @param baseId 父节点ID
     *
     * @return 由父节点以及子节点组成的树形结构
     */
    @PostMapping("/findNodeTreeByBaseId")
    default Result _findNodeTreeByBaseId(@RequestParam(value = "baseId") String baseId) {
        return Result.success().singleData(findNodeTreeByBaseId(baseId));
    }

    /**
     * 根据父节点ID查询所有子节点<br>
     * 返回平铺结构
     *
     * @param baseId 父节点ID
     *
     * @return 父节点以及子节点
     */
    @PostMapping("/findAllNodeByBaseId")
    default Result _findAllNodeByBaseId(@RequestParam(value = "baseId") String baseId) {
        return Result.success().singleData(findAllNodeByBaseId(baseId));
    }

    /**
     * 根据父节点ID删除所有子节点
     *
     * @param baseId 父节点ID
     *
     * @return 删除结果
     */
    @PostMapping("/deleteNodeTreeByBaseId")
    default Result _deleteNodeTreeByBaseId(@RequestParam(value = "baseId") String baseId) {
        return Result.success().singleData(deleteNodeTreeByBaseId(baseId));
    }

}
