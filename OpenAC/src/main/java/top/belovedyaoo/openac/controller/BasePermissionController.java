package top.belovedyaoo.openac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.openac.model.Permission;
import top.belovedyaoo.openac.service.PermissionServiceImpl;
import top.belovedyaoo.opencore.base.DefaultController;
import top.belovedyaoo.opencore.result.Result;

/**
 * 权限控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class BasePermissionController extends DefaultController<Permission> {

    private final PermissionServiceImpl permissionService;

    @Override
    @GetMapping("/queryAll")
    public Result queryAll() {
        System.out.println("父类");
        return Result.success().singleData(permissionService.list());
    }

}
