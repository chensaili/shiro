package com.shiro.demo;

import com.shiro.demo.model.Permission;
import com.shiro.demo.model.Role;
import com.shiro.demo.model.User;
import com.shiro.demo.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//AuthorizingRealm是与认证与授权相关的类
public class AuthRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //从session里获取到了用户
        User user=(User)principalCollection.fromRealm(this.getClass().getName()).iterator().next();
        //获取permission
        List<String>permissionList=new ArrayList<>();
        List<String>roleNameList=new ArrayList<>();
        Set<Role>roleSet=user.getRoles();
        if (!CollectionUtils.isEmpty(roleSet)){
            for(Role role:roleSet){
                roleNameList.add(role.getRname());
                Set<Permission>permissionSet=role.getPermissions();
                if(!CollectionUtils.isEmpty(permissionList)){
                    for(Permission permission:permissionSet){
                        permissionList.add(permission.getName());
                    }
                }
            }
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissionList);
        simpleAuthorizationInfo.addRoles(roleNameList);
        return simpleAuthorizationInfo;
    }

    //认证登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken=(UsernamePasswordToken)authenticationToken;
        String username=usernamePasswordToken.getUsername();
        User user=userService.findByUsername(username);
        return new SimpleAuthenticationInfo(user,user.getPassword(),this.getClass().getName());
    }
}
