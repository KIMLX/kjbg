package com.kjbg.web;

import com.google.common.collect.Maps;
import com.kgbg.dto.ResponseResult;
import com.kgbg.dto.TokenDto;
import com.kgbg.util.JwtUtils;
import com.kgbg.util.OkHttpClientUtil;
import com.kjbg.dto.LoginParam;
import com.kjbg.entity.PermissionPojo;
import com.kjbg.entity.UserPojo;
import com.kjbg.service.UserSecureCheck;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
public class LoginControler {
    private static final String AUTH_TOKEN_URL = "http://gateway:4000/uaa/oauth/token";

    @Autowired
    UserSecureCheck userSecureCheck;


    @PostMapping("/login/in")
    public ResponseResult<Map> login(@Valid LoginParam loginParam) throws Exception {

        UserPojo userPojo = userSecureCheck.userCheck(loginParam);

        if(userPojo==null){
           return  new ResponseResult(ResponseResult.CodeStatus.FAIL,"用户名或密码错误");
        }else{
            List<PermissionPojo> permissions = userSecureCheck.getPermissionByUserId(userPojo.getId());
            Map map = new HashMap();
            map.put("name",userPojo.getName());
            map.put("per",permissions);
            TokenDto tokenDto = JwtUtils.buildJwtRS256(map);
            map.clear();
            map.put("token",tokenDto);
            ResponseResult<Map> mapResponseResult = new ResponseResult<>(ResponseResult.CodeStatus.OK, "登录成功");
            mapResponseResult.setData(map);
            return mapResponseResult;
        }
    }

    @PostMapping("/auth/refreshToken")
    public ResponseResult<TokenDto>  refreshToken(String refreshToken) throws Exception {
        TokenDto tokenDto = JwtUtils.refreshToken(refreshToken);
        ResponseResult<TokenDto> mapResponseResult = new ResponseResult<>(ResponseResult.CodeStatus.OK, "刷新成功");
        mapResponseResult.setData(tokenDto);
        return mapResponseResult;
    }

}
