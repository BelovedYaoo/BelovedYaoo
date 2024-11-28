package top.belovedyaoo.openauth.data.convert;

import top.belovedyaoo.openauth.data.model.AccessTokenModel;
import top.belovedyaoo.openauth.data.model.CodeModel;
import top.belovedyaoo.openauth.data.model.RefreshTokenModel;

import java.util.List;

/**
 * OpenAuth 数据格式转换器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface OpenAuthDataConverter {

    /**
     * 转换 scope 数据格式：String -> List
     * @param scopeString /
     * @return /
     */
    List<String> convertScopeStringToList(String scopeString);

    /**
     * 转换 scope 数据格式：List -> String
     * @param scopeList /
     * @return /
     */
    String convertScopeListToString(List<String> scopeList);

    /**
     * 转换 redirect_uri 数据格式：String -> List
     * @param redirectUris /
     * @return /
     */
    List<String> convertRedirectUriStringToList(String redirectUris);

    /**
     * 将 Code 转换为 Access-Token
     * @param cm CodeModel对象
     * @return AccessToken对象
     */
    AccessTokenModel convertCodeToAccessToken(CodeModel cm);

    /**
     * 将 Access-Token 转换为 Refresh-Token
     * @param at /
     * @return /
     */
    RefreshTokenModel convertAccessTokenToRefreshToken(AccessTokenModel at);

    /**
     * 将 Refresh-Token 转换为 Access-Token
     * @param rt /
     * @return /
     */
    AccessTokenModel convertRefreshTokenToAccessToken(RefreshTokenModel rt);

    /**
     * 根据 Refresh-Token 创建一个新的 Refresh-Token
     * @param rt /
     * @return /
     */
    RefreshTokenModel convertRefreshTokenToRefreshToken(RefreshTokenModel rt);

}
