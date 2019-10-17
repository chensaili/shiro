package com.shiro.demo;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;

//自定义密码校验规则
public class CredentialMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获取用户输入的密码
        UsernamePasswordToken usernamePasswordToken=(UsernamePasswordToken)token;
        String password=new String(usernamePasswordToken.getPassword());
        //获取数据库的密码
        String dbPassword=(String)info.getCredentials();

        return this.equals(password,dbPassword);
    }
}
