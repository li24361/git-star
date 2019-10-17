package com.lzh.gitstar.index;

import com.lzh.gitstar.domain.graphql.Repositories;
import org.springframework.stereotype.Component;

/**
 * @author : lizhihao
 * @since : 2019/10/17, 星期四
 **/
@Component
public class HIndexCalculator implements IndexCalculator{
    @Override
    public Integer calculate(Repositories repositories) {
        //计算H指数
        int result = 0;
        for (int i = 0; i < repositories.getNodes().size(); i++) {
            if (result >= repositories.getNodes().get(i).getStargazers().getTotalCount()) {
                return result;
            }
            result++;
        }
        return result;
    }
}
