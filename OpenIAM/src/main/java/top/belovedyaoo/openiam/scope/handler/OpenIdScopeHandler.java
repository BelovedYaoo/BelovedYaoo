package top.belovedyaoo.openiam.scope.handler;

import top.belovedyaoo.openiam.core.OpenAuthManager;
import top.belovedyaoo.openiam.consts.OpenAuthConst;
import top.belovedyaoo.openiam.data.model.AccessTokenModel;
import top.belovedyaoo.openiam.data.model.ClientTokenModel;
import top.belovedyaoo.openiam.scope.CommonScope;

/**
 * OpenId 权限处理器：在 AccessToken 扩展参数中追加 openid 字段
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class OpenIdScopeHandler implements OpenAuthScopeHandlerInterface {

    @Override
    public String getHandlerScope() {
        return CommonScope.OPENID;
    }

    @Override
    public void workAccessToken(AccessTokenModel at) {
        at.extraData.put(OpenAuthConst.ExtraField.openid, OpenAuthManager.getDataLoader().getOpenid(at.clientId, at.loginId));
    }

    @Override
    public void workClientToken(ClientTokenModel ct) {

    }

}