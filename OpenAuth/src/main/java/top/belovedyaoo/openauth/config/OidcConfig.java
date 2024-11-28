package top.belovedyaoo.openauth.config;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * OpenAuth Server 端 Oidc 配置类 Model
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class OidcConfig implements Serializable {

    /**
     * iss 值，如不配置则自动计算
     */
    public String iss;

    /**
     * idToken 有效期（单位秒） 默认十分钟
     */
    public long idTokenTimeout = 60 * 10;

    @Override
    public String toString() {
        return "SaOAuth2OidcConfig{" +
                "iss='" + iss + '\'' +
                ", idTokenTimeout=" + idTokenTimeout +
                '}';
    }

}
