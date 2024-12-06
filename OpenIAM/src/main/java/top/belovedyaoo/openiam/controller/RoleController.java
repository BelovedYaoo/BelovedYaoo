package top.belovedyaoo.openiam.controller;

import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.agcore.base.BaseController;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.logs.annotation.InterfaceLog;
import top.belovedyaoo.openiam.entity.po.Role;

import java.util.List;

import static top.belovedyaoo.openiam.entity.po.table.RoleTableDef.ROLE;

/**
 * 角色控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/role")
@InterfaceLog(identifierCode = "role", interfaceName = "角色管理", print = true)
public class RoleController extends BaseController<Role> {

    @Override
    public Result queryAll() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(ROLE.DEFAULT_COLUMNS)
                .from(ROLE)
                .orderBy(ROLE.ORDER_NUM, true);
        List<Role> queryList = baseMapper().selectListWithRelationsByQuery(queryWrapper);
        return Result.success().singleData(queryList);
    }
}
