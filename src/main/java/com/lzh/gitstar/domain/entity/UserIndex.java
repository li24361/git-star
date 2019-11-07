package com.lzh.gitstar.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/
@Entity
@Table(name = "user_index")
@Data
public class UserIndex implements Serializable {

    @Id
    @Column(columnDefinition = "varchar(100) COMMENT '用户名'")
    private String login;

    @Column(columnDefinition = "varchar(100) COMMENT '头像'")
    private String avatarUrl;

    @Column(columnDefinition = "bigint(20) COMMENT '所有星星'")
    private Integer contributeStar;

    @Column(columnDefinition = "bigint(20) COMMENT '所有星星'")
    private Integer ownStar;

    @Column(columnDefinition = "bigint(20) COMMENT '所有星星'")
    private Integer topStar;

    @Column(columnDefinition = "bigint(20) COMMENT '粉丝数'")
    private Integer follower;

    @Column(columnDefinition = "varchar(20) COMMENT '主语言'")
    private String primaryLanguage;

    @Column(columnDefinition = "varchar(100) COMMENT '贡献最多项目'")
    private String topRepository;

    @Column(columnDefinition = "int(10) COMMENT '项目影响力'" )
    private Integer repositoryHIndex;

    @Column(columnDefinition = "int(10)  COMMENT '开源项目影响力'")
    private Integer contributeRepositoryHIndex;

    @Column(columnDefinition = "int(10)")
    private Integer contributes;

    @Column(columnDefinition = "int(10)")
    private Integer contributeYears;

    @Column(columnDefinition = "int(10)")
    private Integer allScore;

}
