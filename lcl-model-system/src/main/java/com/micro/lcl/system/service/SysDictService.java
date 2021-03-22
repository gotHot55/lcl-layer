package com.micro.lcl.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.lcl.system.model.DictModel;
import com.micro.lcl.system.model.SysDictModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2216:52
 */
public interface SysDictService extends IService<SysDictModel> {
    Map<String, List<DictModel>> queryAllDictItems();

    /**
     * excel导入数据库
     * @param filename 文件名
     * @param file 文件
     * @return
     */
    Map<String, String> batchImport(String filename, MultipartFile file) throws IOException;
}
