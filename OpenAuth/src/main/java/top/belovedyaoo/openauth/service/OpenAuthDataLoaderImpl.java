package top.belovedyaoo.openauth.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.ejlchina.okhttps.OkHttps;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import top.belovedyaoo.agcore.common.SoMap;
import top.belovedyaoo.agcore.result.Result;
import top.belovedyaoo.agcore.security.SecurityConfig;
import top.belovedyaoo.openauth.data.loader.OpenAuthDataLoader;
import top.belovedyaoo.openauth.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openauth.enums.OpenAuthResultEnum;
import top.belovedyaoo.openauth.function.ConfirmFunction;
import top.belovedyaoo.openauth.function.DoLoginFunction;
import top.belovedyaoo.openauth.function.NotLoginFunction;

/**
 * 自定义数据加载器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class OpenAuthDataLoaderImpl implements OpenAuthDataLoader {

    private final SecurityConfig securityConfig;

    // 根据 clientId 获取 Client 信息
    @Override
    public OpenAuthClientModel getClientModel(String clientId) {
        String str = OkHttps.sync("http://openiam.top:8090/openAuth/getClientModel")
                .addBodyPara("client_id", clientId)
                .post()
                .getBody()
                .toString();
        SoMap so = SoMap.getSoMap().setJsonString(str);
        if (so.getInt("code") != 200) {
            new OpenAuthClientModel();
        }
        SoMap clientModel = so.getMap("data");
        System.out.println(clientModel.getString("clientId"));
        System.out.println(clientModel.get("allowGrantTypes"));
        return new OpenAuthClientModel()
                .setClientId(clientModel.getString("clientId"))
                .setClientSecret(clientModel.getString("clientSecret"))
                .setContractScopes(clientModel.getList("contractScopes", String.class))
                .setAllowRedirectUris(clientModel.getList("allowRedirectUris", String.class))
                .setAllowGrantTypes(clientModel.getList("allowGrantTypes", String.class))
                .setIsNewRefresh(clientModel.getBoolean("isNewRefresh"))
                .setAccessTokenTimeout(clientModel.getLong("accessTokenTimeout"))
                .setRefreshTokenTimeout(clientModel.getLong("refreshTokenTimeout"))
                .setClientTokenTimeout(clientModel.getLong("clientTokenTimeout"))
                .setLowerClientTokenTimeout(clientModel.getLong("lowerClientTokenTimeout"));
    }

    // 根据 clientId 和 loginId 获取 openid
    @Override
    public String getOpenid(String clientId, Object loginId) {
        // 此处使用框架默认算法生成 openid
        return OpenAuthDataLoader.super.getOpenid(clientId, loginId);
    }

    public NotLoginFunction notLogin() {
        return () -> Result.failed().resultType(OpenAuthResultEnum.NEED_LOGIN);
    }

    public DoLoginFunction doLogin() {
        return (username, password) -> {
            RSA rsa = new RSA(securityConfig.getPrivateKey(), securityConfig.getPublicKey());
            String str = OkHttps.sync("http://openiam.top:8090/openAuth/getUser")
                    .addBodyPara("open_id", Hex.encodeHexString(rsa.encrypt(username, KeyType.PrivateKey)))
                    .addBodyPara("password", Hex.encodeHexString(rsa.encrypt(password, KeyType.PrivateKey)))
                    .post()
                    .getBody()
                    .toString();

            // 转为Json
            SoMap so = SoMap.getSoMap().setJsonString(str);
            if (so.getInt("code") != 200) {
                return Result.failed().message(so.getString("message")).description(so.getString("description"));
            }
            String decData;
            try {
                decData = StrUtil.str(rsa.decrypt(Hex.decodeHex(so.getString("data")), KeyType.PublicKey), CharsetUtil.CHARSET_UTF_8);
            } catch (DecoderException e) {
                throw new RuntimeException(e);
            }
            SoMap user = SoMap.getSoMap().setJsonString(decData).getMap("user");
            System.out.println(user.getString("openId"));
            SoMap userData = SoMap.getSoMap()
                    .set("baseId", user.getString("baseId"))
                    .set("openId", user.getString("openId"))
                    .set("phone", user.getString("phone"))
                    .set("email", user.getString("email"))
                    .set("nickname", user.getString("nickname"));
            // Sa-Token登录
            StpUtil.login(userData.getString("baseId"));
            return Result.success().description("登录成功")
                    .data(userData)
                    .data("tokenValue", StpUtil.getTokenValue());
        };
    }

    public ConfirmFunction confirm() {
        return (clientId, scopes) -> Result.success().resultType(OpenAuthResultEnum.NEED_CONFIRM).data("client_id", clientId).data("scope", scopes);
    }

}
