package com.micro.lcl.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/417:11
 */
public class JWTtoken implements AuthenticationToken {
    public static final Long serialVersionUID = 1L;

    private final String token;

    public JWTtoken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
