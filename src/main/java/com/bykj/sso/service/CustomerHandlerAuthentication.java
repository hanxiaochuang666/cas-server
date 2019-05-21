package com.bykj.sso.service;

/**
 * @desc  获取除了用户名密码之外的验证数据
 * @Author：hanchuang
 * @Version 1.0
 * @Date：add on 17:01 2019/5/15
 */

import com.bykj.sso.entity.CustomCredential;
import com.bykj.sso.entity.User;
import org.apereo.cas.authentication.*;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class CustomerHandlerAuthentication extends AbstractPreAndPostProcessingAuthenticationHandler {

    public CustomerHandlerAuthentication(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    public boolean supports(Credential credential) {
        //判断传递过来的Credential 是否是自己能处理的类型
        return credential instanceof UsernamePasswordCredential;
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {

        CustomCredential customCredential = (CustomCredential) credential;

        String username = customCredential.getUsername();
        String password = customCredential.getPassword();
        String email = customCredential.getEmail();
        String telephone = customCredential.getTelephone();
        String capcha = customCredential.getCapcha();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String right = attributes.getRequest().getSession().getAttribute("captcha_code").toString();
        // 从session中获取验证码进行校验
        if(!capcha.equalsIgnoreCase(right)){
            throw new AccountException("Sorry, capcha not correct !");
        }

        System.out.println("username : " + username);
        System.out.println("password : " + password);
        System.out.println("email : " + email);
        System.out.println("telephone : " + telephone);


        // JDBC模板依赖于连接池来获得数据的连接，所以必须先要构造连接池
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT&autoReconnect=true&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        // 创建JDBC模板
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        String sql = "SELECT * FROM user WHERE username = ?";

        User info = (User) jdbcTemplate.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper(User.class));

        System.out.println("database username : "+ info.getUsername());
        System.out.println("database password : "+ info.getPassword());


        if (info == null) {
            throw new AccountException("Sorry, username not found!");
        }

        if (!info.getPassword().equals(password)) {
            throw new FailedLoginException("Sorry, password not correct!");
        } else {
            // 自定义返回的属性
            final List<MessageDescriptor> list = new ArrayList<>();
            HashMap<String,Object> resMap = new HashMap<>();
            resMap.put("userName",info.getUsername());
            resMap.put("password",info.getPassword());
            resMap.put("email",info.getEmail());
            resMap.put("id",info.getId());

            return createHandlerResult(customCredential,
                    this.principalFactory.createPrincipal(username, resMap), list);
        }
    }
}

