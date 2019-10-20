package com.lzh.gitstar.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author : lizhihao
 * @since : 2019/10/15, 星期二
 **/
@Data
public class Index {

    private String login;

    private String avatarUrl;

    /**
     * 流行度
     */
    private Long contributeStar;


    private Long ownStar;

    /**
     * 粉丝
     */
    private Long follower;

    /**
     * 主语言
     */
    private String primaryLanguage;

    private String topRepository;

    /**
     * 代码影响力
     */
    private String repositoryHIndex;

    /**
     * 开源影响力
     */
    private String contributeRepositoryHIndex;

//    private List<String> pinRepos;

    private Integer contributes;

    private Integer contributeYears;
}
