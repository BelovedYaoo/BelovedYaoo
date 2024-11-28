package top.belovedyaoo.openauth.data.generate;

import top.belovedyaoo.openauth.data.model.AccessTokenModel;
import top.belovedyaoo.openauth.data.model.ClientTokenModel;
import top.belovedyaoo.openauth.data.model.CodeModel;
import top.belovedyaoo.openauth.data.model.request.RequestAuthModel;

import java.util.List;

/**
 * OpenAuth 数据构建器，负责相关 Model 数据构建
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface OpenAuthDataGenerate {

    /**
     * 构建Model：Code授权码
     * @param ra 请求参数Model
     * @return 授权码Model
     */
    CodeModel generateCode(RequestAuthModel ra);

    /**
     * 构建Model：Access-Token
     * @param code 授权码Model
     * @return AccessToken Model
     */
    AccessTokenModel generateAccessToken(String code);

    /**
     * 刷新Model：根据 Refresh-Token 生成一个新的 Access-Token
     * @param refreshToken Refresh-Token值
     * @return 新的 Access-Token
     */
    AccessTokenModel refreshAccessToken(String refreshToken);

    /**
     * 构建Model：Access-Token (根据RequestAuthModel构建，用于隐藏式 and 密码式)
     * @param ra 请求参数Model
     * @param isCreateRt 是否生成对应的Refresh-Token
     * @return Access-Token Model
     */
    AccessTokenModel generateAccessToken(RequestAuthModel ra, boolean isCreateRt);

    /**
     * 构建Model：Client-Token
     * @param clientId 应用id
     * @param scopes 授权范围
     * @return Client-Token Model
     */
    ClientTokenModel generateClientToken(String clientId, List<String> scopes);

    /**
     * 构建URL：下放Code URL (Authorization Code 授权码)
     * @param redirectUri 下放地址
     * @param code code参数
     * @param state state参数
     * @return 构建完毕的URL
     */
    String buildRedirectUri(String redirectUri, String code, String state);

    /**
     * 构建URL：下放Access-Token URL （implicit 隐藏式）
     * @param redirectUri 下放地址
     * @param token token
     * @param state state参数
     * @return 构建完毕的URL
     */
    String buildImplicitRedirectUri(String redirectUri, String token, String state);

    /**
     * 检查 state 是否被重复使用
     * @param state /
     */
    void checkState(String state);

}
