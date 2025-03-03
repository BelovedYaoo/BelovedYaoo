package top.belovedyaoo.openac.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.openac.model.Permission;
import top.belovedyaoo.opencore.base.DefaultController;

/**
 * 权限控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/permission")
public class PermissionController extends DefaultController<Permission> {
}
