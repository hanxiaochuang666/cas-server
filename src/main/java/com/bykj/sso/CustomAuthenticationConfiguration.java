package com.bykj.sso;

/**
 * @desc
 * @Author：hanchuang
 * @Version 1.0
 * @Date：add on 14:56 2019/5/15
 */

import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("CustomAuthenticationConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CustomAuthenticationConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier(value = "servicesManager")
    private ServicesManager servicesManager;

    @Bean
    public AuthenticationHandler myAuthenticationHandler() {
        // 参数: name, servicesManager, principalFactory, order
        // 定义为优先使用它进行认证
        /*return new CustomUsernamePasswordAuthentication(CustomUsernamePasswordAuthentication.class.getName(),
                servicesManager, new DefaultPrincipalFactory(), 1);*/
        return new CustomerHandlerAuthentication(CustomUsernamePasswordAuthentication.class.getName(),
                servicesManager, new DefaultPrincipalFactory(), 1);
    }

    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(myAuthenticationHandler());
    }
}
