package com.lzh.gitstar.domain.graphql;

import lombok.Data;

import java.util.List;

/**
 * @author : lizhihao
 * @since : 2019/10/20, 星期日
 **/
@Data
public class ContributionsCollection {
    private int totalCommitContributions;
    private int totalRepositoryContributions;
    private int totalPullRequestContributions;
    private List<Integer> contributionYears;

    public Integer getAllContributions() {
        return this.totalCommitContributions + this.totalPullRequestContributions + this. totalRepositoryContributions;
    }
}
