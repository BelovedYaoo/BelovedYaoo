package top.belovedyaoo.opencore.result;

/**
 * 返回结果类型State接口
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface ResultState extends ResultType {

    /**
     * 获取状态信息
     *
     * @return 状态信息
     */
    boolean state();

}
