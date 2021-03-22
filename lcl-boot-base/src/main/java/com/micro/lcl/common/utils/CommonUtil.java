package com.micro.lcl.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    /**
     * 获取类的所有属性，包括父类
     * @param parameter
     * @return
     */
    public static Field[] getAllFields(Object parameter) {
        Class<?> clazz = parameter.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
