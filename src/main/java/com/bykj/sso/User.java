package com.bykj.sso;

import lombok.Data;

/**
 * @desc
 * @Author：hanchuang
 * @Version 1.0
 * @Date：add on 15:01 2019/5/15
 */
@Data
public class User {

    private Integer id;

    private String username;

    private String password;

    private String expired;

    private String disabled;


}
