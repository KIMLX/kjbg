package com.kjbg.service;

import com.kjbg.dto.LoginParam;
import com.kjbg.entity.PermissionPojo;
import com.kjbg.entity.UserPojo;

import java.util.List;

public interface UserSecureCheck {
    /**
     * 校验用户密码
     *
     * @param loginParam
     * @return
     */
    UserPojo userCheck(LoginParam loginParam);

    /**
     * 获取用户权限
     * @param userId
     * @return
     */
    List<PermissionPojo> getPermissionByUserId(int userId);
}
