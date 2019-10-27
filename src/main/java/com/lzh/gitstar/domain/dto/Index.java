package com.lzh.gitstar.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : lizhihao
 * @since : 2019/10/15, 星期二
 **/
@Data
public class Index {

    private String login;

    private String avatarUrl;

    private String createdAt;

    private Integer ownStars;

    private Integer followers;

    private String primaryLanguage;

    private String topRepository;

    private Integer contributeYears;

    private BigDecimal allStarsScore;

    private BigDecimal followerScore;

    private BigDecimal repositoryHIndexScore;

    private BigDecimal contributeRepositoryHIndexScore;

    private BigDecimal contributesScore;

    private Integer score;

    public Integer getScore() {
        BigDecimal allScore = allStarsScore
                .add(followerScore)
                .add(repositoryHIndexScore)
                .add(contributeRepositoryHIndexScore)
                .add(contributesScore);
        return allScore.intValue() ;
    }


}
