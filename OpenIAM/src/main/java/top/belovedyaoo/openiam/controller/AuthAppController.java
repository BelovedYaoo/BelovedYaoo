package top.belovedyaoo.openiam.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.opencore.base.DefaultController;
import top.belovedyaoo.openiam.entity.po.AuthorizedApplication;

/**
 * 授权应用控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/authApp")
public class AuthAppController extends DefaultController<AuthorizedApplication> {

}
