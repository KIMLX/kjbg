package com.kjbg.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginParam implements Serializable {
    @NotBlank(message = "登录名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;

}