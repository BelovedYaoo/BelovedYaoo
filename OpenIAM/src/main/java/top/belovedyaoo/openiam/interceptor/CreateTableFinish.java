package top.belovedyaoo.openiam.interceptor;

import org.dromara.autotable.core.callback.CreateTableFinishCallback;
import org.dromara.autotable.core.strategy.TableMetadata;
import org.springframework.stereotype.Component;
import top.belovedyaoo.opencore.common.OcMap;

/**
 * 建表操作完成的回调<br>
 * 用于记录建表的元数据
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
public class CreateTableFinish implements CreateTableFinishCallback {

    public static final OcMap TABLE_METADATA_MAP = OcMap.build();

    @Override
    public void afterCreateTable(String databaseDialect, TableMetadata tableMetadata) {
        TABLE_METADATA_MAP.set(tableMetadata.getTableName(), tableMetadata);
    }

}
