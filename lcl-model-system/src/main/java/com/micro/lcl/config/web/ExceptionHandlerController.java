package com.micro.lcl.config.web;

import com.micro.lcl.common.api.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/3/1211:09
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {
    /**
     * 认证异常
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler({UnauthenticatedException.class, AuthenticationException.class})
    public Result<?> authenticationException(Exception e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return Result.error(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler({AuthorizationException.class})
    public Result<?> authorizationException(Exception e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return Result.error(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
    }
}
