package top.belovedyaoo.opencore.result;

/**
 * 返回结果类型Message接口
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public interface ResultMessage extends ResultType {

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    String message();

}
