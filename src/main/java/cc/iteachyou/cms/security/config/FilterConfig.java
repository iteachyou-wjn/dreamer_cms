package cc.iteachyou.cms.security.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cc.iteachyou.cms.security.filter.XssAndSqlFilter;

/**
 * Filter配置
 * @author 王俊南
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<XssAndSqlFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssAndSqlFilter> registration = new FilterRegistrationBean<XssAndSqlFilter>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssAndSqlFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssAndSqlFilter");
        registration.setOrder(Integer.MAX_VALUE);
        Map<String, String> initParameters = new HashMap<String, String>();
        //-excludes用于配置不需要参数过滤的请求url;
        initParameters.put("excludes", "/resource/*,/admin/archives/*,/admin/category/*,/admin/templates/*");
        //-isIncludeRichText默认为true，主要用于设置富文本内容是否需要过滤。
        initParameters.put("isIncludeRichText", "true");
        registration.setInitParameters(initParameters);
        return registration;
    }
}