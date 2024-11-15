package top.belovedyaoo.openiam.oauth2.data.model.request;

import cn.dev33.satoken.util.SaFoxUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import top.belovedyaoo.openiam.oauth2.error.OpenAuthErrorCode;
import top.belovedyaoo.openiam.oauth2.exception.OpenAuthException;

import java.io.Serializable;
import java.util.List;

/**
 * 请求授权参数的 Model
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class RequestAuthModel implements Serializable {

    /**
     * 应用id
     */
    public String clientId;

    /**
     * 授权范围
     */
    public List<String> scopes;

    /**
     * 对应的账号id
     */
    public Object loginId;

    /**
     * 待重定向URL
     */
    public String redirectUri;

    /**
     * 授权类型, 非必填
     */
    public String responseType;

    /**
     * 状态标识, 可为null
     */
    public String state;

    /**
     * 随机数
     */
    public String nonce;

    /**
     * 检查此Model参数是否有效
     *
     * @return 对象自身
     */
    public RequestAuthModel checkModel() {
        if (SaFoxUtil.isEmpty(clientId)) {
            throw new OpenAuthException("client_id 不可为空").setCode(OpenAuthErrorCode.CODE_30101);
        }
        if (SaFoxUtil.isEmpty(scopes)) {
            throw new OpenAuthException("scope 不可为空").setCode(OpenAuthErrorCode.CODE_30102);
        }
        if (SaFoxUtil.isEmpty(redirectUri)) {
            throw new OpenAuthException("redirect_uri 不可为空").setCode(OpenAuthErrorCode.CODE_30103);
        }
        if (SaFoxUtil.isEmpty(String.valueOf(loginId))) {
            throw new OpenAuthException("LoginId 不可为空").setCode(OpenAuthErrorCode.CODE_30104);
        }
        return this;
    }

}
