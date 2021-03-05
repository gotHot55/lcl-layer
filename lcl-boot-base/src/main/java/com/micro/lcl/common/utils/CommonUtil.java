package com.micro.lcl.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/814:43
 */
public class CommonUtil {
    /**
     * IP
     * @param request
     * @return
     */
    public static String getIpAddrByRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static boolean isNotEmpty(Object obj) {
        if (obj != null && !obj.equals("") && !obj.equals("null")) {
            return true;
        }
        return (false);
    }

}
