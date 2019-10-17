package com.shiro.demo;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager")SecurityManager securityManager){
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl("/login");
        bean.setSuccessUrl("/index");
        bean.setUnauthorizedUrl("/unauthorized");
        //拦截器链，key代表的是访问的请求，value代表使用什么拦截器
        LinkedHashMap<String,String>filterChainDefinitionMap=new LinkedHashMap<>();
        filterChainDefinitionMap.put("/index","authc");
        filterChainDefinitionMap.put("/login","anon");
        filterChainDefinitionMap.put("/loginUser","anon");//登录是不需要做身份认证的
        filterChainDefinitionMap.put("/admin","roles[admin]");//只有角色为admin的用户才能访问admin接口
        filterChainDefinitionMap.put("/edit","perms[edit]");//具有edit权限才可以访问edit接口
        filterChainDefinitionMap.put("/druid/**","anon");
        filterChainDefinitionMap.put("/**","user");//其他接口都需要登录才可以使用
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }
    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm){
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        return manager;
    }
    @Bean("authRealm")
    public AuthRealm authRealm(@Qualifier("credentialMatcher") CredentialMatcher matcher){
        AuthRealm authRealm=new AuthRealm();
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }


    //获取校验规则
    @Bean("credentialMatcher")
    public CredentialMatcher credentialMatcher(){
        return new CredentialMatcher();
    }

    //配置spring与shiro之间的关联，这样spring在使用shiro的SecurityManager就是我们自定义的SecurityManager
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager){
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
