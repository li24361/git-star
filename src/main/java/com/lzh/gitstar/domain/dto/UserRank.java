package com.lzh.gitstar.domain.dto;

import lombok.Data;

/**
 * @Author: lizhihao
 * @Date: 2019/11/5
 */
@Data
public class UserRank {

    private String login;

    private String avatarUrl;

    private Integer ownStar;

    private Integer follower;

    private String primaryLanguage;

    private Integer contributeYears;

    private Integer allScore;

    private Long rank;
}
