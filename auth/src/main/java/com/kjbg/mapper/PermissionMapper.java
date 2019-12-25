package com.kjbg.mapper;

import com.kjbg.entity.PermissionPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper {
    @Select("select t.ename,t.url from permission t")
    List<PermissionPojo> getAllPermission();


    @Select("select p.ename,p.url from permission p inner join role_permission_relation rp on rp.per_id = p.id inner join user_role_relation ur on rp.role_id=ur.role_id where ur.user_id=#{userId}")
    List<PermissionPojo> getPermissionByUserId(int userId);

}
