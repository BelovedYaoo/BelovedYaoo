package top.belovedyaoo.openiam.function;

import java.util.List;
import java.util.function.BiFunction;

/**
 * 函数式接口：OAuth-Server端 确认授权函数
 *
 * <p>  参数：无  </p>
 * <p>  返回：授权状态  </p>
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@FunctionalInterface
public interface ConfirmFunction extends BiFunction<String, List<String>, Object> {

}