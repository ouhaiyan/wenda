package com.example.wenda.configuration;


import com.example.wenda.interceptor.LoginRequiredInterceptor;
import com.example.wenda.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


//@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    PassportInterceptor passportInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor).excludePathPatterns("/login") // 排除登录接口
                .excludePathPatterns("/regist"); // 排除注册接口;
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
