package com.micro.lcl.system.controller;

import com.micro.lcl.common.api.Result;
import com.micro.lcl.system.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/3/2214:48
 */
@RestController
@Api(tags = "导入文件控制层")
@Slf4j
public class CalImportController {
    private final SysDictService sysDictService;
    @Value("${file.upload.url}")
    public String uploadPath;

    public CalImportController(SysDictService sysDictService) {
        this.sysDictService = sysDictService;
    }

    @ApiOperation("将excel文件导入数据库")
    @PostMapping("/import")
    public Result<?> importFile(@RequestParam("file") MultipartFile file) {
        String filename = file.getOriginalFilename();
        Result<Map<String,String>> result = new Result<>();
        try {
            Map<String,String> res = sysDictService.batchImport(filename, file);
            result.setResult(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, filename + "execl文件导入失败！");
        }
        return result.success("导入成功");
    }
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        Result<?> result = new Result<>();
        if (file == null) {
            return Result.error(500, "未选择文件");
        }
        String filename = file.getOriginalFilename();
        File dest = new File(uploadPath + '/' + filename);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return result.error500("上传失败！");
        }
        return result.success("上传成功！");
    }

    @GetMapping("/download")
    @ApiOperation("文件下载")
    public Result download(HttpServletResponse response, @RequestParam("fileName") String fileName) {
        Result<?> result = new Result<>();
        File file = new File(uploadPath + '/' + fileName);
        if (!file.exists()) {
            return Result.error(500, "下载文件不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLengthLong(file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i=0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        }catch (IOException e) {
            e.printStackTrace();
            return Result.error(500, "下载失败！");
        }
        return result.success("下载成功");
    }
}
