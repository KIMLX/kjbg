package com.kjbg.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RolePojo {
    private Integer id;
    private String name;
    private Date created;

    public RolePojo() {
    }
}
