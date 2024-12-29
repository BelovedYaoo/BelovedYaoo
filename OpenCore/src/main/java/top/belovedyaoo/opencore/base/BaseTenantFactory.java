package top.belovedyaoo.opencore.base;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.tenant.TenantFactory;

/**
 * 多租户工厂
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class BaseTenantFactory implements TenantFactory {

    @Override
    public Object[] getTenantIds() {
        return new Object[]{StpUtil.getLoginIdDefaultNull()};
    }

    @Override
    public Object[] getTenantIds(String tableName) {
        return this.getTenantIds();
    }

}
