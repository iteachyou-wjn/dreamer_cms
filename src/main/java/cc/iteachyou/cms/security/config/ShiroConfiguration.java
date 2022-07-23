package cc.iteachyou.cms.security.config;

import java.util.LinkedHashMap;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cc.iteachyou.cms.security.cache.ShiroRedisCacheManager;
import cc.iteachyou.cms.security.token.DreamerCMSRealm;
import cc.iteachyou.cms.utils.CipherKeyUtils;

@Configuration
public class ShiroConfiguration {
	
	/**
	 * 自定义角色过滤器 支持多个角色可以访问同一个资源 eg:/home.jsp = authc,roleOR[admin,user]
	用户有admin或者user角色 就可以访问
	 * @return
	 */
	@Bean("shirFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/u/toLogin");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        LinkedHashMap<String,String> fc = new LinkedHashMap<>();
        fc.put("/**", "anon");
        fc.put("/admin/toLogin", "anon");
        fc.put("/logout", "logout");
        fc.put("/admin/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(fc);
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        return shiroFilterFactoryBean;
    }
	
	@Bean
	public ShiroDialect shiroDialect() {
	    return new ShiroDialect();
	}
	
	/**
	 * 会话Cookie模板
	 * @return
	 */
	@Bean("sessionIdCookie")
	public SimpleCookie sessionIdCookie() {
		SimpleCookie simpleCookie = new SimpleCookie("dreamer-cms-s");
		simpleCookie.setHttpOnly(true);
		simpleCookie.setMaxAge(-1);
		return simpleCookie;
	}
	
	/**
	 * 用户信息记住我功能的相关配置
	 * @return
	 */
	@Bean("rememberMeCookie")
	public SimpleCookie rememberMeCookie() {
		SimpleCookie rememberMeCookie = new SimpleCookie("dreamer-cms-r");
		rememberMeCookie.setHttpOnly(true);
		rememberMeCookie.setMaxAge(2592000);
		return rememberMeCookie;
	}
	
	/**
	 * rememberMe管理器
	 * @return
	 */
	@Bean("rememberMeManager")
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCipherKey(CipherKeyUtils.generateCipherKey());
		cookieRememberMeManager.setCookie(rememberMeCookie());
		return cookieRememberMeManager;
	}
	
	@Bean("sessionIdGenerator")
	public JavaUuidSessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}
	
	/**
	 * 会话验证调度器
	 * @return
	 */
	@Bean
	public ExecutorServiceSessionValidationScheduler executorServiceSessionValidationScheduler() {
		ExecutorServiceSessionValidationScheduler executorServiceSessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
		executorServiceSessionValidationScheduler.setInterval(18000000);
		executorServiceSessionValidationScheduler.setSessionManager(defaultWebSessionManager());
		return executorServiceSessionValidationScheduler;
	}
	
	/**
	 * 安全管理器
	 * @return
	 */
	@Bean("securityManager")
	public DefaultWebSecurityManager defaultWebSecurityManager(RedisTemplate<Object, Object> template) {
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager(); 
		defaultWebSecurityManager.setRealm(dreamerCMSRealm());
		defaultWebSecurityManager.setSessionManager(defaultWebSessionManager());
		defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
		defaultWebSecurityManager.setCacheManager(customShiroCacheManager(template));
		return defaultWebSecurityManager;
	}

	/**
	 * 用户缓存
	 * @return
	 */
	@Bean
	public ShiroRedisCacheManager customShiroCacheManager(RedisTemplate<Object, Object> redisTemplate) {
		ShiroRedisCacheManager srcm = new ShiroRedisCacheManager(redisTemplate);
		return srcm;
	}
	
	/**
	 * 静态注入，相当于调用SecurityUtils.setSecurityManager(securityManager)
	 * @return
	 */
	@Bean
	public MethodInvokingFactoryBean setDefaultWebSecurityManager(RedisTemplate<Object, Object> template) {
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		methodInvokingFactoryBean.setArguments(defaultWebSecurityManager(template));
		return methodInvokingFactoryBean;
	}
	
	/**
	 * session 管理器
	 * @return
	 */
	@Bean("webSessionManager")
	public DefaultWebSessionManager defaultWebSessionManager() {
		DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
		defaultWebSessionManager.setSessionValidationInterval(1800000);
		defaultWebSessionManager.setGlobalSessionTimeout(1800000);
		defaultWebSessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
		//是否开启 检测，默认开启
		defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
		//是否删除无效的，默认也是开启
		defaultWebSessionManager.setDeleteInvalidSessions(true);
		//会话Cookie模板
		defaultWebSessionManager.setSessionIdCookie(sessionIdCookie());
		return defaultWebSessionManager;
	}
	
	/**
	 * 自定义Realm
	 * @return
	 */
    @Bean("dreamerCMSRealm")
    public DreamerCMSRealm dreamerCMSRealm(){
    	DreamerCMSRealm realm = new DreamerCMSRealm();
        return realm;
    }
    
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    
    /**
     * Shiro生命周期处理器
     * 此处一定要static的方法，否则redis的配置无法注入，会抛出java.lang.NullPointerException
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    	return new LifecycleBeanPostProcessor();
    }
    
}
