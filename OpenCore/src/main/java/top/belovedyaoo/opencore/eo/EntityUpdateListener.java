package top.belovedyaoo.opencore.eo;

import com.mybatisflex.annotation.UpdateListener;
import com.tangzc.mybatisflex.annotation.FieldFill;

/**
 * 实体更新监听器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class EntityUpdateListener extends EntityOperate implements UpdateListener {

    @Override
    public void onUpdate(Object obj) {
        onOperate(FieldFill.UPDATE, obj);
    }

}
