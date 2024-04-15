package com.sky.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    //账号
    private Long id;

    //微信用户唯一标识
    private String openid;

    //姓名
    private String name;

    //手机号
    private String phone;

    //性别 0 女 1 男
    private String sex;

    //身份证号
    private String idNumber;

    //头像
    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //注册时间
    private LocalDateTime createTime;

    //学号
    private String studentId;

    //学校
    private String school;

    //专业
    private String major;

    //班级
    private String clazz;

    //认证
    private boolean authenticated;

    //录取通知书编号
    private String bookId;


}
