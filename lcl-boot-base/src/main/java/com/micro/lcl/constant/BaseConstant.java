package com.micro.lcl.constant;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/116:37
 */
public class BaseConstant {
    public static final String X_ACCESS_TOKEN = "X-Access-Token";
    public static final Integer SC_OK_200 = 200;
    public static final Integer SC_ERROR_500 = 500;
    public static final Integer SC_NO_AUTHZ = 510;

    public static final String X_GATEWAY_BASE_PATH = "X_GATEWAY_BASE_PATH";
    public static final String REDIS_USER_TOKEN = "redis-user-token-";
    public static final String CLOUD_SERVER_KEY = "spring.cloud.nacos.discovery.server-addr";
    /** 登录用户Shiro权限缓存KEY前缀 */
    public static final String PREFIX_USER_SHIRO_CACHE  = "shiro:cache:com.micro.lcl.config.shiro.ShiroRealm.authorizationCache:";
}
