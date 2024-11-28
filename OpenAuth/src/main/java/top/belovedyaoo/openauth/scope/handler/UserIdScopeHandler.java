package top.belovedyaoo.openauth.scope.handler;

import top.belovedyaoo.openauth.consts.OpenAuthConst;
import top.belovedyaoo.openauth.data.model.AccessTokenModel;
import top.belovedyaoo.openauth.data.model.ClientTokenModel;
import top.belovedyaoo.openauth.scope.CommonScope;

/**
 * UserId 权限处理器：在 AccessToken 扩展参数中追加 userid 字段
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class UserIdScopeHandler implements OpenAuthScopeHandlerInterface {

    @Override
    public String getHandlerScope() {
        return CommonScope.USERID;
    }

    @Override
    public void workAccessToken(AccessTokenModel at) {
        at.extraData.put(OpenAuthConst.ExtraField.userid, at.loginId);
    }

    @Override
    public void workClientToken(ClientTokenModel ct) {

    }

}