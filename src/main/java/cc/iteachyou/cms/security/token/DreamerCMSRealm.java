package cc.iteachyou.cms.security.token;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import cc.iteachyou.cms.common.Constant;
import cc.iteachyou.cms.entity.User;
import cc.iteachyou.cms.service.RoleService;
import cc.iteachyou.cms.service.UserService;

/**
 * 
 * shiro 认证 + 授权 重写
 * 
 */
public class DreamerCMSRealm extends AuthorizingRealm {

	@Resource
	private UserService userService;
	@Autowired
	private RoleService roleService;

	public DreamerCMSRealm() {
		super();
	}

	/**
	 * 认证信息，主要针对用户登录，
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		ShiroToken token = (ShiroToken) authcToken;
		User user = userService.getByUserName(token.getUsername());
		if (null == user) {
			throw new AccountException("帐号或密码不正确！");
		} else if (0 == user.getStatus()) {
			/**
			 * 如果用户的status为禁用。那么就抛出DisabledAccountException
			 */
			throw new DisabledAccountException("帐号已经禁止登录！");
		} else {
			String password = user.getPassword();

			ByteSource salt = token.getSalt();
			// 更新登录时间 last login time
			user.setLastLoginTime(new Date());
			userService.save(user);
			return new SimpleAuthenticationInfo(user, password, salt, getName());
		}
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userId = TokenManager.getUserId();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		/**
		 * 根据用户ID查询角色
		 */
		List<String> roles = roleService.queryRoleCodesByUserId(userId);
		
		List<String> permissions = new ArrayList<String>();
		if(roles.contains(Constant.ADMIN_ROLE)) {
			/**
			 * 如果为超级管理员，则查询全部权限
			 */
			permissions = roleService.queryAllPermissionCodes();
		}else {
			/**
			 * 根据用户ID查询权限
			 */
			permissions = roleService.queryPermissionCodesByUserId(userId);
		}
		if(roles != null && roles.size() > 0) {
			info.addRoles(roles);
		}
		if(permissions != null && permissions.size() > 0) {
			info.addStringPermissions(permissions);
		}
		return info;
	}

	/**
	 * 清空当前用户权限信息
	 */
	public void clearCachedAuthorizationInfo() {
		PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
		super.clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 指定principalCollection 清除
	 */
	public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
		super.clearCachedAuthorizationInfo(principals);
	}

}
