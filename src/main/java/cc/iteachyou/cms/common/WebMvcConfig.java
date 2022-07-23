package cc.iteachyou.cms.common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cc.iteachyou.cms.interceptor.UserAuthorizationInterceptor;
import cc.iteachyou.cms.service.SystemService;


/**
 * 配置静态资源映射
 * @author sam
 * @since 2017/7/16
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private UserAuthorizationInterceptor userAuthorizationInterceptor;
	@Autowired
	private SystemService systemService;
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	registry.addViewController("/favicon.ico").setViewName("forward:/resource/icon/favicon.ico");
        registry.addViewController("/admin").setViewName("forward:/admin/u/toLogin");
        registry.addViewController("/admin/").setViewName("forward:/admin/u/toLogin");
        
        /**
         * 注册默认首页路径
         */
        registry.addViewController("/").setViewName("forward:/index");
    	registry.addViewController("").setViewName("forward:/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
    
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userAuthorizationInterceptor)
						.addPathPatterns("/admin/**")
						.excludePathPatterns("/admin/u/**");
	}
}