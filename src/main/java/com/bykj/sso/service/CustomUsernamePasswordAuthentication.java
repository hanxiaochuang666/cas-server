package com.bykj.sso.service;

/**
 * @desc  使用用户名和密码验证登录
 * @Author：hanchuang
 * @Version 1.0
 * @Date：add on 14:57 2019/5/15
 */

import com.bykj.sso.entity.User;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.MessageDescriptor;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CustomUsernamePasswordAuthentication extends AbstractUsernamePasswordAuthenticationHandler {

    public CustomUsernamePasswordAuthentication(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(
            UsernamePasswordCredential usernamePasswordCredential, String s) throws GeneralSecurityException, PreventedException {

        String username = usernamePasswordCredential.getUsername();

        String password = usernamePasswordCredential.getPassword();

        System.out.println("username : " + username);
        System.out.println("password : " + password);

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

        System.out.println("database username : " + info.getUsername());
        System.out.println("database password : " + info.getPassword());

        if (info == null) {
            throw new AccountException("Sorry, username not found!");
        }

        if (!info.getPassword().equals(password)) {
            throw new FailedLoginException("Sorry, password not correct!");
        } else {

            //可自定义返回给客户端的多个属性信息
            HashMap<String, Object> returnInfo = new HashMap<>();
            returnInfo.put("disabled", info.getDisabled());
            returnInfo.put("pass", info.getPassword());
            returnInfo.put("name", info.getUsername());
            returnInfo.put("id", info.getId());
//            returnInfo.put("user", info);

            final List<MessageDescriptor> list = new ArrayList<>();

            return createHandlerResult(usernamePasswordCredential,
                    this.principalFactory.createPrincipal(username, returnInfo), list);
        }
    }
}
