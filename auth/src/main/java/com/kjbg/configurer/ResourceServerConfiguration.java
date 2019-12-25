package com.kjbg.configurer;

import com.kjbg.entity.PermissionPojo;
import com.kjbg.mapper.PermissionMapper;
import com.kjbg.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import java.util.List;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        List<PermissionPojo> allPermission = permissionMapper.getAllPermission();
        http.exceptionHandling().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                // 匹配查询权限
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/uaa/oauth/**").permitAll()
                .antMatchers("/user/login").permitAll()
                .anyRequest().authenticated();
        for (PermissionPojo permissionPojo : allPermission) {
            http.authorizeRequests().antMatchers(permissionPojo.getUrl()).hasAnyAuthority(permissionPojo.getEname());
        }
    }
}
