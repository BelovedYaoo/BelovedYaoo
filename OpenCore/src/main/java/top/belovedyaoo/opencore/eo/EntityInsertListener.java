package top.belovedyaoo.opencore.eo;

import com.mybatisflex.annotation.InsertListener;
import com.tangzc.mybatisflex.annotation.FieldFill;

/**
 * 实体插入监听器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class EntityInsertListener extends EntityOperate implements InsertListener {

    @Override
    public void onInsert(Object obj) {
        onOperate(FieldFill.INSERT, obj);
    }


}
