package top.belovedyaoo.openiam.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.opencore.base.BaseController;
import top.belovedyaoo.openiam.entity.po.ac.Permission;

/**
 * 权限控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController<Permission> {

}
