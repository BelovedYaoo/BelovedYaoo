package top.belovedyaoo.openiam.data.convert;

import top.belovedyaoo.openiam.core.OpenAuthManager;
import top.belovedyaoo.openiam.consts.OpenAuthConst;
import top.belovedyaoo.openiam.data.model.AccessTokenModel;
import top.belovedyaoo.openiam.data.model.CodeModel;
import top.belovedyaoo.openiam.data.model.RefreshTokenModel;
import top.belovedyaoo.openiam.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openiam.strategy.OpenAuthStrategy;
import cn.dev33.satoken.util.SaFoxUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OpenAuth 数据格式转换器，默认实现类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class OpenAuthDataConverterDefaultImpl implements OpenAuthDataConverter {

    /**
     * 转换 scope 数据格式：String -> List
     */
    @Override
    public List<String> convertScopeStringToList(String scopeString) {
        if(SaFoxUtil.isEmpty(scopeString)) {
            return new ArrayList<>();
        }
        // 兼容以下三种分隔符：空格、逗号、%20
        scopeString = scopeString.replaceAll(" ", ",");
        scopeString = scopeString.replaceAll("%20", ",");
        return SaFoxUtil.convertStringToList(scopeString);
    }

    /**
     * 转换 scope 数据格式：List -> String
     */
    @Override
    public String convertScopeListToString(List<String> scopeList) {
        return SaFoxUtil.convertListToString(scopeList);
    }

    /**
     * 转换 redirect_uri 数据格式：String -> List
     */
    @Override
    public List<String> convertRedirectUriStringToList(String redirectUris) {
        if(SaFoxUtil.isEmpty(redirectUris)) {
            return new ArrayList<>();
        }
        return SaFoxUtil.convertStringToList(redirectUris);
    }

    /**
     * 将 Code 转换为 Access-Token
     */
    @Override
    public AccessTokenModel convertCodeToAccessToken(CodeModel cm) {
        AccessTokenModel at = new AccessTokenModel();
        at.accessToken = OpenAuthStrategy.INSTANCE.createAccessToken.execute(cm.clientId, cm.loginId, cm.scopes);
        at.clientId = cm.clientId;
        at.loginId = cm.loginId;
        at.scopes = cm.scopes;
        at.tokenType = OpenAuthConst.TokenType.bearer;
        OpenAuthClientModel clientModel = OpenAuthManager.getDataLoader().getClientModelNotNull(cm.clientId);
        at.expiresTime = System.currentTimeMillis() + (clientModel.getAccessTokenTimeout() * 1000);
        at.extraData = new LinkedHashMap<>();
        return at;
    }

    /**
     * 将 Access-Token 转换为 Refresh-Token
     * @param at .
     * @return .
     */
    @Override
    public RefreshTokenModel convertAccessTokenToRefreshToken(AccessTokenModel at) {
        RefreshTokenModel rt = new RefreshTokenModel();
        rt.refreshToken = OpenAuthStrategy.INSTANCE.createRefreshToken.execute(at.clientId, at.loginId, at.scopes);
        rt.clientId = at.clientId;
        rt.loginId = at.loginId;
        rt.scopes = at.scopes;
        OpenAuthClientModel clientModel = OpenAuthManager.getDataLoader().getClientModelNotNull(at.clientId);
        rt.expiresTime = System.currentTimeMillis() + (clientModel.getRefreshTokenTimeout() * 1000);
        rt.extraData = new LinkedHashMap<>(at.extraData);
        // 改变 at 属性
//        at.refreshToken = rt.refreshToken;
//        at.refreshExpiresTime = rt.expiresTime;
        return rt;
    }

    /**
     * 将 Refresh-Token 转换为 Access-Token
     * @param rt .
     * @return .
     */
    @Override
    public AccessTokenModel convertRefreshTokenToAccessToken(RefreshTokenModel rt) {
        AccessTokenModel at = new AccessTokenModel();
        at.accessToken = OpenAuthStrategy.INSTANCE.createAccessToken.execute(rt.clientId, rt.loginId, rt.scopes);
        at.refreshToken = rt.refreshToken;
        at.clientId = rt.clientId;
        at.loginId = rt.loginId;
        at.scopes = rt.scopes;
        at.tokenType = OpenAuthConst.TokenType.bearer;
        at.extraData = new LinkedHashMap<>(rt.extraData);
        OpenAuthClientModel clientModel = OpenAuthManager.getDataLoader().getClientModelNotNull(rt.clientId);
        at.expiresTime = System.currentTimeMillis() + (clientModel.getAccessTokenTimeout() * 1000);
        at.refreshExpiresTime = rt.expiresTime;
        return at;
    }

    /**
     * 根据 Refresh-Token 创建一个新的 Refresh-Token
     * @param rt .
     * @return .
     */
    @Override
    public RefreshTokenModel convertRefreshTokenToRefreshToken(RefreshTokenModel rt) {
        RefreshTokenModel newRt = new RefreshTokenModel();
        newRt.refreshToken = OpenAuthStrategy.INSTANCE.createRefreshToken.execute(rt.clientId, rt.loginId, rt.scopes);
        OpenAuthClientModel clientModel = OpenAuthManager.getDataLoader().getClientModelNotNull(rt.clientId);
        newRt.expiresTime = System.currentTimeMillis() + (clientModel.getRefreshTokenTimeout() * 1000);
        newRt.clientId = rt.clientId;
        newRt.scopes = rt.scopes;
        newRt.loginId = rt.loginId;
        newRt.extraData = new LinkedHashMap<>(rt.extraData);
        return newRt;
    }

}

