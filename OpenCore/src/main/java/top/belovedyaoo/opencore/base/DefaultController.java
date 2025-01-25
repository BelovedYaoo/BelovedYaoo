package top.belovedyaoo.opencore.base;

/**
 * 默认Controller实现<p>
 * 包含了默认的增删改查排序操作
 *
 * @param <T> 实体类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class DefaultController<T extends BaseFiled> extends BaseController<T> implements BaseQuery<T>, BaseInsert<T>, BaseUpdate<T>, BaseDelete<T>, Order<T> {

}
