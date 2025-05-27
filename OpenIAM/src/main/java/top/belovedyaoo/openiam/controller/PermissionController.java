package top.belovedyaoo.openiam.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.openac.controller.BasePermissionController;
import top.belovedyaoo.openac.model.Permission;
import top.belovedyaoo.openac.service.PermissionServiceImpl;
import top.belovedyaoo.opencore.advice.annotation.CoverRouter;
import top.belovedyaoo.opencore.result.Result;

@CoverRouter
@RestController
@RequestMapping("/permission")
public class PermissionController extends BasePermissionController {

    public PermissionController(PermissionServiceImpl permissionService) {
        super(permissionService);
    }

    /**
     * 更新数据
     *
     * @param entity 要更新的数据
     *
     * @return 操作结果
     */
    @Override
    @PostMapping("/update")
    public Result update(Permission entity) {
        System.out.println("子类");
        return super.update(entity);
    }

}
