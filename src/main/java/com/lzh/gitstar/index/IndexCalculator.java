package com.lzh.gitstar.index;

import com.lzh.gitstar.domain.graphql.Repositories;

/**
 * @author : lizhihao
 * @since : 2019/10/17, 星期四
 **/
public interface IndexCalculator {

    public Integer calculate(Repositories repositories);
}
