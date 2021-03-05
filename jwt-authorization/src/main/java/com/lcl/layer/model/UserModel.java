package com.lcl.layer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Todo
 *
 * @author Administrator
 * @date 2020/12/2115:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserModel {
        private Long id;
        private String username;
        private String password;
        private Integer status;
        private String clientId;
        private List<String> roles;
}
