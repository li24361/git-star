package com.lzh.gitstar.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/
@Entity
@Table(name = "user_index")
@Data
public class UserIndex {

    @Id
    @Column(columnDefinition = "varchar(100) COMMENT '用户名'")
    private String login;

    @Column(columnDefinition = "varchar(100) COMMENT '头像'")
    private String avatarUrl;

    @Column(columnDefinition = "bigint(20) COMMENT '所有星星'")
    private Long allStar;

    @Column(columnDefinition = "bigint(20) COMMENT '粉丝数'")
    private Long follower;

    @Column(columnDefinition = "varchar(20) COMMENT '主语言'")
    private String primaryLanguage;

    @Column(columnDefinition = "varchar(100)")
    private String topRepository;

    @Column(columnDefinition = "int(10)")
    private Integer repositoryHIndex;

//    @Column(columnDefinition = "int(10)")
//    private Integer repositoryGIndex;

    @Column(columnDefinition = "int(10)")
    private Integer contributeRepositoryHIndex;

//    @Column(columnDefinition = "int(10)")
//    private Integer contributeRepositoryGIndex;
}
