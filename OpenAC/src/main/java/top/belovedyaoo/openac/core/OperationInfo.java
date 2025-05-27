package top.belovedyaoo.openac.core;

import top.belovedyaoo.openac.model.Operation;

import java.util.LinkedHashMap;

/**
 * 操作接口信息
 *
 * @author Celrx
 * @version 1.0
 */
public class OperationInfo {

    /**
     * 维护在内存中的操作接口信息
     */
    public final LinkedHashMap<String, LinkedHashMap<String, Operation>> operationInfoMap = new LinkedHashMap<>();

    private OperationInfo() {
    }

    private static class Holder {
        static final OperationInfo INSTANCE = new OperationInfo();
    }

    public static OperationInfo getInstance() {
        return Holder.INSTANCE;
    }

}
