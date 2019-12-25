package com.kjbg.entity;

import lombok.Data;

import java.util.Date;
@Data
public class PermissionPojo {
    private Integer id;
    private Integer parentId;
    private String name;
    private String ename;
    private String url;
    private Date created;

    public PermissionPojo() {
    }
}
