package top.belovedyaoo.openiam.oauth2.data.loader;

import top.belovedyaoo.openiam.oauth2.OpenAuthManager;
import top.belovedyaoo.openiam.oauth2.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openiam.oauth2.error.OpenAuthErrorCode;
import top.belovedyaoo.openiam.oauth2.exception.OpenAuthClientModelException;
import cn.dev33.satoken.secure.SaSecureUtil;

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
        if(clientModel == null) {
            throw new OpenAuthClientModelException("无效 client_id: " + clientId)
                    .setClientId(clientId)
                    .setCode(OpenAuthErrorCode.CODE_30105);
        }
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
