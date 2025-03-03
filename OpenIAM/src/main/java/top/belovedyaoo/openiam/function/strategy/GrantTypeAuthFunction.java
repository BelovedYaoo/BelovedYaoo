package top.belovedyaoo.openiam.function.strategy;

import cn.dev33.satoken.context.model.SaRequest;
import top.belovedyaoo.openiam.data.model.AccessTokenModel;

import java.util.function.Function;

/**
 * 函数式接口：GrantType 认证
 *
 * <p>  参数：SaRequest、grant_type </p>
 * <p>  返回：处理结果  </p>
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@FunctionalInterface
public interface GrantTypeAuthFunction extends Function<SaRequest, AccessTokenModel> {

}