package com.lzh.gitstar.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : lizhihao
 * @since : 2019/10/15, 星期二
 **/
@Data
public class Index {

    private String login;

    private String avatarUrl;

    private String primaryLanguage;

    private String topRepository;

    private Integer contributeYears;

    private BigDecimal allStarsPercent;

    private BigDecimal followerPercent;

    private BigDecimal repositoryHIndexPercent;

    private BigDecimal contributeRepositoryHIndexPercent;

    private BigDecimal contributesPercent;

    private Integer score;

    public Integer getScore() {
        BigDecimal allScore = allStarsPercent
                .add(followerPercent)
                .add(repositoryHIndexPercent)
                .add(contributeRepositoryHIndexPercent)
                .add(contributesPercent)
                .multiply(BigDecimal.valueOf(100));
        return allScore.intValue() ;
    }


}
