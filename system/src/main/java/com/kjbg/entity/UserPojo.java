package com.kjbg.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPojo implements Serializable {

    private Integer id;
    private String name;
    private String mobile;
    private String mail;
    private String pwd;

    public UserPojo() {
    }
}
