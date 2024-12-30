package top.belovedyaoo.opencore.exception;

/**
 * 异常信息类型接口
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface ExceptionType {

    /**
     * 获取异常码
     *
     * @return 异常码
     */
    Integer code();

    /**
     * 获取异常消息
     *
     * @return 异常消息
     */
    String message();

    /**
     * 获取异常描述
     *
     * @return 异常描述
     */
    String description();

}
