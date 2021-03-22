package com.micro.lcl.config.shiro.filter;

import com.micro.lcl.common.api.Result;
import com.micro.lcl.common.utils.JsonUtil;
import com.micro.lcl.config.shiro.JWTtoken;
import com.micro.lcl.constant.BaseConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 鉴权登录拦截器
 *
 * @author Administrator
 * @date 2021/2/817:15
 */
@Slf4j
public class JwtFilter extends AuthenticatingFilter {
    private boolean allowOrigin = true;

    public JwtFilter() {
    }

    public JwtFilter(boolean allowOrigin) {
        this.allowOrigin = allowOrigin;
    }

    /**
     * 执行登录认证
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            log.debug("_-_-_-返回结果：executeLogin(request, response)--------------");
            return executeLogin(request, response);
        } catch (Exception e) {
            throw new AuthenticationException("Token失效，请重新登录！");
        }
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Result<?> result;
        RuntimeException throwable;
        //提交realm进行登入，如果错误会抛出异常并捕获
        try {
            AuthenticationToken token = createToken(request, response);
            Subject subject = getSubject(request, response);
            subject.login(token);
            //如果没有异常则代表登入成功，返回true
            return true;
        } catch (AuthenticationException e) {
            throwable = e;
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result = Result.error(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (RuntimeException e) {
            throwable = e;
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result = Result.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        try {
            httpServletResponse.getWriter().write(JsonUtil.OBJECT_MAPPER.writeValueAsString(result));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw throwable;
        }
        return false;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(BaseConstant.X_ACCESS_TOKEN);
        if (StringUtils.isEmpty(token)) {
            throw new UnknownAccountException("token cannot be empty.");
        }
        return new JWTtoken(token);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }

    /**
     * 对跨域提供支持
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (allowOrigin) {
            httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
            httpServletResponse.setHeader("Access-Control-Allow-Headers",httpServletRequest.getHeader("Access-Control-Request-Headers"));
            // 是否允许发送Cookie，默认Cookie不包括在CORS请求之中。设为true时，表示服务器允许Cookie包含在请求中。
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        }
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS)) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
