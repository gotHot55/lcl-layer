package com.micro.lcl.config.shiro;

import com.micro.lcl.common.api.model.model.LoginUserModel;
import com.micro.lcl.common.service.ApiService;
import com.micro.lcl.common.utils.CommonUtil;
import com.micro.lcl.common.utils.JwtUtil;
import com.micro.lcl.common.utils.RedisUtil;
import com.micro.lcl.common.utils.SpringContextUtil;
import com.micro.lcl.constant.BaseConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/417:06
 */
@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {
    @Resource
    @Lazy
    private ApiService apiService;

    @Resource
    @Lazy
    private RedisUtil redisUtil;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTtoken;
    }

    /**
     * 权限信息认证(包括角色以及权限)是用户访问controller的时候才进行验证(redis存储的此处权限信息)
     * 触发检测用户权限时才会调用此方法，例如checkRole,checkPermission
     *
     * @param principals 身份信息
     * @return 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("========================Shiro权限认证开始==================");
        String username = null;
        if (principals != null) {
            LoginUserModel user = (LoginUserModel) principals.getPrimaryPrincipal();
            username = user.getUsername();
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roleSet = apiService.getRoleByUserName(username);
        info.addStringPermissions(roleSet);
        log.info("===============Shiro权限认证成功==拥有权限：{}============",roleSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        log.debug("===============Shiro身份认证开始============doGetAuthenticationInfo==========");
        String token = (String) auth.getCredentials();
        if (null == token) {
            log.info("————————身份认证失败——————————IP地址:  "+ CommonUtil.getIpAddrByRequest(SpringContextUtil.getHttpServletRequest()));
            throw new AuthenticationException("token为空!");
        }
        LoginUserModel loginUserModel = this.checkUserTokenIsEffect(token);
        return new SimpleAuthenticationInfo(loginUserModel, token, getName());
    }

    /**
     * 校验token有效性
     * @param token token
     * @return
     */
    private LoginUserModel checkUserTokenIsEffect(String token) {
        //解密获取username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("Token非法无效");
        }
        //查询用户信息
        log.debug("———校验token是否有效————checkUserTokenIsEffect——————— "+ token);
        LoginUserModel loginUser = apiService.getUserByName(username);
        if (loginUser == null) {
            throw new AuthenticationException("用户不存在！");
        }
        //判断用户状态
        if (loginUser.getStatus() != 1) {
            throw new AuthenticationException("账号已被锁定，请联系管理员");
        }
        //校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefer(token, username, loginUser.getPassword())) {
            throw new AuthenticationException("Token失效，请重新登录！");
        }
        return loginUser;
    }

    private boolean jwtTokenRefer(String token, String username, String password) {
        String cacheToken = String.valueOf(redisUtil.get(BaseConstant.REDIS_USER_TOKEN + token));
        if (CommonUtil.isNotEmpty(cacheToken)) {
            //校验token有效性
            if (!JwtUtil.verify(cacheToken, username, password)) {
                String newAuthorization = JwtUtil.sign(username, password);
                //设置超时时间
                redisUtil.set(BaseConstant.REDIS_USER_TOKEN + token, newAuthorization);
                redisUtil.expire(BaseConstant.REDIS_USER_TOKEN, JwtUtil.EXPIRE_TIME * 2 / 1000);
                log.info("——————————用户在线操作，更新token保证不掉线—————————jwtTokenRefresh——————— "+ token);
            }
            return true;
        }
        return false;
    }

}
