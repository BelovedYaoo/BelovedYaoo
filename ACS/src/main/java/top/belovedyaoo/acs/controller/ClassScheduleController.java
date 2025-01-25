package top.belovedyaoo.acs.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.acs.entity.po.ClassSchedule;
import top.belovedyaoo.opencore.base.DefaultController;

/**
 * 课程表控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/classSchedule")
public class ClassScheduleController extends DefaultController<ClassSchedule> {

}
