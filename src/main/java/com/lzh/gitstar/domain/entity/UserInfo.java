package com.lzh.gitstar.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/
@Entity
@Table(name = "user")
@Data
public class UserInfo {


    @Id
    private Long id;

    @Column(columnDefinition = "varchar(100) COMMENT '用户名'")
    private String login;

    @Column(columnDefinition = "varchar(100) COMMENT 'token'")
    private String token;

    @Column(columnDefinition = "varchar(100) COMMENT '地区'")
    private String location;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '生成日期'", insertable=false, updatable=false)
    private Date createTime;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期'", insertable=false, updatable=false)
    private Date updateTime;


}
