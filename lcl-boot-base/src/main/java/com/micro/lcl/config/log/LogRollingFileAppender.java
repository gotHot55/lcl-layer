package com.micro.lcl.config.log;

import ch.qos.logback.core.rolling.RollingFileAppender;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/3/916:19
 */
public class LogRollingFileAppender extends RollingFileAppender {
    @Override
    public void setFile(String file) {
        if (file == null) {
            fileName = null;
        }else {
            fileName = HostAddress.addHostAddress(file.trim());
        }
    }
}
