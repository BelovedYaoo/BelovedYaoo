package top.belovedyaoo.openiam.config;

import lombok.Data;
import lombok.experimental.Accessors;
import top.belovedyaoo.openiam.consts.OpenAuthConst;
import top.belovedyaoo.openiam.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openiam.function.ConfirmFunction;
import top.belovedyaoo.openiam.function.DoLoginFunction;
import top.belovedyaoo.openiam.function.NotLoginFunction;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenAuth Server 端 配置类 Model
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class ServerConfigurer implements Serializable {

    /**
     * 是否打开模式：授权码（Authorization Code）
     */
    public Boolean enableAuthorizationCode = true;

    /**
     * 是否打开模式：隐藏式（Implicit）
     */
    public Boolean enableImplicit = true;

    /**
     * 是否打开模式：密码式（Password）
     */
    public Boolean enablePassword = true;

    /**
     * 是否打开模式：凭证式（Client Credentials）
     */
    public Boolean enableClientCredentials = true;

    /**
     * 是否在每次 Refresh-Token 刷新 Access-Token 时，产生一个新的 Refresh-Token
     */
    public Boolean isNewRefresh = false;

    /**
     * Code授权码 保存的时间(单位：秒) 默认五分钟
     */
    public long codeTimeout = 60 * 5;

    /**
     * Access-Token 保存的时间(单位：秒) 默认两个小时
     */
    public long accessTokenTimeout = 60 * 60 * 2;

    /**
     * Refresh-Token 保存的时间(单位：秒) 默认30 天
     */
    public long refreshTokenTimeout = 60 * 60 * 24 * 30;

    /**
     * Client-Token 保存的时间(单位：秒) 默认两个小时
     */
    public long clientTokenTimeout = 60 * 60 * 2;

    /**
     * Lower-Client-Token 保存的时间(单位：秒) 默认为 -1，代表延续 Client-Token 有效期
     */
    public long lowerClientTokenTimeout = -1;

    /**
     * 默认 openid 生成算法中使用的摘要前缀
     */
    public String openidDigestPrefix = OpenAuthConst.OPENID_DEFAULT_DIGEST_PREFIX;

    /**
     * 指定高级权限，多个用逗号隔开
     */
    public String higherScope;

    /**
     * 指定低级权限，多个用逗号隔开
     */
    public String lowerScope;

    /**
     * 模式4是否返回 AccessToken 字段
     */
    public Boolean mode4ReturnAccessToken = false;

    /**
     * 是否在返回值中隐藏默认的状态字段 (code、msg、data)
     */
    public Boolean hideStatusField = false;

    /**
     * oidc 相关配置
     */
    OidcConfigurer oidc = new OidcConfigurer();

    /**
     * client 列表
     */
    public Map<String, OpenAuthClientModel> clients = new LinkedHashMap<>();


    // -------------------- SaOAuth2Handle 所有回调函数 --------------------

    /**
     * OAuth-Server端：未登录时返回的View
     */
    public NotLoginFunction notLogin = () -> "你需要重写此函数，实现自己的逻辑";

    /**
     * OAuth-Server端：确认授权时返回的View
     */
    public ConfirmFunction confirm = (clientId, scopes) -> "你需要重写此函数，实现自己的逻辑";

    /**
     * OAuth-Server端：登录函数
     */
    public DoLoginFunction doLogin = (username, password) -> "你需要重写此函数，实现自己的逻辑";

    /**
     * 注册 client
     *
     * @return /
     */
    public ServerConfigurer addClient(OpenAuthClientModel client) {
        if (this.clients == null) {
            this.clients = new LinkedHashMap<>();
        }
        this.clients.put(client.getClientId(), client);
        return this;
    }

}
