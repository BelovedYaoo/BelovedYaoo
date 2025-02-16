package top.belovedyaoo.openac.controller;

import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.logs.annotation.InterfaceLog;
import top.belovedyaoo.logs.enums.BusinessType;
import top.belovedyaoo.openac.model.User;
import top.belovedyaoo.opencore.base.DefaultController;
import top.belovedyaoo.opencore.result.Result;

import java.util.List;

import static top.belovedyaoo.openac.model.table.UserTableDef.USER;

/**
 * 用户控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController extends DefaultController<User> {

    @Override
    @InterfaceLog(persistence = false, print = true, businessType = BusinessType.SELECT, identifierCode = "baseId", interfaceName = "BaseController.queryAll", interfaceDesc = "查询")
    public Result queryAll() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
                .from(USER)
                .orderBy(USER.ORDER_NUM, true);
        List<User> queryList = getMapper().selectListWithRelationsByQuery(queryWrapper);
        return Result.success().singleData(queryList);
    }

}