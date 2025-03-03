package top.belovedyaoo.openiam.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Model: Access-Token
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AccessTokenModel implements Serializable {

    /**
     * Access-Token 值
     */
    public String accessToken;

    /**
     * Refresh-Token 值
     */
    public String refreshToken;

    /**
     * Access-Token 到期时间
     */
    public long expiresTime;

    /**
     * Refresh-Token 到期时间
     */
    public long refreshExpiresTime;

    /**
     * 应用id
     */
    public String clientId;

    /**
     * 账号id
     */
    public Object loginId;

    /**
     * 授权范围
     */
    public List<String> scopes;

    /**
     * Token 类型
     */
    public String tokenType;

    /**
     * 扩展数据
     */
    public Map<String, Object> extraData;

    /**
     * 构建一个
     *
     * @param accessToken accessToken
     * @param clientId    应用id
     * @param scopes      请求授权范围
     * @param loginId     对应的账号id
     */
    public AccessTokenModel(String accessToken, String clientId, Object loginId, List<String> scopes) {
        super();
        this.accessToken = accessToken;
        this.clientId = clientId;
        this.loginId = loginId;
        this.scopes = scopes;
    }

    // 追加只读属性

    /**
     * 获取：此 Access-Token 的剩余有效期（秒）
     *
     * @return /
     */
    public long getExpiresIn() {
        long s = (expiresTime - System.currentTimeMillis()) / 1000;
        return s < 1 ? -2 : s;
    }

    /**
     * 获取：此 Refresh-Token 的剩余有效期（秒）
     *
     * @return /
     */
    public long getRefreshExpiresIn() {
        long s = (refreshExpiresTime - System.currentTimeMillis()) / 1000;
        return s < 1 ? -2 : s;
    }

}
