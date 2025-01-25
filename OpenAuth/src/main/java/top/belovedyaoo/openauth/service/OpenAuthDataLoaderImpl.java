package top.belovedyaoo.openauth.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.ejlchina.okhttps.OkHttps;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import top.belovedyaoo.openac.model.BaseUser;
import top.belovedyaoo.openauth.data.loader.OpenAuthDataLoader;
import top.belovedyaoo.openauth.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openauth.enums.OpenAuthResultEnum;
import top.belovedyaoo.openauth.function.ConfirmFunction;
import top.belovedyaoo.openauth.function.DoLoginFunction;
import top.belovedyaoo.openauth.function.NotLoginFunction;
import top.belovedyaoo.opencore.common.OcMap;
import top.belovedyaoo.opencore.result.Result;
import top.belovedyaoo.opencore.security.SecurityConfig;

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

    /**
     * 根据 clientId 获取 Client 信息
     *
     * @param clientId 应用id
     *
     * @return Client信息
     */
    @Override
    public OpenAuthClientModel getClientModel(String clientId) {
        String str = OkHttps.sync("http://openiam.top:8090/openAuth/getClientModel")
                .addBodyPara("client_id", clientId)
                .post()
                .getBody()
                .toString();
        OcMap so = OcMap.build(str);
        if (so.getInt("code") != 200) {
            new OpenAuthClientModel();
        }
        OcMap clientModel = so.getMap("data");
        System.out.println(clientModel.getString("clientId"));
        System.out.println(clientModel.get("allowGrantTypes"));
        return new OpenAuthClientModel()
                .setClientId(clientModel.getString("clientId"))
                .setClientSecret(clientModel.getString("clientSecret"))
                .setContractScopes(clientModel.getList("contractScopes", String.class))
                .setAllowRedirectUris(clientModel.getList("allowRedirectUris", String.class))
                .setAllowGrantTypes(clientModel.getList("allowGrantTypes", String.class));
        // .setIsNewRefresh(clientModel.getBoolean("isNewRefresh"))
        // .setAccessTokenTimeout(clientModel.getLong("accessTokenTimeout"))
        // .setRefreshTokenTimeout(clientModel.getLong("refreshTokenTimeout"))
        // .setClientTokenTimeout(clientModel.getLong("clientTokenTimeout"))
        // .setLowerClientTokenTimeout(clientModel.getLong("lowerClientTokenTimeout"));
    }

    /**
     * 根据 clientId 和 loginId 获取 openid
     *
     * @param clientId 应用id
     * @param loginId  账号id
     *
     * @return openid
     */
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
            OcMap so = OcMap.build(str);
            if (so.getInt("code") != 200) {
                return Result.failed().message("登录失败").description(so.getString("message"));
            }
            String decData;
            try {
                decData = StrUtil.str(rsa.decrypt(Hex.decodeHex(so.getString("data")), KeyType.PublicKey), CharsetUtil.CHARSET_UTF_8);
            } catch (DecoderException e) {
                throw new RuntimeException(e);
            }
            // BaseUser user;
            // try {
            //     user = new ObjectMapper().readValue(decData, BaseUser.class);
            // } catch (JsonProcessingException e) {
            //     user = new BaseUser();
            // }
            // System.out.println(user);
            // System.out.println(user.openId());
            OcMap user = OcMap.build(decData);
            System.out.println(user.getString("openId"));
            OcMap userData = OcMap.build()
                    .set("baseId", user.getString("baseId"))
                    .set("openId", user.getString("openId"))
                    .set("phone", user.getString("phone"))
                    .set("email", user.getString("email"))
                    .set("nickname", user.getString("nickname"));
            BaseUser user2 = user.getModel(BaseUser.class);
            // Sa-Token登录
            StpUtil.login(user2.baseId());
            return Result.success().message("登录成功")
                    .description("欢迎您，" + user2.nickname())
                    .data(OcMap.build(user))
                    .data("tokenValue", StpUtil.getTokenValue());
        };
    }

    public ConfirmFunction confirm() {
        return (clientId, scopes) -> Result.success().resultType(OpenAuthResultEnum.NEED_CONFIRM).data("client_id", clientId).data("scope", scopes);
    }

}
