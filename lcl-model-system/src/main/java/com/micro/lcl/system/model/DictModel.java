package com.micro.lcl.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2215:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class DictModel implements Serializable {
    public static final long serialVersionUID = 1L;
    /**
     * zidianvalue
     */
    private String value;
    /**
     * 字典文本
     */
    private String text;

}
