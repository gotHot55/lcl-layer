package com.micro.lcl.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.micro.lcl.common.api.Result;
import com.micro.lcl.common.api.model.LoginUserModel;
import com.micro.lcl.common.utils.JwtUtil;
import com.micro.lcl.common.utils.RandImageUtil;
import com.micro.lcl.common.utils.RedisUtil;
import com.micro.lcl.constant.BaseConstant;
import com.micro.lcl.system.constant.CommonConstant;
import com.micro.lcl.system.model.SysLoginModel;
import com.micro.lcl.system.service.SysDepartService;
import com.micro.lcl.system.service.SysDictService;
import com.micro.lcl.system.service.SysUserRoleService;
import com.micro.lcl.system.service.SysUserService;
import com.micro.lcl.system.utils.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/1011:26
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "用户登录")
@Slf4j
public class LoginController {
    private final SysUserService sysUserService;
    private final RedisUtil redisUtil;
    private final SysDepartService sysDepartService;
    private final SysUserRoleService sysUserRoleService;
    private final SysDictService sysDictService;
    @Autowired
    public LoginController(SysUserService sysUserService, RedisUtil redisUtil, SysDepartService sysDepartService, SysUserRoleService sysUserRoleService, SysDictService sysDictService) {
        this.sysUserService = sysUserService;
        this.redisUtil = redisUtil;
        this.sysDepartService = sysDepartService;
        this.sysUserRoleService = sysUserRoleService;
        this.sysDictService = sysDictService;
    }
    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    @ApiOperation("登录接口")
    @PostMapping(value = "/login")
    public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        String captcha = sysLoginModel.getCaptcha();
        if (captcha == null) {
            result.error500("验证码不能为空！");
            return result;
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = PasswordUtil.MD5Encode(captcha + sysLoginModel.getCheckKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if(checkCode==null || !checkCode.toString().equals(lowerCaseCaptcha)) {
            result.error500("验证码错误");
            return result;
        }
        LambdaQueryWrapper<LoginUserModel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LoginUserModel::getUsername, username);
//        wrapper.eq(LoginUserModel::getStatus, 1);
        LoginUserModel userModel = sysUserService.getOne(wrapper);
        //检验用户是否有效
        Result res = sysUserService.checkUserIsEffective(userModel);
        if (!res.isSuccess()) {
            return res;
        }
        //检验用户名或密码是否正确
        String userPassword = PasswordUtil.encrypt(username, password, userModel.getSalt());
        if (!userPassword.equals(userModel.getPassword())) {
            result.error500("用户名或密码错误！");
            return result;
        }
        userInfo(userModel,result);
        log.error("打印信息:{}",result);
        return result;
    }
    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public Result<Object> logout(HttpServletRequest request) {
        String token = request.getHeader(BaseConstant.X_ACCESS_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return Result.error("token为空，用户未登录！");
        }
        LoginUserModel loginUser = JwtUtil.getLoginUser();
        if (loginUser != null) {
            redisUtil.del(BaseConstant.X_ACCESS_TOKEN + token);
            redisUtil.del(BaseConstant.PREFIX_USER_SHIRO_CACHE+loginUser.getId());
            SecurityUtils.getSubject().logout();
            return Result.OK("退出登录成功", "成功");
        }else {
            return Result.error("用户未登录");
        }
    }
    private Result<JSONObject> userInfo(LoginUserModel userModel, Result<JSONObject> result) {
        String username = userModel.getUsername();
        String password = userModel.getPassword();
        //生成token
        String token = JwtUtil.sign(username, password);
//        添加redis缓存
        redisUtil.set(BaseConstant.REDIS_USER_TOKEN+token, token,JwtUtil.EXPIRE_TIME*2/1000);
        //获取用户部门信息
        JSONObject obj = new JSONObject();
//        List<SysDepartModel> departs = sysDepartService.queryUserDeparts(userModel.getId());
//        obj.put("departs", departs);
        obj.put("token", token);
        obj.put("userInfo", userModel);
        obj.put("roles", sysUserRoleService.getRoleByUsername(username));
        obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        result.setResult(obj);
        result.success("登陆成功！");
        return result;
    }

    @ApiOperation("获取验证码图片")
    @GetMapping(value = "/randomImage/{key}")
    public Result<?> randomImage(@PathVariable String key) {
        Result<Object> result = new Result<>();
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);
            String lowerCase = code.toLowerCase();
            String realKey = PasswordUtil.MD5Encode(lowerCase + key, "utf-8");
            redisUtil.set(realKey, lowerCase, 60);
            log.error("redisKey:{},value:{}", realKey, lowerCase);
            String base64 = RandImageUtil.generate(code);
            result.setResult(base64);
            result.setSuccess(true);
        } catch (IOException e) {
            result.error500("获取验证码错误：" + e.getMessage());
            e.printStackTrace();
        }
        log.error("result:"+result);
        return result;
    }

}
