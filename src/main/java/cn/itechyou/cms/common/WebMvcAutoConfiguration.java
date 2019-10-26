/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 * Copyright © Dreamer CMS 2019 All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.itechyou.cms.common;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.itechyou.cms.interceptor.UserAuthorizationInterceptor;

/**
 * @author TODAY <br>
 *         2019-10-26 22:04
 */
//@EnableWebMvc
@Configuration
public class WebMvcAutoConfiguration implements WebMvcConfigurer,
        ApplicationContextInitializer<ConfigurableWebApplicationContext> {

    @Autowired
    private UserAuthorizationInterceptor userAuthorizationInterceptor;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.add(new DefaultHttpMessageConverter());
        converters.add(new StringHttpMessageConverter(Constant.DEFAULT_CHARSET));
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PageableMethodArgumentResolver());
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/favicon.ico").setViewName("forward:/resource/icon/favicon.ico");
        registry.addViewController("/admin").setViewName("forward:/admin/u/toLogin");
        registry.addViewController("/admin/").setViewName("forward:/admin/u/toLogin");
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
    public void initialize(ConfigurableWebApplicationContext applicationContext) {

        ServletContext servletContext = applicationContext.getServletContext();
        servletContext.setRequestCharacterEncoding(Constant.DEFAULT_ENCODING);
        servletContext.setResponseCharacterEncoding(Constant.DEFAULT_ENCODING);

    }

}
