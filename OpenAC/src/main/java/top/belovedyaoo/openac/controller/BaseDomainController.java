package top.belovedyaoo.openac.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.openac.model.BaseDomain;
import top.belovedyaoo.opencore.base.BaseTree;
import top.belovedyaoo.opencore.base.DefaultController;

/**
 * 域控制器基类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/domain")
public class BaseDomainController extends DefaultController<BaseDomain> implements BaseTree<BaseDomain> {
}
