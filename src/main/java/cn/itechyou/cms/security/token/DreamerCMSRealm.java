package cn.itechyou.cms.security.token;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.exception.UnauthorizedException;
import cn.itechyou.cms.service.UserService;

/**
 * 
 * shiro 认证 + 授权 重写
 * 
 */
public class DreamerCMSRealm extends AuthorizingRealm {

    @Resource
    UserService userService;

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
            /**
             * 如果用户的status为禁用。那么就抛出<code>DisabledAccountException</code>
             */
        }
        else if (0 == user.getStatus()) {
            throw new DisabledAccountException("帐号已经禁止登录！");
        }
        else {
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

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        if (!isPermitted(permission, info)) {
            String msg = "User is not permitted [" + permission + "]";
            throw new UnauthorizedException(msg);
        }
    }

    @Override
    protected void checkRole(String role, AuthorizationInfo info) {
        if (!hasRole(role, info)) {
            String msg = "User does not have role [" + role + "]";
            throw new UnauthorizedException(msg);
        }
    }
}
