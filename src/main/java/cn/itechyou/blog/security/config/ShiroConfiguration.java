package cn.itechyou.blog.security.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import cn.itechyou.blog.security.CustomShiroSessionDAO;
import cn.itechyou.blog.security.cache.JedisManager;
import cn.itechyou.blog.security.cache.JedisShiroSessionRepository;
import cn.itechyou.blog.security.cache.ShiroCacheManager;
import cn.itechyou.blog.security.cache.impl.CustomShiroCacheManager;
import cn.itechyou.blog.security.cache.impl.JedisShiroCacheManager;
import cn.itechyou.blog.security.listener.CustomSessionListener;
import cn.itechyou.blog.security.session.CustomSessionManager;
import cn.itechyou.blog.security.token.DreamerCMSRealm;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class ShiroConfiguration {
	
	@Autowired
	private RedisConfiguration redisConfiguration;
	
	/**
	 * 自定义角色过滤器 支持多个角色可以访问同一个资源 eg:/home.jsp = authc,roleOR[admin,user]
	用户有admin或者user角色 就可以访问
	 * @return
	 */
	@Bean("shirFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
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
	 * 自定义Session监听器
	 * @return
	 */
	@Bean
	public CustomSessionListener customSessionListener() {
		CustomSessionListener customSessionListener = new CustomSessionListener();
		customSessionListener.setShiroSessionRepository(getJedisShiroSessionRepository());
		return customSessionListener;
	}
	
	/**
	 * rememberMe管理器
	 * @return
	 */
	@Bean("rememberMeManager")
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		byte[] cipherKey = Base64.decode("wGiHplamyXlVB11UXWol8g==");
		cookieRememberMeManager.setCipherKey(cipherKey);
		cookieRememberMeManager.setCookie(rememberMeCookie());
		return cookieRememberMeManager;
	}
	
	@Bean("sessionIdGenerator")
	public JavaUuidSessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}
	
	/**
	 * 自定义Shiro Session Dao
	 * @return
	 */
	@Bean("customShiroSessionDAO")
	public CustomShiroSessionDAO customShiroSessionDAO() {
		CustomShiroSessionDAO customShiroSessionDAO = new CustomShiroSessionDAO();
		//会话Session ID生成器
		customShiroSessionDAO.setSessionIdGenerator(sessionIdGenerator());
		customShiroSessionDAO.setShiroSessionRepository(getJedisShiroSessionRepository());
		return customShiroSessionDAO;
	}
	
	/**
	 * 手动操作Session，管理Session
	 * @return
	 */
	@Bean("customSessionManager")
	public CustomSessionManager customSessionManager() {
		CustomSessionManager customSessionManager = new CustomSessionManager();
		customSessionManager.setCustomShiroSessionDAO(customShiroSessionDAO());
		customSessionManager.setShiroSessionRepository(getJedisShiroSessionRepository());
		return customSessionManager;
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
	public DefaultWebSecurityManager defaultWebSecurityManager() {
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager(); 
		defaultWebSecurityManager.setRealm(dreamerCMSRealm());
		defaultWebSecurityManager.setSessionManager(defaultWebSessionManager());
		defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
		defaultWebSecurityManager.setCacheManager(customShiroCacheManager());
		return defaultWebSecurityManager;
	}

	/**
	 * 用户缓存
	 * @return
	 */
	@Bean
	public CustomShiroCacheManager customShiroCacheManager() {
		CustomShiroCacheManager cacheManager = new CustomShiroCacheManager();
		cacheManager.setShiroCacheManager(getJedisShiroCacheManager());
		return cacheManager;
	}
	
	/**
	 * redis 的缓存管理器
	 * @return
	 */
	@Bean("jedisManager")
	public JedisManager getJedisManager() {
		JedisManager jedisManager = new JedisManager();
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfiguration.getMaxIdle());
        jedisPoolConfig.setMinIdle(redisConfiguration.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(redisConfiguration.getMaxWaitMillis());

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisConfiguration.getHost(), redisConfiguration.getPort(), redisConfiguration.getTimeout());
		jedisManager.setJedisPool(jedisPool);
		return jedisManager;
	}
	
	/**
	 * session 创建、删除、查询
	 * @return
	 */
	@Bean
	public JedisShiroSessionRepository getJedisShiroSessionRepository() {
		JedisShiroSessionRepository jssr = new JedisShiroSessionRepository();
		jssr.setJedisManager(getJedisManager());
		return jssr;
	}
	
	/**
	 * shiro 缓存实现，对ShiroCacheManager，我是采用redis的实现
	 * @return
	 */
	@Bean
	public ShiroCacheManager getJedisShiroCacheManager() {
		JedisShiroCacheManager cacheManager = new JedisShiroCacheManager();
		cacheManager.setJedisManager(getJedisManager());
		return cacheManager;
	}
	
	/**
	 * 静态注入，相当于调用SecurityUtils.setSecurityManager(securityManager)
	 * @return
	 */
	@Bean
	public MethodInvokingFactoryBean setDefaultWebSecurityManager() {
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		methodInvokingFactoryBean.setArguments(defaultWebSecurityManager());
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
		defaultWebSessionManager.setSessionDAO(customShiroSessionDAO());
		//session 监听
		List<SessionListener> listeners = new ArrayList<SessionListener>();
		listeners.add(customSessionListener());
		defaultWebSessionManager.setSessionListeners(listeners);
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
    
    /**
     * Shiro生命周期处理器
     * 此处一定要static的方法，否则redis的配置无法注入，会抛出java.lang.NullPointerException
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    	return new LifecycleBeanPostProcessor();
    }
    
    /**
     * Google 开源验证码
     */
    @Bean
    public DefaultKaptcha kaptcha() {
    	DefaultKaptcha kaptcha = new DefaultKaptcha();
    	Properties p = new Properties();
    	p.setProperty("kaptcha.border", "no");
    	p.setProperty("kaptcha.textproducer.char.length", "4");
    	p.setProperty("kaptcha.textproducer.font.color", "252,111,180");
    	p.setProperty("kaptcha.image.width", "200");
    	p.setProperty("kaptcha.image.height", "60");
    	Config config = new Config(p);
    	kaptcha.setConfig(config);
    	return kaptcha;
    }
}
