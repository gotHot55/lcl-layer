package com.micro.lcl.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2216:01
 */
@Data
@Accessors(chain = true)
@TableName("sys_dict_item")
public class SysDictItemModel implements Serializable {
    public static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 字典id
     */
    private Integer dictId;
    /**
     * 字典项文本
     */
    private String itemText;
    /**
     * 字典项值
     */
    private String itemValue;
    /**
     * 描述
     */
    private String description;
    /**
     * 排序
     */
    private Integer sortOrder;
    /**
     * 状态（1启用 0不启用）
     */
    private Integer status;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}
