package top.belovedyaoo.opencore.processor;

import cn.hutool.core.util.IdUtil;
import com.tangzc.mybatisflex.annotation.handler.AutoFillHandler;
import org.springframework.stereotype.Component;
import top.belovedyaoo.opencore.base.BaseIdFiled;

import java.lang.reflect.Field;

/**
 * 插入数据时自动生成主键ID
 *
 * @author BelovedYaoo
 * @version 1.1
 */
@Component
public class BaseIdAutoFillProcessor implements AutoFillHandler<String> {

    /**
     * 自动填充BaseID<p>
     * 当BaseID不为空时,直接返回已存在的BaseID,否则自动生成一个UUID
     *
     * @param object 需要填充的对象
     * @param clazz  对象的类型
     * @param field  需要填充的字段
     *
     * @return 填充的值
     */
    @Override
    public String getVal(Object object, Class clazz, Field field) {
        String baseId = BaseIdFiled.readBaseId(object);
        return baseId != null ? baseId : IdUtil.simpleUUID();
    }

}
