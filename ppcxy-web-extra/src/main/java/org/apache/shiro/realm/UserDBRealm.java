package org.apache.shiro.realm;

import com.ppcxy.common.utils.ShiroUser;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.service.authorize.AuthorizeService;
import com.ppcxy.cyfm.sys.service.user.PasswordService;
import com.ppcxy.cyfm.sys.service.user.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.exception.*;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.utils.Encodes;
import org.springside.modules.utils.Reflections;

import javax.annotation.PostConstruct;

/**
 *
 */
public class UserDBRealm extends AuthorizingRealm {
    
    private static final Logger log = LoggerFactory.getLogger(UserDBRealm.class);
    private static final String OR_OPERATOR = " or ";
    private static final String AND_OPERATOR = " and ";
    private static final String NOT_OPERATOR = "not ";
    private UserService userService;
    private AuthorizeService authorizeService;
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        User user = null;
        
        String username = null;
        try {
            
            if (authcToken instanceof UsernamePasswordToken) {
                UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
                
                username = token.getUsername().trim();
                
                String password = "";
                
                if (token.getPassword() != null) {
                    password = new String(token.getPassword());
                }
                
                user = userService.login(username, password);
                
            } else if (authcToken instanceof JoinAuthenticationToken) {
                JoinAuthenticationToken token = (JoinAuthenticationToken) authcToken;
                
                username = token.getUsername().trim();
                String tokenCode = token.getToken();
                
                user = userService.loginByTotp(username, tokenCode);
                token.setPassword("user");
            } else {
                
                throw new AuthenticationException("不支持的用户验证方案.");
            }
            
            
        } catch (UserNotExistsException e) {
            //用户不存在
            throw new UnknownAccountException(e.getMessage(), e);
        } catch (UserPasswordNotMatchException e) {
            //密码错误
            throw new AuthenticationException(e.getMessage(), e);
        } catch (UserTotpNotMatchException e) {
            //密码错误
            throw new AuthenticationException(e.getMessage(), e);
        } catch (UserPasswordRetryLimitExceedException e) {
            //密码重试次数过多
            throw new ExcessiveAttemptsException(e.getMessage(), e);
        } catch (UserBlockedException e) {
            //已锁定
            throw new LockedAccountException(e.getMessage(), e);
        } catch (AuthenticationException e) {
            throw new AuthenticationException(e.getMessage());
        } catch (Exception e) {
            log.error("user error", e);
            throw new AuthenticationException(new UserException("user.unknown.error", null));
        }
        
        byte[] salt = Encodes.decodeHex(user.getSalt());
        return new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getUsername(), user.getName()), user.getPassword(),
                ByteSource.Util.bytes(salt), getName());
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) Reflections.getFieldValue(principals.getPrimaryPrincipal(), "username");
        User user = userService.findByUsername(username);
        
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //授权信息设定，从授权模块获取权限信息
        authorizationInfo.setRoles(authorizeService.findRoles(user));
        authorizationInfo.setStringPermissions(authorizeService.findStringPermissions(user));
        return authorizationInfo;
    }
    
    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(PasswordService.HASH_ALGORITHM);
        matcher.setHashIterations(PasswordService.HASH_INTERATIONS);
        
        setCredentialsMatcher(matcher);
    }
    
    /**
     * 支持or and not 关键词  不支持and or混用
     *
     * @param principals
     * @param permission
     * @return
     */
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        if (permission.contains(OR_OPERATOR)) {
            String[] permissions = permission.split(OR_OPERATOR);
            for (String orPermission : permissions) {
                if (isPermittedWithNotOperator(principals, orPermission)) {
                    return true;
                }
            }
            return false;
        } else if (permission.contains(AND_OPERATOR)) {
            String[] permissions = permission.split(AND_OPERATOR);
            for (String orPermission : permissions) {
                if (!isPermittedWithNotOperator(principals, orPermission)) {
                    return false;
                }
            }
            return true;
        } else {
            return isPermittedWithNotOperator(principals, permission);
        }
    }
    
    private boolean isPermittedWithNotOperator(PrincipalCollection principals, String permission) {
        if (permission.startsWith(NOT_OPERATOR)) {
            return !super.isPermitted(principals, permission.substring(NOT_OPERATOR.length()));
        } else {
            return super.isPermitted(principals, permission);
        }
    }
    
    public void setAuthorizeService(AuthorizeService authorizeService) {
        this.authorizeService = authorizeService;
    }
    
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && (UsernamePasswordToken.class.isAssignableFrom(token.getClass()) || JoinAuthenticationToken.class.isAssignableFrom(token.getClass()));
    }
    
    @Override
    public void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
        if (JoinAuthenticationToken.class.isAssignableFrom(token.getClass())) {
            return;
        }
        super.assertCredentialsMatch(token, info);
    }
    
    
}
