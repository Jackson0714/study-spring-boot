package com.jackson0714.springboot.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {

    private Long userId;
    private String userName;
    private String password;
    private String salt;
    private String nickName;
    private String phone;
    private String avatar;
    private String miniOpenId;
    private String openId;
    private Boolean lockFlag;
    private Boolean delFlag;
    private Timestamp createTime;
    private Timestamp updateTime;
}
