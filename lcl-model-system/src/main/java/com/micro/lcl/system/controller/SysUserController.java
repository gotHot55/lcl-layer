package com.micro.lcl.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.micro.lcl.common.api.Result;
import com.micro.lcl.common.api.model.LoginUserModel;
import com.micro.lcl.system.constant.CommonConstant;
import com.micro.lcl.system.model.SysUserRoleModel;
import com.micro.lcl.system.service.SysUserRoleService;
import com.micro.lcl.system.service.SysUserService;
import com.micro.lcl.system.utils.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2417:25
 */
@RestController
@RequestMapping(value = "/sys/user")
@Api(tags = "用户信息")
@Slf4j
public class SysUserController {
    private final SysUserService sysUserService;
    private final SysUserRoleService sysUserRoleService;

    public SysUserController(SysUserService sysUserService, SysUserRoleService sysUserRoleService) {
        this.sysUserService = sysUserService;
        this.sysUserRoleService = sysUserRoleService;
    }

    @GetMapping(value = "/list")
    @ApiOperation("用户列表")
    public Result<IPage<LoginUserModel>> queryPageList(LoginUserModel userModel, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("------------调用用户列表请求---------------");
        Result<IPage<LoginUserModel>> result = Result.OK();
        QueryWrapper<LoginUserModel> queryWrapper = Wrappers.query(userModel);
        IPage<LoginUserModel> page = new Page<>(pageNo, pageSize);
        IPage<LoginUserModel> pageList = sysUserService.page(page, queryWrapper);
        result.setResult(pageList);

        log.info(pageList.toString());
        return result;
    }
    @GetMapping(value = "/testlist")
    @ApiOperation("所有用户列表")
    public Result<IPage<LoginUserModel>> queryPageList( @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("------------调用所有用户列表请求---------------");
        Result<IPage<LoginUserModel>> result = Result.OK();
        IPage<LoginUserModel> page = new Page<>(pageNo, pageSize);
        IPage<LoginUserModel> pageList = sysUserService.page(page);
        result.setResult(pageList);
        log.info(pageList.toString());
        return result;
    }

    @PostMapping("/add")
    @RequiresPermissions("user:add")
    @RequiresRoles("admin")
    @ApiOperation("添加")
    public Result<LoginUserModel> add(@RequestBody LoginUserModel userModel) {
        log.error("------------调用添加请求---------------");
        Result<LoginUserModel> result = Result.OK();
        try {
            String salt = RandomUtil.randomString(8);
            userModel.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(userModel.getUsername(), userModel.getPassword(), salt);
            userModel.setPassword(passwordEncode);
            userModel.setStatus(1);
            userModel.setDelFlag(CommonConstant.DEL_FLAG_0);
            sysUserService.addUserWithRole(userModel);
            result.success("添加成功");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败！");
        }
        log.error("------------调用添加请求结束---------------");
        return result;
    }

    @PutMapping(value = "/edit")
    @ApiOperation("编辑")
    @RequiresRoles("admin")
    @RequiresPermissions("user:edit")
    public Result<LoginUserModel> edit(@RequestBody LoginUserModel userModel) {
        log.error("------------调用编辑请求---------------");
        Result<LoginUserModel> res = new Result<>();
        LoginUserModel model = sysUserService.getById(userModel.getId());
        try {
            if (model == null) {
                return res.error500("未查找到用户信息");
            }else {
                userModel.setPassword(model.getPassword());
                sysUserService.editUserWithRole(userModel);
                res.success("操作成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            res.error500("操作失败");
        }
        log.error("------------调用编辑请求结束---------------");
        return res;
    }

    @ApiOperation("删除用户信息")
    @DeleteMapping("/delete")
    @RequiresRoles({"admin"})
    public Result<?> delete(@RequestParam("id") String id) {
        boolean remove = sysUserService.deleteUser(id);
        if (remove) {
            return Result.OK("删除成功", "成功");
        }
        return Result.OK("删除失败", "失败");
    }

    @ApiOperation("冻结&解冻用户")
    @PutMapping("/frozenBatch")
    @RequiresRoles({"admin"})
    public Result<LoginUserModel> frozenBatch(Map<String, String> jsonObject) {
        Result<LoginUserModel> result = new Result<>();
        try {
            String ids = jsonObject.get("ids");
            String status = jsonObject.get("status");
            String[] arrIds = ids.split(",");
            for (String id : arrIds) {
                if (StringUtils.isNotEmpty(id)) {
                    sysUserService.update(new LoginUserModel().setStatus(Integer.valueOf(status)),
                            new UpdateWrapper<LoginUserModel>().lambda().eq(LoginUserModel::getId, id));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
            return result.error500("冻结&解冻，操作失败");
        }
        return result.ok("操作成功");
    }

    @ApiOperation("通过id查询")
    @GetMapping("/queryById")
    public Result<LoginUserModel> queryById(@RequestParam("id") String id) {
        Result<LoginUserModel> result = new Result<>();
        LoginUserModel userModel = sysUserService.getById(id);
        if (userModel == null) {
            result.error500("未找到对应实体类");
        }else {
            result.setResult(userModel);
            result.setSuccess(true);
        }
        return result;
    }

    @ApiOperation("查询用户角色信息")
    @GetMapping("/queryUserRole")
    public Result<List<Integer>> queryUserRole(@RequestParam("userid") String userid) {
        Result<List<Integer>> result = Result.OK();
        List<Integer> list = new ArrayList<>();
        List<SysUserRoleModel> userRole = sysUserRoleService.list(new QueryWrapper<SysUserRoleModel>().lambda().eq(SysUserRoleModel::getUser_id, userid));
        if (userRole == null || userRole.size() == 0) {
            result.error500("未找到用户角色相关信息");
        }else {
            for (SysUserRoleModel model : userRole) {
                list.add(model.getRole_id());
            }
            result.setResult(list);
            result.setSuccess(true);
        }
        return result;
    }
}
