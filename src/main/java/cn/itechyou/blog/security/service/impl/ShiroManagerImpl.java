package cn.itechyou.blog.security.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itechyou.blog.security.service.ShiroManager;
import cn.itechyou.blog.utils.LoggerUtils;

/**
 * 动态加载权限 Service
 */
public class ShiroManagerImpl implements ShiroManager {
	
	// 注意/r/n前不能有空格
	private static final String CRLF = "\r\n";

	@Resource
	@Autowired
	private ShiroFilterFactoryBean shiroFilterFactoryBean;


	@Override
	public String loadFilterChainDefinitions() {
		StringBuffer sb = new StringBuffer();
		sb.append("/u/** = anon"+CRLF);
		sb.append("/js/** = anon"+CRLF);
		sb.append("/css/** = anon"+CRLF);
		sb.append("/** = kickout,simple,login,permission"+CRLF);
		return sb.toString();
	}
	
	// 此方法加同步锁
	@Override
	public synchronized void reCreateFilterChains() {
		AbstractShiroFilter shiroFilter = null;
		try {
			shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
		} catch (Exception e) {
			LoggerUtils.getExceptionLogger().error("getShiroFilter from shiroFilterFactoryBean error!", e.getMessage());
			throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
		}

		PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
				.getFilterChainResolver();
		DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
				.getFilterChainManager();

		// 清空老的权限控制
		manager.getFilterChains().clear();

		shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
		shiroFilterFactoryBean.setFilterChainDefinitions(loadFilterChainDefinitions());
		// 重新构建生成
		Map<String, String> chains = shiroFilterFactoryBean
				.getFilterChainDefinitionMap();
		for (Map.Entry<String, String> entry : chains.entrySet()) {
			String url = entry.getKey();
			String chainDefinition = entry.getValue().trim().replace(" ", "");
			manager.createChain(url, chainDefinition);
		}

	}
	public void setShiroFilterFactoryBean(
			ShiroFilterFactoryBean shiroFilterFactoryBean) {
		this.shiroFilterFactoryBean = shiroFilterFactoryBean;
	}

}
