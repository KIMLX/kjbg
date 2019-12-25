package com.kjbg.integration.authenticator;

import com.kjbg.entity.UserPojo;
import com.kjbg.integration.IntegrationAuthenticationEntity;
import com.kjbg.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Primary
public class UsernamePasswordAuthenticator extends AbstractPreparableIntegrationAuthenticator {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserPojo authenticate(IntegrationAuthenticationEntity entity) {
        String name = entity.getAuthParameter("name");
        String pwd = entity.getAuthParameter("pwd");
        if(name == null || pwd == null){
            throw new OAuth2Exception("用户名或密码不能为空");
        }
        UserPojo pojo = mapper.findByName(name);
        if(passwordEncoder.matches(pwd,pojo.getPwd())){
            return pojo;
        }
        return null;
    }

    @Override
    public boolean support(IntegrationAuthenticationEntity entity) {
        return StringUtils.isEmpty(entity.getAuthType());
    }
}
