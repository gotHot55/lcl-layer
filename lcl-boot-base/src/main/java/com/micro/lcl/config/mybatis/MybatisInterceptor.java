package com.micro.lcl.config.mybatis;

import com.micro.lcl.common.api.model.LoginUserModel;
import com.micro.lcl.common.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/3/1117:53
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class,method = "update",args = {
        MappedStatement.class,Object.class
})})
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.debug("--------sqlID----------{}",sqlId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        log.debug("---------sqlCommandType------------{}",sqlCommandType);
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            return invocation.proceed();
        }
        if (SqlCommandType.INSERT == sqlCommandType) {
            LoginUserModel sysUser = this.getLoginUser();
            Field[] fields = CommonUtil.getAllFields(parameter);
            for (Field field : fields) {
                log.debug("------field.name--------{}",field.getName());
                try {
                    if ("createBy".equals(field.getName())) {
                        field.setAccessible(true);
                        Object createBy = field.get(parameter);
                        field.setAccessible(false);
                        if (createBy == null || "".equals(createBy)) {
                            if (sysUser != null) {
                                //登录人账号
                                field.setAccessible(true);
                                field.set(parameter,sysUser.getUsername());
                                field.setAccessible(false);
                            }else {
                                field.setAccessible(true);
                                field.set(parameter, "Administrator");
                                field.setAccessible(false);
                            }
                        }
                    }
                    //注入创建时间
                    if ("createTime".equals(field.getName())) {
                        field.setAccessible(true);
                        Object createDate = field.get(parameter);
                        field.setAccessible(false);
                        if (createDate == null || "".equals(createDate)) {
                            field.setAccessible(true);
                            field.set(parameter, new Date());
                            field.setAccessible(false);
                        }
                    }
                } catch (Exception e) {
                    log.error("自动注入创建人，创建时间失败，原因：{}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        if (SqlCommandType.UPDATE == sqlCommandType) {
            Field[] fields;
            LoginUserModel sysUser = this.getLoginUser();
            if (parameter instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<?> p = (MapperMethod.ParamMap<?>) parameter;
                if (p.containsKey("et")) {
                    parameter = p.get("et");
                }else {
                    parameter = p.get("param1");
                }
                if (parameter == null) {
                    return invocation.proceed();
                }
            }
            fields = CommonUtil.getAllFields(parameter);
            for (Field field : fields) {
                log.debug("----------field.name---------:{}", field.getName());
                try {
                    if ("updateBy".equals(field.getName())) {
                        //获取登录用户信息
                        if (sysUser != null) {
                            //登录账号
                            field.setAccessible(true);
                            field.set(parameter, sysUser.getUsername());
                            field.setAccessible(false);
                        }else {
                            field.setAccessible(true);
                            field.set(parameter, "Administrator");
                            field.setAccessible(false);
                        }
                    }
                    if ("updateTime".equals(field.getName())) {
                        field.setAccessible(true);
                        field.set(parameter, new Date());
                        field.setAccessible(false);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        }

        return invocation.proceed();
    }

    private LoginUserModel getLoginUser() {
        LoginUserModel sysUser = null;
        try {
            Object principal = SecurityUtils.getSubject().getPrincipal();
            sysUser = principal != null ? (LoginUserModel) principal : null;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return sysUser;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
