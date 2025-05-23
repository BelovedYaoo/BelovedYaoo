package top.belovedyaoo.acs.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.acs.entity.po.EnterpriseConfig;
import top.belovedyaoo.opencore.base.DefaultController;

/**
 * 企业配置表控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/enterpriseConfig")
public class EnterpriseConfigController extends DefaultController<EnterpriseConfig> {

}
