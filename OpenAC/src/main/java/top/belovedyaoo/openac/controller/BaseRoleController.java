package top.belovedyaoo.openac.controller;

import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.logs.annotation.InterfaceLog;
import top.belovedyaoo.openac.model.BaseRole;
import top.belovedyaoo.opencore.base.BaseController;
import top.belovedyaoo.opencore.result.Result;

import java.util.List;

import static top.belovedyaoo.openac.model.table.BaseRoleTableDef.BASE_ROLE;

/**
 * 角色控制器基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/role")
@InterfaceLog(identifierCode = "role", interfaceName = "角色管理", print = true)
public class BaseRoleController extends BaseController<BaseRole> {

    @Override
    public Result queryAll() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(BASE_ROLE.DEFAULT_COLUMNS)
                .from(BASE_ROLE)
                .orderBy(BASE_ROLE.ORDER_NUM, true);
        List<BaseRole> queryList = baseMapper().selectListWithRelationsByQuery(queryWrapper);
        return Result.success().singleData(queryList);
    }
}