package top.belovedyaoo.opencore.ac;

import com.mybatisflex.annotation.SetListener;

/**
 * 查询实体类数据时，对实体类的属性设置的监听。
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class EntitySetListener implements SetListener {

    /**
     * 实体类属性设置。
     *
     * @param entity   实体类
     * @param property 属性名
     * @param value    属性值
     * @return 属性值
     */
    @Override
    public Object onSet(Object entity, String property, Object value) {
        return value;
    }

}
