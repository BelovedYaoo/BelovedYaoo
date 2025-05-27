package top.belovedyaoo.openac.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belovedyaoo.openac.core.OperationInfo;
import top.belovedyaoo.openac.model.Operation;

import java.util.LinkedHashMap;

/**
 * 操作控制器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@RestController
@RequestMapping("/operation")
public class OperationController {

    @GetMapping("/getAllOperations")
    public LinkedHashMap<String, LinkedHashMap<String, Operation>> getAllOperations() {
        return OperationInfo.getInstance().operationInfoMap;
    }

}
