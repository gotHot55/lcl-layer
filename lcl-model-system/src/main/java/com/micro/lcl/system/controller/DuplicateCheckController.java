package com.micro.lcl.system.controller;

import com.micro.lcl.common.api.model.Result;
import com.micro.lcl.common.utils.SqlInjectionUtil;
import com.micro.lcl.system.model.DuplicateCheckVo;
import com.micro.lcl.system.repository.SysDictRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/3/416:40
 */
@Slf4j
@RestController
@RequestMapping("/sys/duplicate")
@Api(tags = "重复校验")
public class DuplicateCheckController {
    private final SysDictRepository sysDictRepository;

    public DuplicateCheckController(SysDictRepository sysDictRepository) {
        this.sysDictRepository = sysDictRepository;
    }

    @GetMapping(value = "/check")
    @ApiOperation("重复校验")
    public Result<?> check(DuplicateCheckVo checkVo) {
        int num = 0;
        log.info("---------duplicate check---------:{}",checkVo.toString());
        SqlInjectionUtil.filterContent(new String[]{checkVo.getTableName(), checkVo.getFieldName()});
//          添加  编辑页面校验
        num = sysDictRepository.dupliateCheckCountSql(checkVo);
        log.info("重复校验检查： num={}", num);
        if (num == 0) {
            return Result.OK();
        }else {
            return Result.error(HttpServletResponse.SC_OK, "该值不可用，系统中已存在！");
        }
    }
}
