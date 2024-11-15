package top.belovedyaoo.agcore.log;

import cn.dev33.satoken.stp.StpUtil;
import com.tangzc.mybatisflex.annotation.handler.AutoFillHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 操作人ID自动填充处理器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
public class OperatorIdAutoFillProcessor implements AutoFillHandler<String> {

    @Override
    public String getVal(Object object, Class<?> clazz, Field field) {
        return StpUtil.getLoginId("-1");
    }

}
