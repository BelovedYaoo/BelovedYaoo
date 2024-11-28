package top.belovedyaoo.openauth.data.generate;

import top.belovedyaoo.openauth.core.OpenAuthManager;
import top.belovedyaoo.openauth.consts.OpenAuthConst;
import top.belovedyaoo.openauth.dao.OpenAuthDao;
import top.belovedyaoo.openauth.data.convert.OpenAuthDataConverter;
import top.belovedyaoo.openauth.data.model.AccessTokenModel;
import top.belovedyaoo.openauth.data.model.ClientTokenModel;
import top.belovedyaoo.openauth.data.model.CodeModel;
import top.belovedyaoo.openauth.data.model.RefreshTokenModel;
import top.belovedyaoo.openauth.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openauth.data.model.request.RequestAuthModel;
import top.belovedyaoo.openauth.error.OpenAuthErrorCode;
import top.belovedyaoo.openauth.exception.AuthorizationCodeException;
import top.belovedyaoo.openauth.exception.OpenAuthException;
import top.belovedyaoo.openauth.exception.OpenAuthRefreshTokenException;
import top.belovedyaoo.openauth.strategy.OpenAuthStrategy;
import cn.dev33.satoken.util.SaFoxUtil;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * OpenAuth 数据构建器，默认实现类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class OpenAuthDataGenerateDefaultImpl implements OpenAuthDataGenerate {

    /**
     * 构建Model：Code授权码
     * @param ra 请求参数Model
     * @return 授权码Model
     */
    @Override
    public CodeModel generateCode(RequestAuthModel ra) {

        OpenAuthDao dao = OpenAuthManager.getDao();

        // 删除旧Code
        dao.deleteCode(dao.getCodeValue(ra.clientId, ra.loginId));

        // 生成新Code
        String codeValue = OpenAuthStrategy.INSTANCE.createCodeValue.execute(ra.clientId, ra.loginId, ra.scopes);
        CodeModel cm = new CodeModel(codeValue, ra.clientId, ra.scopes, ra.loginId, ra.redirectUri, ra.getNonce());

        // 保存新Code
        dao.saveCode(cm);
        dao.saveCodeIndex(cm);

        // 保存code-nonce
        dao.saveCodeNonceIndex(cm);

        // 返回
        return cm;
    }

    /**
     * 构建Model：Access-Token
     * @param code 授权码Model
     * @return AccessToken Model
     */
    @Override
    public AccessTokenModel generateAccessToken(String code) {

        OpenAuthDao dao = OpenAuthManager.getDao();
        OpenAuthDataConverter dataConverter = OpenAuthManager.getDataConverter();

        // 1、先校验
        CodeModel cm = dao.getCode(code);
        AuthorizationCodeException.throwBy(cm == null, "无效 code: " + code, code, OpenAuthErrorCode.CODE_30110);

        // 2、删除旧Token
        dao.deleteAccessToken(dao.getAccessTokenValue(cm.clientId, cm.loginId));
        dao.deleteRefreshToken(dao.getRefreshTokenValue(cm.clientId, cm.loginId));

        // 3、生成token
        AccessTokenModel at = dataConverter.convertCodeToAccessToken(cm);
        OpenAuthStrategy.INSTANCE.workAccessTokenByScope.accept(at);
        RefreshTokenModel rt = dataConverter.convertAccessTokenToRefreshToken(at);
        at.refreshToken = rt.refreshToken;
        at.refreshExpiresTime = rt.expiresTime;

        // 4、保存token
        dao.saveAccessToken(at);
        dao.saveAccessTokenIndex(at);
        dao.saveRefreshToken(rt);
        dao.saveRefreshTokenIndex(rt);

        // 5、删除此Code
        dao.deleteCode(code);
        dao.deleteCodeIndex(cm.clientId, cm.loginId);

        // 6、返回 Access-Token
        return at;
    }

    /**
     * 刷新Model：根据 Refresh-Token 生成一个新的 Access-Token
     * @param refreshToken Refresh-Token值
     * @return 新的 Access-Token
     */
    @Override
    public AccessTokenModel refreshAccessToken(String refreshToken) {

        OpenAuthDao dao = OpenAuthManager.getDao();

        // 获取 Refresh-Token 信息
        RefreshTokenModel rt = dao.getRefreshToken(refreshToken);
        OpenAuthRefreshTokenException.throwBy(rt == null, "无效 refresh_token: " + refreshToken, refreshToken, OpenAuthErrorCode.CODE_30111);

        // 如果配置了[每次刷新产生新的Refresh-Token]
        OpenAuthClientModel clientModel = OpenAuthManager.getDataLoader().getClientModelNotNull(rt.clientId);
        if(clientModel.getIsNewRefresh()) {
            // 删除旧 Refresh-Token
            dao.deleteRefreshToken(rt.refreshToken);

            // 创建并保存新的 Refresh-Token
            rt = OpenAuthManager.getDataConverter().convertRefreshTokenToRefreshToken(rt);
            dao.saveRefreshToken(rt);
            dao.saveRefreshTokenIndex(rt);
        }

        // 删除旧 Access-Token
        dao.deleteAccessToken(dao.getAccessTokenValue(rt.clientId, rt.loginId));

        // 生成新 Access-Token
        AccessTokenModel at = OpenAuthManager.getDataConverter().convertRefreshTokenToAccessToken(rt);

        // 保存新 Access-Token
        dao.saveAccessToken(at);
        dao.saveAccessTokenIndex(at);

        // 返回新 Access-Token
        return at;
    }

    /**
     * 构建Model：Access-Token (根据RequestAuthModel构建，用于隐藏式 and 密码式)
     * @param ra 请求参数Model
     * @param isCreateRt 是否生成对应的Refresh-Token
     * @return Access-Token Model
     */
    @Override
    public AccessTokenModel generateAccessToken(RequestAuthModel ra, boolean isCreateRt) {

        OpenAuthDao dao = OpenAuthManager.getDao();

        // 1、删除 旧Token
        dao.deleteAccessToken(dao.getAccessTokenValue(ra.clientId, ra.loginId));
        if(isCreateRt) {
            dao.deleteRefreshToken(dao.getRefreshTokenValue(ra.clientId, ra.loginId));
        }

        // 2、生成 新Access-Token
        String newAtValue = OpenAuthStrategy.INSTANCE.createAccessToken.execute(ra.clientId, ra.loginId, ra.scopes);
        AccessTokenModel at = new AccessTokenModel(newAtValue, ra.clientId, ra.loginId, ra.scopes);
        at.tokenType = OpenAuthConst.TokenType.bearer;

        // 3、根据权限构建额外参数
        at.extraData = new LinkedHashMap<>();
        OpenAuthStrategy.INSTANCE.workAccessTokenByScope.accept(at);

        OpenAuthClientModel clientModel = OpenAuthManager.getDataLoader().getClientModelNotNull(ra.clientId);
        at.expiresTime = System.currentTimeMillis() + (clientModel.getAccessTokenTimeout() * 1000);

        // 3、生成&保存 Refresh-Token
        if(isCreateRt) {
            RefreshTokenModel rt = OpenAuthManager.getDataConverter().convertAccessTokenToRefreshToken(at);
            at.refreshToken = rt.refreshToken;
            at.refreshExpiresTime = rt.expiresTime;

            dao.saveRefreshToken(rt);
            dao.saveRefreshTokenIndex(rt);
        }

        // 5、保存 新Access-Token
        dao.saveAccessToken(at);
        dao.saveAccessTokenIndex(at);

        // 6、返回 新Access-Token
        return at;
    }

    /**
     * 构建Model：Client-Token
     * @param clientId 应用id
     * @param scopes 授权范围
     * @return Client-Token Model
     */
    @Override
    public ClientTokenModel generateClientToken(String clientId, List<String> scopes) {

        OpenAuthDao dao = OpenAuthManager.getDao();

        // 1、删掉旧 Lower-Client-Token
        dao.deleteClientToken(dao.getLowerClientTokenValue(clientId));

        // 2、将旧Client-Token 标记为新 Lower-Client-Token
        ClientTokenModel oldCt = dao.getClientToken(dao.getClientTokenValue(clientId));
        dao.saveLowerClientTokenIndex(oldCt);

        // 2.5、如果配置了 Lower-Client-Token 的 ttl ，则需要更新一下
        OpenAuthClientModel cm = OpenAuthManager.getDataLoader().getClientModelNotNull(clientId);
        if(oldCt != null && cm.getLowerClientTokenTimeout() != -1) {
            oldCt.expiresTime = System.currentTimeMillis() + (cm.getLowerClientTokenTimeout() * 1000);
            dao.saveClientToken(oldCt);
        }

        // 3、生成新 Client-Token
        String clientTokenValue = OpenAuthStrategy.INSTANCE.createClientToken.execute(clientId, scopes);
        ClientTokenModel ct = new ClientTokenModel(clientTokenValue, clientId, scopes);
        ct.tokenType = OpenAuthConst.TokenType.bearer;
        ct.expiresTime = System.currentTimeMillis() + (cm.getClientTokenTimeout() * 1000);
        ct.extraData = new LinkedHashMap<>();
        OpenAuthStrategy.INSTANCE.workClientTokenByScope.accept(ct);

        // 3、保存新Client-Token
        dao.saveClientToken(ct);
        dao.saveClientTokenIndex(ct);

        // 4、返回
        return ct;
    }

    /**
     * 构建URL：下放Code URL (Authorization Code 授权码)
     * @param redirectUri 下放地址
     * @param code code参数
     * @param state state参数
     * @return 构建完毕的URL
     */
    @Override
    public String buildRedirectUri(String redirectUri, String code, String state) {
        String url = SaFoxUtil.joinParam(redirectUri, OpenAuthConst.Param.code, code);
        if( ! SaFoxUtil.isEmpty(state)) {
            checkState(state);
            url = SaFoxUtil.joinParam(url, OpenAuthConst.Param.state, state);
        }
        return url;
    }

    /**
     * 构建URL：下放Access-Token URL （implicit 隐藏式）
     * @param redirectUri 下放地址
     * @param token token
     * @param state state参数
     * @return 构建完毕的URL
     */
    @Override
    public String buildImplicitRedirectUri(String redirectUri, String token, String state) {
        String url = SaFoxUtil.joinSharpParam(redirectUri, OpenAuthConst.Param.token, token);
        if( ! SaFoxUtil.isEmpty(state)) {
            checkState(state);
            url = SaFoxUtil.joinSharpParam(url, OpenAuthConst.Param.state, state);
        }
        return url;
    }

    /**
     * 检查 state 是否被重复使用
     * @param state /
     */
    @Override
    public void checkState(String state) {
        String value = OpenAuthManager.getDao().getState(state);
        if(SaFoxUtil.isNotEmpty(value)) {
            throw new OpenAuthException("多次请求的 state 不可重复: " + state).setCode(OpenAuthErrorCode.CODE_30127);
        }
        OpenAuthManager.getDao().saveState(state);
    }

}

