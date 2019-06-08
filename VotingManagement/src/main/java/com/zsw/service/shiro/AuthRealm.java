package com.zsw.service.shiro;

import com.zsw.common.util.Encrypt;
import com.zsw.pojo.role.Resources;
import com.zsw.pojo.role.Role;
import com.zsw.pojo.user.User;
import com.zsw.service.ResourcesService;
import com.zsw.service.RoleService;
import com.zsw.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthRealm extends AuthorizingRealm {

    Logger log = LoggerFactory.getLogger(AuthRealm.class);

    private UserService userService;
    private RoleService roleService;
    private ResourcesService resourcesService;
    
    @Autowired
    public AuthRealm(RoleService roleService, UserService userService, ResourcesService resourcesService) {
        this.userService = userService;
        this.roleService = roleService;
        this.resourcesService = resourcesService;
    }
    
    
    

    /**
     * 授权,当jsp页面遇到shiro标签会执行该方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        log.debug("用户授权中...{}",pc);
        pc.getPrimaryPrincipal();
        //得到主要的身份 （是任意一个)
        User user = (User) pc.getPrimaryPrincipal();
        //根据realm得到相应的身份验证      因为可以自定义多个realm，而pc中就是获取这多个realm，如果只定义了一个realm，那么用两种方法是一样的。
//        User user = (User) pc.fromRealm(this.getName()).iterator().next();  // 根据realm名字找到对应的realm
        Set<Role> roles = getUserPermission(user);
//        user.setRole(roles);
//
//        List<String> permissions = new ArrayList<String>();
//
//        Set<Role> roles = user.getRoles();
//        for (Role role : roles) {
//            Set<Module> modules = role.getModules();
//            for (Module module : modules) {
//                permissions.add(module.getName());
//            }
//        }
//
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(getUrlByRoleSet(roles));     // 添加该用户的所有可访问的url
        if (user.getEmail().equalsIgnoreCase("root")) {
            info.addRole("superAdmin");
        }else {
            info.addRoles(roles.stream().map(role -> role.getName()).collect(Collectors.toList()));
        }

        log.debug("用户{}授权成功", user.getName());

        return info;
    }

    /**
     *  登录认证，创建用户的登录信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.debug("用户登录中..." + token.getPrincipal());

        //获取用户信息
        UsernamePasswordToken userTocken = (UsernamePasswordToken)token;
        String loginEmail = userTocken.getUsername();
        User user = userService.getUserByEmail(loginEmail);
        
        if (user == null) {
            return null;
        }
        // 好比session, 通过 sub.getPrincipal() 就可以得到当前登陆的用户
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword().toCharArray(), getName());

        return info;
    }
    
    private Set<Role> getUserPermission(User user) {
        return roleService.getRolesByUser(user);
        
    }
    
    private Set<String> getUrlByRoleSet(Collection<Role> roles) {
        List<Resources> resourcesList = resourcesService.getResourcesByRoles(roles);
        return resourcesList.stream().map(res -> res.getUrl()).collect(Collectors.toSet());
    }

}
