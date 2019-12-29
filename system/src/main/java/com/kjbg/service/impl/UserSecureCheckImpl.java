package com.kjbg.service.impl;

import com.kjbg.dto.LoginParam;
import com.kjbg.entity.PermissionPojo;
import com.kjbg.entity.UserPojo;
import com.kjbg.mapper.PermissionMapper;
import com.kjbg.mapper.UserMapper;
import com.kjbg.service.UserSecureCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSecureCheckImpl implements UserSecureCheck {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Override
    public UserPojo userCheck(LoginParam loginParam) {
        String pass = bCryptPasswordEncoder.encode(loginParam.getPassword());
        UserPojo userPojo = userMapper.findByName(loginParam.getUsername());
        boolean matches = bCryptPasswordEncoder.matches(loginParam.getPassword(), pass);
        if(matches)return userPojo;
        return null;
    }

    @Override
    public List<PermissionPojo> getPermissionByUserId(int userId) {
        return permissionMapper.getPermissionByUserId(userId);
    }


}
