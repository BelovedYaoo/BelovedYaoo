package top.belovedyaoo.opencore.ac;

import java.util.function.Function;

/**
 * 函数式接口：OpenAC 权限控制器函数
 *
 * <p>  参数：鉴权实体  </p>
 * <p>  返回：是否有权限  </p>
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@FunctionalInterface
public interface AcHandlerFunction<T> extends Function<T, Boolean> {

}