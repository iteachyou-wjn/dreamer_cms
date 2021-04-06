package cn.itechyou.cms.security.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.hutool.core.util.StrUtil;
import cn.itechyou.cms.security.filter.XssFilter;

/**
 * Filter配置
 */
@Configuration
public class FilterConfig {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        //添加过滤路径
        registration.addUrlPatterns(StrUtil.split("/*", ","));
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        //设置初始化参数
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("excludes", "/admin/*");
        initParameters.put("enabled", "true");
        registration.setInitParameters(initParameters);
        return registration;
    }
}
