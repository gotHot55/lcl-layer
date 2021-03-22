package com.micro.lcl.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.lcl.system.model.DictModel;
import com.micro.lcl.system.model.SysDictItemModel;
import com.micro.lcl.system.model.SysDictModel;
import com.micro.lcl.system.repository.SysDictItemRepository;
import com.micro.lcl.system.repository.SysDictRepository;
import com.micro.lcl.system.service.SysDictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2216:54
 */
@Service
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictRepository, SysDictModel> implements SysDictService {
    private final SysDictRepository dictRepository;
    private final SysDictItemRepository dictItemRepository;

    public SysDictServiceImpl(SysDictRepository dictRepository, SysDictItemRepository dictItemRepository) {
        this.dictRepository = dictRepository;
        this.dictItemRepository = dictItemRepository;
    }


    @Override
    public Map<String, List<DictModel>> queryAllDictItems() {
        Map<String, List<DictModel>> res = new HashMap<>();
        List<SysDictModel> sysDictModels = dictRepository.selectList(null);
        LambdaQueryWrapper<SysDictItemModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictItemModel::getStatus, 1);
        queryWrapper.orderByAsc(SysDictItemModel::getSortOrder);
        List<SysDictItemModel> itemModelList = dictItemRepository.selectList(queryWrapper);
        for (SysDictModel model : sysDictModels) {
            List<DictModel> dictModelList = itemModelList.stream().filter(s -> model.getId().equals(s.getDictId())).map(item -> {
                DictModel dictModel = new DictModel();
                dictModel.setText(item.getItemText());
                dictModel.setValue(item.getItemValue());
                return dictModel;
            }).collect(Collectors.toList());
            res.put(model.getDictCode(), dictModelList);
        }
        log.debug("---------登录加载系统字典——————————————————————————"+res.toString());
        return res;
    }

    @Override
    public Map<String, String> batchImport(String filename, MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();
        List<SysDictModel> sysDictModelList = new ArrayList<>();
        if (!(filename.matches("^.+\\.(?i)(xls)$") && filename.matches("^.+\\.(?i)(xlsx)$"))) {
            map.put("resultMessage", "文件格式不正确！");
            return map;
        }
        //判断excel表格版本，xls为03版本，xlsx为07版本
        boolean isExcel03 = true;
        if (filename.matches("^.+\\.(?i)(xlsx)$")) isExcel03 = false;
        //选择创建workbook的方式
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel03)
            wb = new HSSFWorkbook(is);
        else
            wb = new XSSFWorkbook(is);
        //参数代表索引,0代表引用第一个sheet工作表
        Sheet sheet = wb.getSheetAt(0);
        if (sheet!=null)
            map.put("resultMessage", "Excel表导入成功！");
        SysDictModel sysDictModel;
        //根据总行数循环
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            //获取行对象
            Row row = sheet.getRow(i);
            if (row==null) continue;
            sysDictModel = new SysDictModel();
            if (row.getCell(0) == null || row.getCell(0).getCellType() != CellType.STRING) {
                map.put("ResultMessage", "导入失败（第" + (i + 1) + "行，字典名称不能为空或者请设为文本格式");
            }
            //是字符串类型，取出内容，并判断校验
            String dictName = row.getCell(0).getStringCellValue();
            String dictCode = row.getCell(1).getStringCellValue();
            if (dictCode == null || dictCode.isEmpty()) {
                map.put("ResultMessage", "导入失败（第" + (i + 1) + "行，字典编码不能为空");
            }
            String description = row.getCell(2).getStringCellValue();
            if (description == null || description.isEmpty()) {
                map.put("ResultMessage", "导入失败（第" + (i + 1) + "行，描述不能为空");
            }
            String delFlag = row.getCell(3).getStringCellValue();
            if (delFlag == null || delFlag.isEmpty()) {
                map.put("ResultMessage", "导入失败（第" + (i + 1) + "行，描述不能为空");
            }
            sysDictModel.setDictName(dictName);
            sysDictModel.setDictCode(dictCode);
            sysDictModel.setDescription(description);
            sysDictModel.setDelFlag(Integer.valueOf(delFlag));
            sysDictModelList.add(sysDictModel);
        }
        LambdaQueryWrapper<SysDictModel> wrapper;
        for (SysDictModel dictModel : sysDictModelList) {
            String dictCode = dictModel.getDictCode();
             wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysDictModel::getDictCode, dictCode);
            Integer exist = baseMapper.selectCount(wrapper);
            if (exist == 0) {
                //不存在
                int in = baseMapper.insert(dictModel);
                if (in != 1) {
                    map.put("ResultMessage", "添加失败，" + dictCode);
                }
            }else {
                wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SysDictModel::getDictCode, dictCode);
                int up = baseMapper.update(dictModel, wrapper);
                if (up != 1) {
                    map.put("ResultMessage", "更新失败，" + dictCode);
                }
            }
        }
        return map;
    }

}
