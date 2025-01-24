package top.belovedyaoo.opencore.processor;

import cn.dev33.satoken.stp.StpUtil;
import com.tangzc.mybatisflex.annotation.handler.AutoFillHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 插入数据时自动生成创建者ID
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
public class CreatorIdAutoFillProcessor implements AutoFillHandler<String> {

    /**
     * 自动填充CreatorID<p>
     *
     * @param object 需要填充的对象
     * @param clazz  对象的类型
     * @param field  需要填充的字段
     *
     * @return 填充的值
     */
    @Override
    public String getVal(Object object, Class clazz, Field field) {
        return StpUtil.getLoginId("");
    }

}
