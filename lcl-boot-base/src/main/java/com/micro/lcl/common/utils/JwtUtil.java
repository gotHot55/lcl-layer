package com.micro.lcl.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.micro.lcl.common.api.model.model.LoginUserModel;
import com.micro.lcl.constant.BaseConstant;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/815:18
 */
public class JwtUtil {

    /**
     *Token过期时间30分钟（用户登录过期时间是此时间的两倍，
     * 以token在reids缓存时间为准）
     */
    public static final long EXPIRE_TIME = 30 * 60 * 1000;

    /**
     * 获取token中的信息
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 根据request中的token获取用户账号信息
     * @param request
     * @return
     */
    public static String getUserNameByReq(HttpServletRequest request) {
        String accessToken = request.getHeader(BaseConstant.X_ACCESS_TOKEN);
        String username = getUsername(accessToken);
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("未获取到用户信息");
        }
        return username;
    }

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param username  用户名
     * @param password  密码
     * @return
     */
    public static boolean verify(String token, String username, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            //校验token
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成签名 5min过期
     * @param username  用户名
     * @param password  密码
     * @return  加密的token
     */
    public static String sign(String username, String password) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(password);
        //附带username信息
        return JWT.create().withClaim("username", username)
                .withExpiresAt(date).sign(algorithm);
    }

    public static LoginUserModel getLoginUser() {
        return (LoginUserModel) SecurityUtils.getSubject().getPrincipal();
    }
}
