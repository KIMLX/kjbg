package com.kjbg.integration;

import com.kjbg.entity.PermissionPojo;
import com.kjbg.entity.UserPojo;
import com.kjbg.integration.authenticator.IntegrationAuthenticator;
import com.kjbg.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class IntegrationUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PermissionMapper permissionMapper;

    private List<IntegrationAuthenticator> authenticators;

    @Autowired(required = false)
    public void setIntegrationAuthenticators(List<IntegrationAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    @Override
    public UserDetails loadUserByUsername(String str) throws UsernameNotFoundException {
        IntegrationAuthenticationEntity entity = IntegrationAuthenticationContext.get();
        if (entity == null){
            entity = new IntegrationAuthenticationEntity();
        }
        UserPojo pojo = this.authenticate(entity);
        if (pojo == null){
            throw new OAuth2Exception("用户名或密码错误");
        }
        // 根据用户id查询权限
        List<PermissionPojo> permissionByUserId = permissionMapper.getPermissionByUserId(pojo.getId());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (PermissionPojo permissionPojo : permissionByUserId) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permissionPojo.getEname());
            grantedAuthorities.add(grantedAuthority);
        }
        User user = new User(pojo.getName(),passwordEncoder.encode(entity.getAuthParameter("password")), grantedAuthorities);
        return user;
    }

    private UserPojo authenticate(IntegrationAuthenticationEntity entity) {
        if (this.authenticators != null) {
            for (IntegrationAuthenticator authenticator : authenticators) {
                if (authenticator.support(entity)) {
                    return authenticator.authenticate(entity);
                }
            }
        }
        return null;
    }
}
