package com.micro.lcl.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * sql注入处理工具类
 *
 * @author Administrator
 * @date 2021/3/117:54
 */
@Slf4j
public class SqlInjectionUtil {
    private final static String XSS_STR = "'|and |exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|or |+|,";

    /**
     * sql注入过滤器，遇到注入关键字抛出异常
     * @param values
     */
    public static void filterContent(String[] values) {
        String[] xssArr = XSS_STR.split("\\|");
        for (String value : values) {
            if (value == null || "".equals(value)) {
                continue;
            }
            value = value.toLowerCase();
            for (String arr : xssArr) {
                if (value.contains(arr)) {
                    log.error("该值{}可能存在sql注入风险",value);
                    throw new RuntimeException("请注意，该值存在sql注入风险--->" + value);
                }
            }
        }
    }
}
