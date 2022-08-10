package cc.iteachyou.cms.common;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.interceptor.UserAuthorizationInterceptor;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.utils.FileConfiguration;
import lombok.extern.slf4j.Slf4j;


/**
 * 配置静态资源映射
 * @author sam
 * @since 2017/7/16
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired
	private FileConfiguration configuration;
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
    }
    
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userAuthorizationInterceptor)
						.addPathPatterns("/admin/**")
						.excludePathPatterns("/admin/u/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System system = systemService.getSystem();
		File file = new File(configuration.getResourceDir());
		if(!file.exists()) {
			log.error("资源目录不存在或设置错误！");
		}
		registry.addResourceHandler("/" + Constant.UPLOAD_PREFIX + "**").addResourceLocations(file.toURI().toString()).setCachePeriod(31556926);
	}
    
}