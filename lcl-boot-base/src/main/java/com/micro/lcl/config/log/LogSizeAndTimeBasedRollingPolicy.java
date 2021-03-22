package com.micro.lcl.config.log;

import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/3/916:24
 */
public class LogSizeAndTimeBasedRollingPolicy extends SizeAndTimeBasedRollingPolicy {
    @Override
    public void setFileNamePattern(String fnp) {
        if (fnp != null) {
            fileNamePatternStr = HostAddress.addHostAddress(fnp);
        } else {
            fileNamePatternStr = null;
        }

    }
}
