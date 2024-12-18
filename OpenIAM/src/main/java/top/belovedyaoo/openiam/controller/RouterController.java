package top.belovedyaoo.openiam.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.agcore.base.BaseController;
import top.belovedyaoo.logs.annotation.InterfaceLog;
import top.belovedyaoo.openiam.entity.po.ac.Router;

/**
 * 路由控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/router")
@InterfaceLog(identifierCode = "router", interfaceName = "路由管理", print = true)
public class RouterController extends BaseController<Router> {

}
