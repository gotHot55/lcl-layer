package com.micro.lcl.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.micro.lcl.common.api.model.Result;
import com.micro.lcl.common.utils.JwtUtil;
import com.micro.lcl.system.constant.CommonConstant;
import com.micro.lcl.system.model.PermissionTree;
import com.micro.lcl.system.model.SysPermission;
import com.micro.lcl.system.service.SysPermissionService;
import com.micro.lcl.system.service.SysRolePermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2316:04
 */
@RestController
@RequestMapping("/sys/permission")
@Slf4j
@Api(tags = "菜单权限表")
public class SysPermissionController {
    private final SysRolePermissionService sysRolePermissionService;
    private final SysPermissionService sysPermissionService;

    public SysPermissionController(SysRolePermissionService sysRolePermissionService, SysPermissionService sysPermissionService) {
        this.sysRolePermissionService = sysRolePermissionService;
        this.sysPermissionService = sysPermissionService;
    }

    @GetMapping("/list")
    @ApiOperation("加载数据节点")
    public Result<List<PermissionTree>> list() {
        long start = System.currentTimeMillis();
        Result<List<PermissionTree>> result = null;
        try {
            LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            queryWrapper.orderByAsc(SysPermission::getSortNo);
            List<SysPermission> sysPermissionList = sysPermissionService.list(queryWrapper);
            List<PermissionTree> treeList = new ArrayList<>();
            getTreeList(treeList, sysPermissionList, null);
            result = Result.OK(treeList);
            log.info("======获取全部菜单数据=====耗时:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation("系统菜单列表（一级菜单）")
    @GetMapping("/getSystemMenuList")
    public Result<List<PermissionTree>> getSystemMenuList() {
        long start = System.currentTimeMillis();
        Result<List<PermissionTree>> result = null;
        try {
            LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysPermission::getMenuType, CommonConstant.MENU_TYPE_0);
            queryWrapper.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            queryWrapper.orderByAsc(SysPermission::getSortNo);
            List<SysPermission> sysPermissionList = sysPermissionService.list(queryWrapper);
            List<PermissionTree> treeList = new ArrayList<>();
            for (SysPermission permission : sysPermissionList) {
                PermissionTree tree = new PermissionTree(permission);
                treeList.add(tree);
            }
            result = Result.OK(treeList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        log.info("======获取一级菜单数据=====耗时:" + (System.currentTimeMillis() - start));
        return result;
    }
    @ApiOperation("查询子菜单,通过父id")
    @GetMapping("/getSystemSubmenu")
    public Result<List<PermissionTree>> getSystemSubmenu(@RequestParam("parentId") String parentId) {
        long start = System.currentTimeMillis();
        Result<List<PermissionTree>> result = null;
        try {
            LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysPermission::getParentId, parentId);
            queryWrapper.eq(SysPermission::getMenuType, CommonConstant.MENU_TYPE_0);
            queryWrapper.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            queryWrapper.orderByAsc(SysPermission::getSortNo);
            List<SysPermission> sysPermissionList = sysPermissionService.list(queryWrapper);
            List<PermissionTree> treeList = new ArrayList<>();
            for (SysPermission permission : sysPermissionList) {
                PermissionTree tree = new PermissionTree(permission);
                treeList.add(tree);
            }
            result = Result.OK(treeList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }
    @ApiOperation("查询子菜单,通过多个父id,采用逗号隔开")
    @GetMapping("/getSystemSubmenuBatch")
    public Result getSystemSubmenuBatch(@RequestParam("parentIds") String parentIds) {
        try {
            LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in(SysPermission::getParentId, parentIdList);
            queryWrapper.eq(SysPermission::getMenuType, CommonConstant.MENU_TYPE_0);
            queryWrapper.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            queryWrapper.orderByAsc(SysPermission::getSortNo);
            List<SysPermission> sysPermissionList = sysPermissionService.list(queryWrapper);
            Map<Integer, List<PermissionTree>> listMap = new HashMap<>();
            for (SysPermission permission : sysPermissionList) {
                Integer pid = permission.getParentId();
                if (parentIdList.contains(pid)) {
                    List<PermissionTree> mapList = listMap.get(pid);
                    if (mapList == null) {
                        mapList = new ArrayList<>();
                    }
                    mapList.add(new PermissionTree(permission));
                    listMap.put(pid, mapList);
                }
            }
            return Result.OK(listMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子菜单失败：" + e.getMessage());
        }
    }

    @ApiOperation("查询用户拥有的菜单权限和按钮权限（根据TOKEN）")
    @GetMapping("/getUserPermissionByToken")
    public Result<?> getUserPermissionByToken(HttpServletRequest request, @RequestParam(name = "token", required = false) String token) {
        String username = null;
        // 如果没有传递token，就从header中获取token并获取用户信息
        if (StringUtils.isEmpty(token)) {
            username = JwtUtil.getUserNameByReq(request);
        }else {
            username = JwtUtil.getUsername(token);
        }
        Result<Map<String, Object>> result = new Result<>();

        try {
            if (StringUtils.isEmpty(username)) {
                return Result.error("Token不允许为空");
            }
            log.info(" ------ 通过令牌获取用户拥有的访问菜单 ---- TOKEN：{} ------ ",token);
            List<SysPermission> metaList = sysPermissionService.queryByUser(username);
            Map<String, Object> json = new HashMap<>();
            List<Map<String, Object>> menujsonArray = new ArrayList<>();
            this.getPermissionJsonArray(menujsonArray, metaList, null);
            List<Map<String, Object>> authjsonArray = new ArrayList<>();
            this.getAuthJsonArray(authjsonArray, metaList);
            //查询所有的权限
            LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
            wrapper.eq(SysPermission::getMenuType, CommonConstant.MENU_TYPE_2);
            List<SysPermission> allAuthList = sysPermissionService.list(wrapper);
            List<Map<String, Object>> allAuthjsonArray = new ArrayList<>();
            this.getAllAuthJsonArray(allAuthjsonArray, allAuthList);
            //路由菜单
            json.put("menu", menujsonArray);
            //按钮权限(用户拥有的权限集合)
            json.put("auth", authjsonArray);
            //全部权限配置集合(按钮权限,访问权限)
            json.put("allAuth", allAuthjsonArray);
            result.setResult(json);
            result.success("查询成功！");
        } catch (Exception e) {
            result.error500("查询失败" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取所有权限json数组
     * @param allAuthjsonArray
     * @param allAuthList
     */
    private void getAllAuthJsonArray(List<Map<String, Object>> allAuthjsonArray, List<SysPermission> allAuthList) {
        Map<String, Object> json;
        for (SysPermission permission : allAuthList) {
            json = new HashMap<>();
            json.put("action", permission.getPerms());
            json.put("status", permission.getStatus());
            json.put("type", permission.getPermsType());
            json.put("describe", permission.getTitle());
            allAuthjsonArray.add(json);
        }
    }

    /**
     * 获取权限json数组（按钮权限）
     * @param jsonArray
     * @param metaList
     */
    private void getAuthJsonArray(List<Map<String, Object>> jsonArray, List<SysPermission> metaList) {
        for (SysPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            Map<String, Object> json = null;
            if (CommonConstant.MENU_TYPE_2.equals(permission.getMenuType()) && CommonConstant.STATUS_1.equals(permission.getStatus())) {
                json = new HashMap<>();
                json.put("action", permission.getPerms());
                json.put("type", permission.getPermsType());
                json.put("describe", permission.getTitle());
                jsonArray.add(json);
            }
        }
    }

    private void getPermissionJsonArray(List<Map<String, Object>> menujsonArray, List<SysPermission> metaList, Map<String, Object> parentJson) {
        for (SysPermission permission : metaList) {
            if (permission.getMenuType() == null) {
                continue;
            }
            Integer tempPid = permission.getParentId();
            Map<String,Object> json = getPermissionJsonObject(permission);
            if (json == null) {
                continue;
            }
            if (parentJson == null && tempPid == null) {
                menujsonArray.add(json);
                if (!permission.isLeaf()) {
                    getPermissionJsonArray(menujsonArray, metaList, json);
                }
            } else if (parentJson != null && tempPid != null && tempPid.equals(parentJson.get("id"))) {
                // 类型( 0：一级菜单 1：子菜单 2：按钮 )
                if (CommonConstant.MENU_TYPE_2.equals(permission.getMenuType())) {
                    Map<String, Object> metaJson = (Map<String, Object>) parentJson.get("meta");
                    if (metaJson.containsKey("permissionList")) {
                        ((List)metaJson.get("permissionList")).add(json);
                    }else {
                        List<Map<String, Object>> permissionList = new ArrayList<>();
                        permissionList.add(json);
                        metaJson.put("permissionList", permissionList);
                    }
                    // 类型( 0：一级菜单 1：子菜单 2：按钮 )
                } else if (CommonConstant.MENU_TYPE_1.equals(permission.getMenuType()) || CommonConstant.MENU_TYPE_0.equals(permission.getMenuType())) {
                    if (parentJson.containsKey("children")) {
                        ((List) parentJson.get("children")).add(json);
                    }else {
                        List<Map<String, Object>> children = new ArrayList<>();
                        children.add(json);
                        parentJson.put("children", children);
                    }
                    if (!permission.isLeaf()) {
                        getPermissionJsonArray(menujsonArray, metaList, json);
                    }
                }
            }
        }
    }

    /**
     * 根据菜单配置生成路由json
     * @param permission
     * @return
     */
    private Map<String, Object> getPermissionJsonObject(SysPermission permission) {
        Map<String, Object> json = new HashMap<>();
        // 类型(0：一级菜单 1：子菜单 2：按钮)
        if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_2)) {
            return null;
        } else if (permission.getMenuType().equals(CommonConstant.MENU_TYPE_0) || permission.getMenuType().equals(CommonConstant.MENU_TYPE_1)) {
            json.put("id", permission.getId());
            if (permission.isRoute()) {
                json.put("route", "1");// 表示生成路由
            } else {
                json.put("route", "0");// 表示不生成路由
            }

            // 重要规则：路由name (通过URL生成路由name,路由name供前端开发，页面跳转使用)
            if (StringUtils.isNotEmpty(permission.getName())) {
                json.put("name", permission.getName());
            } else {
                json.put("name", urlToRouteName(permission.getUrl()));
            }

            // 是否隐藏路由，默认都是显示的
            if (permission.isHidden()) {
                json.put("hidden", true);
            }
            // 聚合路由
            if (permission.isAlwaysShow()) {
                json.put("alwaysShow", true);
            }
            json.put("component", permission.getComponent());
            Map<String, Object> meta = new HashMap<>();
            String path = permission.getUrl();
            if (!permission.isExternal() && isWWWHttpUrl(permission.getUrl())) {
                path = DigestUtils.md5DigestAsHex(permission.getUrl().getBytes());
                meta.put("url", permission.getUrl());
            }
            json.put("path", path);

            // 由用户设置是否缓存页面 用布尔值
            if (permission.isKeepAlive()) {
                meta.put("keepAlive", true);
            }

            //外链菜单打开方式
            if (permission.isExternal()) {
                meta.put("external", true);
            }

            meta.put("title", permission.getTitle());
            if (permission.getParentId() == null) {
                // 一级菜单跳转地址
                json.put("redirect", permission.getRedirect());
                if (StringUtils.isNotEmpty(permission.getIcon())) {
                    meta.put("icon", permission.getIcon());
                }
            } else {
                if (StringUtils.isNotEmpty(permission.getIcon())) {
                    meta.put("icon", permission.getIcon());
                }
            }
            json.put("meta", meta);
        }

        return json;
    }

    private void getTreeList(List<PermissionTree> treeList, List<SysPermission> metaList, PermissionTree temp) {
        for (SysPermission permission : metaList) {
            Integer tempPid = permission.getParentId();
            PermissionTree tree = new PermissionTree(permission);
            if (temp == null && tempPid == null) {
                treeList.add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeList(treeList,metaList,tree);
                }else if (temp!=null && tempPid!=null && tempPid.equals(temp.getId())){
                    temp.getChildren().add(tree);
                    if (!tree.getIsLeaf()) {
                        getTreeList(treeList, metaList, tree);
                    }
                }
            }
        }
    }
    /**
     * @return
     */
    private boolean isWWWHttpUrl(String url) {
        return url != null && (Pattern.matches("^(https?:|mailto:|tel:).*", url) || url.startsWith("{{"));
    }

    /**
     * 通过URL生成路由name（去掉URL前缀斜杠，替换内容中的斜杠‘/’为-） 举例： URL = /isystem/role RouteName =
     * isystem-role
     *
     * @return
     */
    private String urlToRouteName(String url) {
        if (StringUtils.isNotEmpty(url)) {
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            url = url.replace("/", "-");

            // 特殊标记
            url = url.replace(":", "@");
            return url;
        } else {
            return null;
        }
    }
}
