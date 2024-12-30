package top.belovedyaoo.openauth.data.model.request;

import cn.dev33.satoken.util.SaFoxUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import top.belovedyaoo.openauth.enums.OidcExceptionEnum;
import top.belovedyaoo.opencore.exception.OpenException;

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
        // clientId 不可为空
        OpenException.throwBy(SaFoxUtil.isEmpty(clientId), OidcExceptionEnum.MISSING_CLIENT_ID);
        // scope 不可为空
        OpenException.throwBy(SaFoxUtil.isEmpty(scopes), OidcExceptionEnum.MISSING_SCOPE);
        // redirectUri 不可为空
        OpenException.throwBy(SaFoxUtil.isEmpty(redirectUri), OidcExceptionEnum.MISSING_REDIRECT_URI);
        // loginId 不可为空
        OpenException.throwBy(SaFoxUtil.isEmpty(String.valueOf(loginId)), OidcExceptionEnum.MISSING_LOGIN_ID);
        return this;
    }

}
