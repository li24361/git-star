package com.lzh.gitstar.index;

import com.lzh.gitstar.domain.graphql.Repositories;
import org.springframework.stereotype.Component;

/**
 * @author : lizhihao
 * @since : 2019/10/17, 星期四
 **/
@Component
@Deprecated
public class GIndexCalculator implements IndexCalculator{
    @Override
    public Integer calculate(Repositories repositories) {
        //计算H指数
        int result = 0;
        int totalStar=0;
        for (int i = 0; i < repositories.getNodes().size(); i++) {
            totalStar +=repositories.getNodes().get(i).getStarIndex();
            if (Math.pow(result,2) >= totalStar) {
                return result;
            }
            result++;
        }
        return result;
    }
}
