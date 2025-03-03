package top.belovedyaoo.openiam.data.loader;

import cn.dev33.satoken.secure.SaSecureUtil;
import top.belovedyaoo.openiam.core.OpenAuthManager;
import top.belovedyaoo.openiam.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openiam.enums.OidcExceptionEnum;
import top.belovedyaoo.opencore.exception.OpenException;

/**
 * OpenAuth 数据加载器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface OpenAuthDataLoader {

    /**
     * 根据 id 获取 Client 信息
     *
     * @param clientId 应用id
     *
     * @return ClientModel
     */
    default OpenAuthClientModel getClientModel(String clientId) {
        // 默认从内存配置中读取数据
        return OpenAuthManager.getServerConfig().getClients().get(clientId);
    }

    /**
     * 根据 ID 获取 Client 信息，不允许为 null
     *
     * @param clientId 应用ID
     *
     * @return ClientModel
     */
    default OpenAuthClientModel getClientModelNotNull(String clientId) {
        OpenAuthClientModel clientModel = getClientModel(clientId);
        OpenException.throwBy(clientModel == null, OidcExceptionEnum.INVALID_CLIENT_ID, clientId);
        return clientModel;
    }

    /**
     * 根据ClientId 和 LoginId 获取openID
     *
     * @param clientId 应用ID
     * @param loginId  账号ID
     *
     * @return 此账号在此Client下的openID
     */
    default String getOpenid(String clientId, Object loginId) {
        return SaSecureUtil.md5(OpenAuthManager.getServerConfig().getOpenidDigestPrefix() + "_" + clientId + "_" + loginId);
    }

    /**
     * 判断此账号是否可以访问此Client
     *
     * @param userId   用户ID
     * @param clientId 应用ID
     *
     * @return 是否可以访问此Client
     */
    default boolean canAccess(String userId, String clientId) {
        return true;
    }

}
