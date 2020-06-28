package cn.itechyou.cms.common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.interceptor.UserAuthorizationInterceptor;
import cn.itechyou.cms.service.SystemService;


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
        
        System system = systemService.getSystem();
        String staticdir = system.getStaticdir();
        if(1 == system.getBrowseType()) {//动态
        	registry.addViewController("/").setViewName("forward:/index");
        	registry.addViewController("").setViewName("forward:/index");
        }else {//静态
        	registry.addViewController("/").setViewName("redirect:/" + staticdir + "/index.html");
        	registry.addViewController("").setViewName("redirect:/" + staticdir + "/index.html");
        }
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
    
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userAuthorizationInterceptor)
						.addPathPatterns("/admin/**")
						.excludePathPatterns("/admin/u/**");
	}

    @Bean("captcha")
    public CircleCaptcha captcha() {
    	CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
    	// 自定义验证码内容为四则运算方式
    	captcha.setGenerator(new MathGenerator(1));
    	return captcha;
    }
}