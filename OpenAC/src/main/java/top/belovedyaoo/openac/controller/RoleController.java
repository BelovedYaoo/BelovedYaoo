package top.belovedyaoo.openac.controller;

import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.logs.annotation.InterfaceLog;
import top.belovedyaoo.openac.model.Role;
import top.belovedyaoo.openac.service.RoleServiceImpl;
import top.belovedyaoo.opencore.base.DefaultController;
import top.belovedyaoo.opencore.result.Result;

import java.util.List;

import static top.belovedyaoo.openac.model.table.RoleTableDef.ROLE;

/**
 * 角色控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/role")
@InterfaceLog(identifierCode = "role", interfaceName = "角色管理", print = true)
@RequiredArgsConstructor
public class RoleController extends DefaultController<Role> {

    private final RoleServiceImpl roleService;

    @Override
    public Result queryAll() {
        // QueryWrapper queryWrapper = QueryWrapper.create()
        //         .select(ROLE.ALL_COLUMNS)
        //         .from(ROLE)
        //         .orderBy(ROLE.ORDER_NUM, true);
        // List<Role> queryList = getMapper().selectListWithRelationsByQuery(queryWrapper);
        // List<Role> queryList = getMapper().selectListByQuery(queryWrapper);
        return Result.success().singleData(roleService.list());
    }
}