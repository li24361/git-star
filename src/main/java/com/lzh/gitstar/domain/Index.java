package com.lzh.gitstar.domain;

import lombok.Data;

/**
 * @author : lizhihao
 * @since : 2019/10/15, 星期二
 **/
@Data
public class Index {

    private String login;

    private String avatarUrl;

    private Long allStar;

    private Long follower;

    private String primaryLanguage;

    private String topRepository;

    private String repositoryHIndex;

    private String repositoryGIndex;

    private String contributeRepositoryHIndex;

    private String contributeRepositoryGIndex;
}
