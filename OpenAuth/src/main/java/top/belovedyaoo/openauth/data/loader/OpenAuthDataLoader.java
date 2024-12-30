package top.belovedyaoo.openauth.data.loader;

import top.belovedyaoo.openauth.core.OpenAuthManager;
import top.belovedyaoo.openauth.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openauth.enums.OidcExceptionEnum;
import cn.dev33.satoken.secure.SaSecureUtil;
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
     * @return ClientModel
     */
    default OpenAuthClientModel getClientModel(String clientId) {
        // 默认从内存配置中读取数据
        return OpenAuthManager.getServerConfig().getClients().get(clientId);
    }

    /**
     * 根据 id 获取 Client 信息，不允许为 null
     *
     * @param clientId 应用id
     * @return ClientModel
     */
    default OpenAuthClientModel getClientModelNotNull(String clientId) {
        OpenAuthClientModel clientModel = getClientModel(clientId);
        OpenException.throwBy(clientModel == null, OidcExceptionEnum.INVALID_CLIENT_ID, clientId);
        return clientModel;
    }

    /**
     * 根据ClientId 和 LoginId 获取openid
     *
     * @param clientId 应用id
     * @param loginId 账号id
     * @return 此账号在此Client下的openid
     */
    default String getOpenid(String clientId, Object loginId) {
        return SaSecureUtil.md5(OpenAuthManager.getServerConfig().getOpenidDigestPrefix() + "_" + clientId + "_" + loginId);
    }

}
