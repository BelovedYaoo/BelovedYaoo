package top.belovedyaoo.openauth.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Model: Client-Token
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ClientTokenModel implements Serializable {

    /**
     * Client-Token 值
     */
    public String clientToken;

    /**
     * Client-Token 到期时间
     */
    public long expiresTime;

    /**
     * 应用id
     */
    public String clientId;

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
     * @param clientToken clientToken
     * @param clientId    应用id
     * @param scopes      请求授权范围
     */
    public ClientTokenModel(String clientToken, String clientId, List<String> scopes) {
        super();
        this.clientToken = clientToken;
        this.clientId = clientId;
        this.scopes = scopes;
    }

    /**
     * 获取：此 Client-Token 的剩余有效期（秒）
     *
     * @return /
     */
    public long getExpiresIn() {
        long s = (expiresTime - System.currentTimeMillis()) / 1000;
        return s < 1 ? -2 : s;
    }

}
