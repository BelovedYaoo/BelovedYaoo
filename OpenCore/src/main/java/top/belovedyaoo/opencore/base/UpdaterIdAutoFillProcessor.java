package top.belovedyaoo.opencore.base;

import cn.dev33.satoken.stp.StpUtil;
import com.tangzc.mybatisflex.annotation.handler.AutoFillHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 插入与修改数据时自动修改更新者ID
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
public class UpdaterIdAutoFillProcessor implements AutoFillHandler<String> {

    /**
     * 自动填充UpdaterID<p>
     *
     * @param object 需要填充的对象
     * @param clazz  对象的类型
     * @param field  需要填充的字段
     *
     * @return 填充的值
     */
    @Override
    public String getVal(Object object, Class clazz, Field field) {
        String lastUpdaterId = BaseFiled.convertToBaseFiled(object).baseId();
        String updaterId = StpUtil.getLoginId("");
        return updaterId.isEmpty() ? lastUpdaterId : updaterId;
    }

}
