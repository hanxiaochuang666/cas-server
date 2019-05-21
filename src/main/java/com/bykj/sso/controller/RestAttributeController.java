package com.bykj.sso.controller;

import com.bykj.sso.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
  * @Author hanchuang
  * @Version 1.0
  * @Date add on 2019/5/20 
  * @Description   rest多行属性返回
  */
@RestController
public class RestAttributeController {

    @PostMapping("/attributes")
    public Object getAttributes(@RequestHeader HttpHeaders httpHeaders) {
        User user = new User();
        user.setEmail("rest@gmail.com");
        user.setUsername("email");
        user.setPassword("123");
        List<String> role = new ArrayList<>();
        role.add("admin");
        role.add("dev");
        //成功返回json
        return user;
    }
}
