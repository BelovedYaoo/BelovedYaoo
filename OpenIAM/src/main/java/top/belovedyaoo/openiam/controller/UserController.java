package top.belovedyaoo.openiam.controller;

import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.agcore.base.BaseController;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.logs.annotation.InterfaceLog;
import top.belovedyaoo.logs.enums.BusinessType;
import top.belovedyaoo.openiam.entity.po.User;

import java.util.List;

import static top.belovedyaoo.openiam.entity.po.table.UserTableDef.USER;

/**
 * 用户控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<User> {

    @Override
    @InterfaceLog(persistence = false, print = true, businessType = BusinessType.SELECT, identifierCode = "baseId", interfaceName = "BaseController.queryAll", interfaceDesc = "查询")
    public Result queryAll() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(USER.DEFAULT_COLUMNS)
                .from(USER)
                .orderBy(USER.ORDER_NUM, true);
        List<User> queryList = baseMapper().selectListWithRelationsByQuery(queryWrapper);
        return Result.success().singleData(queryList);
    }

}
