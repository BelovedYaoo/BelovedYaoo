package top.belovedyaoo.logs.toolkit;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Servlet 工具类
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class ServletUtil {

    public static String getClientIp(HttpServletRequest request, String... otherHeaderNames) {
        String[] headers = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
            headers = ArrayUtil.addAll(headers, otherHeaderNames);
        }

        return getClientIpByHeader(request, headers);
    }

    public static String getClientIpByHeader(HttpServletRequest request, String... headerNames) {
        for(String header : headerNames) {
            String ip = request.getHeader(header);
            if (!NetUtil.isUnknown(ip)) {
                return NetUtil.getMultistageReverseProxyIp(ip);
            }
        }

        String ip = request.getRemoteAddr();
        return NetUtil.getMultistageReverseProxyIp(ip);
    }

}
