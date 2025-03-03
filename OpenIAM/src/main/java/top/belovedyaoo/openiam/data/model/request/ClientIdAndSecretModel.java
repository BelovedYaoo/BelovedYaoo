package top.belovedyaoo.openiam.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Client 的 id 和 secret
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClientIdAndSecretModel implements Serializable {

    /**
     * 应用id
     */
    public String clientId;

    /**
     * 应用秘钥
     */
    public String clientSecret;

}
