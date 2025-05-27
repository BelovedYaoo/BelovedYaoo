package top.belovedyaoo.openac.initializer;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dromara.autotable.springboot.properties.AutoTableProperties;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.belovedyaoo.openac.core.OperationInfo;
import top.belovedyaoo.openac.model.Operation;
import top.belovedyaoo.openac.model.Permission;
import top.belovedyaoo.openac.model.mapping.MappingPermissionOperation;
import top.belovedyaoo.opencore.base.BaseFiled;
import top.belovedyaoo.opencore.events.AutoTableFinishEvent;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 权限数据缓存加载器<p>
 * 当系统启动时，且操作接口信息初始化完成后，从持久层读取一份权限数据，加载到内存中<p>
 * 后续读取始终从内存中读取，写入时写入到内存并穿透到持久层
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class AcDataCacheLoader {

    private final AutoTableProperties atp;

    private final BaseMapper<MappingPermissionOperation> mpo;

    // Key 是接口ID
    // Value 是接口所需的所有权限的ID
    @Getter
    private final LinkedHashMap<String, List<String>> mappingOperationPermissionMap = new LinkedHashMap<>();

    @PostConstruct
    public void acDataCacheInit() {
        if (!atp.getEnable()) {
            dataLoad();
        }
    }

    @Async
    @EventListener
    public void onAutoTableFinish(AutoTableFinishEvent atfEvent) {
        dataLoad();
    }

    // 从持久层加载接口所需的权限
    private void dataLoad() {
        OperationInfo operationInfoInstance = OperationInfo.getInstance();
        String permissionAlias = "p";
        String mpoAlias = "mpo";
        operationInfoInstance.operationInfoMap.forEach((_, operationMap) -> {
            operationMap.forEach((_, operation) -> {
                QueryWrapper queryWrapper = QueryWrapper.create()
                        .select(new QueryColumn(permissionAlias, Permission.PERMISSION_CODE))
                        .from(MappingPermissionOperation.class).as(mpoAlias)
                        .leftJoin(Permission.class).as(permissionAlias)
                        .on(new QueryColumn(mpoAlias, Permission.PERMISSION_ID).eq(new QueryColumn(permissionAlias, BaseFiled.BASE_ID)))
                        .where(new QueryColumn(mpoAlias, Operation.OPERATION_ID).eq(operation.baseId()));
                List<String> strings = mpo.selectListByQueryAs(queryWrapper, String.class);
                mappingOperationPermissionMap.put(operation.baseId(), strings);
            });
            /*
             * SELECT `p`.`permission_code`
             * FROM `mapping_permission_operation` AS `mpo`
             * LEFT JOIN `permission` AS `p`
             * ON (`mpo`.`permission_id` = `p`.`base_id`) AND `p`.`deleted_at` IS NULL
             * WHERE (`mpo`.`operation_id` = '0d3eea35578b9b20c9c90be19fc040c8') AND `mpo`.`deleted_at` IS NULL
             */
        });
    }

}
