package com.bykj.sso.config;

/**
 * @desc
 * @Author：hanchuang
 * @Version 1.0
 * @Date：add on 13:52 2019/5/20
 */
import com.bykj.sso.controller.CaptchaController;
import com.bykj.sso.controller.ServicesManagerController;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("captchaConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CustomControllerConfigurer {

    /**
     * 验证码配置,注入bean到spring中
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "captchaController")
    public CaptchaController captchaController() {
        return new CaptchaController();
    }

    /**
     * 自定义SercicesManage管理配置,注入bean到spring中
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "servicesManagerController")
    public ServicesManagerController servicesManagerController() {
        return new ServicesManagerController();
    }
}

